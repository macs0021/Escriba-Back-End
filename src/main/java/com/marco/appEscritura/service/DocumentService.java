package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.NotExistingDocument;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

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

        if (!user.getSavedDocuments().contains(document))
            user.getSavedDocuments().add(document);

        document.getSavedBy().add(user);

        if (!user.getSavedDocuments().contains(document)) {
            user.getSavedDocuments().add(document);
            userRepository.save(user);
        }
        documentRepository.save(document);
    }


    public Long updateDocument(DocumentDTO documentDto) {
        return documentRepository.save(DtoToDocument(documentDto)).getId();
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


    private Document DtoToDocument(DocumentDTO documentDto) {

        System.out.println(documentDto);
        Optional<User> user = userRepository.findOneByUsername(documentDto.getCreatorUsername());
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setSynopsis(documentDto.getSynopsis());
        document.setTittle(documentDto.getTittle());
        document.setPrivateText(documentDto.getPrivateText());
        document.setCover(documentDto.getCover());
        document.setCreator(user.get());
        document.setGenres(documentDto.getGenres());
        return document;

    }

}
