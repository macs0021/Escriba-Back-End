package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.Document.NotExistingDocument;
import com.marco.appEscritura.exceptions.User.NotExistingUser;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReadingService readingService;

    @Autowired
    ActivityService activityService;

    public Document getDocument(Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("The document " + documentOptional.get().getTittle() + " does not exist");
        }
        return documentOptional.get();
    }

    public Iterable<Document> getAllDocuments(int page, int pageSize) {
        int offset = page * pageSize;
        return documentRepository.getPageDocuments(pageSize, offset);
    }

    public Long createDocument(DocumentDTO documentDto) {

        Document document = DtoToDocument(documentDto);
        User user = userRepository.findOneByUsername(documentDto.getCreatorUsername()).get();
        user.getCreated().add(document);
        Document savedDocument = documentRepository.save(document);

        userRepository.save(user);

        activityService.createDocumentCreationEvent(user.getUsername(), savedDocument.getId());
        return savedDocument.getId();

    }

    public void userSavesDocument(String username, Long documentId) {
        Optional<User> userOptional = userRepository.findOneByUsername(username);
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if(!userOptional.isPresent()){
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }

        if(!documentOptional.isPresent()){
            throw new NotExistingDocument("Document with id: " + documentId + " does not exist");
        }

        Document document = documentOptional.get();
        User user = userOptional.get();

        document.getSavedBy().add(user);

        if (!user.getSavedDocuments().contains(document)) {
            user.getSavedDocuments().add(document);
            userRepository.save(user);
        }
        documentRepository.save(document);
    }

    public void userUnsavedDocument(String username, Long documentId) {
        Optional<User> userOptional = userRepository.findOneByUsername(username);
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if(!userOptional.isPresent()){
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }

        if(!documentOptional.isPresent()){
            throw new NotExistingDocument("Document with id: " + documentId + " does not exist");
        }

        Document document = documentOptional.get();
        User user = userOptional.get();

        document.getSavedBy().remove(user);

        if (user.getSavedDocuments().contains(document)) {
            user.getSavedDocuments().remove(document);
            userRepository.save(user);
        }
        documentRepository.save(document);
    }


    public Document updateDocument(long id, DocumentDTO documentDto) {

        Optional<Document> documentOptional = documentRepository.findById(id);

        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("Document with id: " + id + " does not exist");
        }

        Document document = documentOptional.get();

        document.setText(documentDto.getText());
        document.setGenres(documentDto.getGenres());
        document.setTittle(documentDto.getTittle());
        document.setCover(documentDto.getCover());
        document.setSynopsis(documentDto.getSynopsis());

        return documentRepository.save(document);
    }

    public Iterable<Document> getDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);

        if(!user.isPresent()){
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }
        return user.get().getCreated().stream().collect(Collectors.toList());
    }

    public Iterable<Document> getPublicDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        if (!user.isPresent()) {
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }
        List<Document> publicDocuments = user.get().getCreated().stream()
                .filter(Document::isPublic)
                .collect(Collectors.toList());
        return publicDocuments;
    }

    public Iterable<Document> getPrivateDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        if (!user.isPresent()) {
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }
        List<Document> privateDocuments = user.get().getCreated().stream()
                .filter(document -> !document.isPublic())
                .collect(Collectors.toList());
        return privateDocuments;
    }

    public Iterable<Document> getDocumentSavedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);

        if(!user.isPresent()){
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }

        return user.get().getSavedDocuments().stream().collect(Collectors.toList());
    }

    public void deleteDocument(long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if(!documentOptional.isPresent()){
            throw new NotExistingDocument("Document with id: " + id + " does not exist");
        }

        documentRepository.deleteById(id);
    }

    public List<Document> getDocumentsBeingReadBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);

        if (!user.isPresent()) {
            throw new NotExistingUser("User with username: " + username + " does not exist");
        }

        return user.get().getReading().stream()
                .map(Reading::getBeingRead)
                .collect(Collectors.toList());
    }

    public Document changeVisibility(long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("Document with id: " + id + " does not exist");
        }
        if (documentOptional.get().isPublic()) {
            activityService.createDocumentCreationEvent(documentOptional.get().getCreator().getUsername(), documentOptional.get().getId());
        }

        Document document = documentOptional.get();
        document.setPublic(!document.isPublic());

        return documentRepository.save(document);
    }

    public Iterable<Document> getDocumentsByGenres(List<String> genres, String tittleFragment, int page, int pageSize) {

        int offset = page * pageSize;

        List<Document> documents = documentRepository.findAllByGenresAndTittleFragment(genres, tittleFragment, pageSize, offset, genres.size());

        return documents;

    }

    public void updateRating(int newRating, int oldRating, long documentID) {
        Optional<Document> optionalDocument = documentRepository.findById(documentID);

        if (!optionalDocument.isPresent()) {
            throw new NotExistingDocument("Document with id: " + documentID + " does not exist");
        }

        Document document = optionalDocument.get();

        document.setRating((int) (((double) document.getRating() * document.getReviews().size()) + newRating - oldRating) / document.getReviews().size());

        documentRepository.save(document);
    }

    public boolean checkPublic(long documentId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("Document with id: " + documentId + " does not exist");
        }
        return documentOptional.get().isPublic();
    }

    public boolean checkOwner(String username, long documentId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("Document with id: " + documentId + " does not exist");
        }
        return documentOptional.get().getCreator().getUsername() == username;
    }

    public void addRating(int rating, long documentID) {
        Optional<Document> optionalDocument = documentRepository.findById(documentID);

        if (!optionalDocument.isPresent()) {
            throw new NotExistingDocument("Document with id: " + documentID + " does not exist");
        }

        Document document = optionalDocument.get();
        int totalReviews = document.getReviews().size();
        int currentRatingSum = document.getRating() * totalReviews;
        int newRatingSum = currentRatingSum + rating;

        double newRatingAverage = (double) newRatingSum / (totalReviews + 1);
        int newRatingRounded = (int) Math.round(newRatingAverage);

        document.setRating(newRatingRounded);

        documentRepository.save(document);
    }

    public Optional<Document> findMostLikedPost() {
        Optional<Document> mostLikedPost = documentRepository.findRandomMostLikedPost();
        if (mostLikedPost.isPresent()) {
            return mostLikedPost;
        }
        return Optional.empty();
    }


    private Document DtoToDocument(DocumentDTO documentDto) {
        Optional<User> user = userRepository.findOneByUsername(documentDto.getCreatorUsername());
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setSynopsis(documentDto.getSynopsis());
        document.setTittle(documentDto.getTittle());
        document.setText(documentDto.getText());
        document.setCover(documentDto.getCover());
        document.setCreator(user.get());

        document.setGenres(documentDto.getGenres());
        document.setRating(documentDto.getRating());
        List<Reading> readingList = documentDto.getReadings().stream()
                .map(readingDTO -> readingService.DtoToReading(readingDTO))
                .collect(Collectors.toList());
        document.setBeingRead(readingList);
        document.setPublic(documentDto.isPublic());
        document.setRating(documentDto.getRating());
        return document;

    }

}
