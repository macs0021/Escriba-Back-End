package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.ReadingID;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.NotExistingDocument;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReadingService readingService;

    public Document getDocument(Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);
        if (!documentOptional.isPresent()) {
            throw new NotExistingDocument("The document " + documentOptional.get().getTittle() + " does not exist");
        }
        return documentOptional.get();
    }

    public Iterable<Document> getAllDocuments() {
        Iterable<Document> documents = documentRepository.findAll();
        return documents;
    }

    public Long createDocument(DocumentDTO documentDto) {

        Document document = DtoToDocument(documentDto);
        User user = userRepository.findOneByUsername(documentDto.getCreatorUsername()).get();
        user.getCreated().add(document);
        Long id = documentRepository.save(document).getId();
        userRepository.save(user);
        return id;

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


    public Document updateDocument(DocumentDTO documentDto) {
        return documentRepository.save(DtoToDocument(documentDto));
    }

    public Iterable<Document> getDocumentsCreatedBy(String username) {
        Optional<User> user = userRepository.findOneByUsername(username);
        return user.get().getCreated().stream().collect(Collectors.toList());
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

        Document document = documentOptional.get();
        document.setPublic(!document.isPublic());

        return documentRepository.save(document);
    }

    public List<Document> getDocumentsByGenres(List<String> genres, int page, int pageSize){
        List<Document> documents = documentRepository.findByGenresIn(genres);

        Comparator<Document> comparator = Comparator.comparing(Document::getRating, Comparator.reverseOrder());
        Collections.sort(documents, comparator);

        return documents.subList(page, page+pageSize+1);

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

        document.setRating((int)((document.getRating()+rating)/document.getReviews().size()+1));

        documentRepository.save(document);
    }


    private Document DtoToDocument(DocumentDTO documentDto) {

        System.out.println(documentDto);
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
        return document;

    }

}
