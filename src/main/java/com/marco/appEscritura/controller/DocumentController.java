package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public Iterable<DocumentDTO> getAllDocuments() {
        Iterable<Document> documents = documentService.getAllDocuments();
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DocumentDTO getDocument(@PathVariable long id) {

        DocumentDTO document = documentService.getDocument(id).toDto();
        return document;
    }

    @PutMapping
    public DocumentDTO updateDocument(@RequestBody DocumentDTO documentDto) {

        System.out.println("POSTING " + documentDto.getId() + " " + documentDto.getPrivateText());

        return documentService.updateDocument(documentDto).toDto();
    }

    @PostMapping
    public Long createDocument(@RequestBody DocumentDTO documentDto) {
        System.out.println("POSTING " + documentDto);
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
    public void deleteDocument(@PathVariable long documentId) {
        documentService.deleteDocument(documentId);
    }


}