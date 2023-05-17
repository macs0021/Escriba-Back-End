package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import com.marco.appEscritura.security.LoggedUserProvider;
import com.marco.appEscritura.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggedUserProvider loggedUserProvider;

    @GetMapping("/all")
    public Iterable<DocumentDTO> getAllDocuments(@RequestParam("page") int page,
                                                 @RequestParam("pageSize") int pageSize) {
        Iterable<Document> documents = documentService.getAllDocuments(page, pageSize);

        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);

        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID",
            description = "Returns a DocumentDTO with the info of the requested document by its ID"                     )

    public DocumentDTO getDocument(@PathVariable long id) {

        DocumentDTO document = documentService.getDocument(id).toDto();
        return document;
    }

    @PutMapping("/{id}")
    public DocumentDTO updateDocument(@PathVariable long id, @RequestBody DocumentDTO documentDto) {

        return documentService.updateDocument(id,documentDto).toDto();
    }

    @PostMapping
    public Long createDocument(@RequestBody DocumentDTO documentDto) {
        return documentService.createDocument(documentDto);
    }

    @GetMapping("/user/{username}")
    public Iterable<DocumentDTO> getDocumentsCreatedBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @GetMapping("/public/{username}")
    public Iterable<DocumentDTO> getDocumentsPublicDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPublicDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @GetMapping("/private/{username}")
    public Iterable<DocumentDTO> getDocumentsPrivateDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPrivateDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @PostMapping("/saved/{username}")
    public ResponseEntity<Void> UserSavesDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userSavesDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/saved/{username}")
    public ResponseEntity<Void> UserUnsavesDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userUnsavesDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/saved/{username}")
    public Iterable<DocumentDTO> getDocumentsSavedBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentSavedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @GetMapping("/read/{username}")
    public Iterable<DocumentDTO> getDocumentsBeingReadBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentsBeingReadBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{documentId}/visibility")
    public ResponseEntity<DocumentDTO> changeVisibility(@PathVariable long documentId, Authentication authentication){

        if (loggedUserProvider.getCurrentUser().getId().equals(documentRepository.findById(documentId).get().getCreator().getId())){
            ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(documentService.changeVisibility(documentId).toDto());
    }

    @GetMapping("/genres")
    public Iterable<DocumentDTO> getDocumentsByGenreAndPage(
            @RequestParam("genres") List<String> genres,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("tittleFragment") String tittleFragment){

        System.out.println("Recibiendo petición controlador");

        Iterable<Document> documents = documentService.getDocumentsByGenres(genres,tittleFragment,page,pageSize);

        Collection<Document> documentCollection = new ArrayList<>();

        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }


}