package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.ReadingID;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.NotExistingDocument;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private ActivityService activityService;

    public Document getDocument(Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("The document " + documentOptional.get().getTittle() + " does not exist");
        }
        return documentOptional.get();
    }

    public Iterable<Document> getAllDocuments(int page, int pageSize) {
        /*List<Document> documents = documentRepository.findAllOrderByRatingAndTitle();

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, documents.size());

        if(startIndex>=documents.size()){
            return Collections.emptyList();
        }

        return documents.subList(startIndex, endIndex);*/

        int offset = page * pageSize;
        return documentRepository.getPageDocuments(pageSize,offset);
    }

    public Long createDocument(DocumentDTO documentDto) {

        Document document = DtoToDocument(documentDto);
        User user = userRepository.findOneByUsername(documentDto.getCreatorUsername()).get();
        user.getCreated().add(document);
        Document savedDocument = documentRepository.save(document);
        userRepository.save(user);

        activityService.createDocumentCreationEvent(user.getUsername(),savedDocument.getId());
        return savedDocument.getId();

    }

    public void userSavesDocument(String username, Long documentId) {
        User user = userRepository.findOneByUsername(username).get();
        Document document = documentRepository.findById(documentId).get();

        document.getSavedBy().add(user);

        if (!user.getSavedDocuments().contains(document)) {
            user.getSavedDocuments().add(document);
            userRepository.save(user);
        }
        documentRepository.save(document);
    }

    public void userUnsavesDocument(String username, Long documentId) {
        User user = userRepository.findOneByUsername(username).get();
        Document document = documentRepository.findById(documentId).get();

        document.getSavedBy().remove(user);

        if (user.getSavedDocuments().contains(document)) {
            user.getSavedDocuments().remove(document);
            userRepository.save(user);
        }
        documentRepository.save(document);
    }


    public Document updateDocument(long id,DocumentDTO documentDto) {

        Optional<Document> documentOptional = documentRepository.findById(id);

        if(!documentOptional.isPresent()){}

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
        return user.get().getCreated().stream().collect(Collectors.toList());
    }

    public Iterable<Document> getPublicDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        if (user.isPresent()) {
            System.out.println("Enviando publicos");
            List<Document> publicDocuments = user.get().getCreated().stream()
                    .filter(Document::isPublic)
                    .collect(Collectors.toList());
            return publicDocuments;
        } else {
            System.out.println("Usuario no encontrado");
            return Collections.emptyList();
        }
    }

    public Iterable<Document> getPrivateDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        if (user.isPresent()) {
            System.out.println("Enviando privados");
            List<Document> privateDocuments = user.get().getCreated().stream()
                    .filter(document -> !document.isPublic())
                    .collect(Collectors.toList());
            return privateDocuments;
        } else {
            System.out.println("Usuario no encontrado");
            return Collections.emptyList();
        }
    }

    public Iterable<Document> getDocumentSavedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        return user.get().getSavedDocuments().stream().collect(Collectors.toList());
    }

    public void deleteDocument(long id) {
        documentRepository.deleteById(id);
    }

    public List<Document> getDocumentsBeingReadBy(String username){
        Optional<User> user = userRepository.findOneByUsername(username);

        if(!user.isPresent()){
            //Excepcion
        }

        return user.get().getReading().stream()
                .map(Reading::getBeingRead)
                .collect(Collectors.toList());
    }

    public Document changeVisibility(long id){
        Optional<Document> documentOptional = documentRepository.findById(id);

        if(!documentOptional.isPresent()){

        }
        if(documentOptional.get().isPublic()){
            activityService.createDocumentCreationEvent(documentOptional.get().getCreator().getUsername(),documentOptional.get().getId());
        }

        Document document = documentOptional.get();
        document.setPublic(!document.isPublic());

        return documentRepository.save(document);
    }

    public Iterable<Document> getDocumentsByGenres(List<String> genres,String tittleFragment, int page, int pageSize){

        int offset = page * pageSize;

        List<Document> documents = documentRepository.findAllByGenresAndTittleFragment(genres,tittleFragment, pageSize, offset, genres.size());

        return documents;

    }

    public void updateRating(int newRating,int oldRating, long documentID){
        Optional<Document> optionalDocument = documentRepository.findById(documentID);

        if(!optionalDocument.isPresent()){

        }

        Document document = optionalDocument.get();

        document.setRating((int)((document.getRating()+newRating-oldRating)/document.getReviews().size()));

        documentRepository.save(document);
    }

    public void addRating(int rating, long documentID){
        Optional<Document> optionalDocument = documentRepository.findById(documentID);

        if(!optionalDocument.isPresent()){

        }

        Document document = optionalDocument.get();

        document.setRating((int)((document.getRating()+rating)/(document.getReviews().size()+1)));

        documentRepository.save(document);
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
        return document;

    }

}
