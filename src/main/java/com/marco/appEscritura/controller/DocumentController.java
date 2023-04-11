package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Get document by ID",
            description = "Returns a DocumentDTO with the info of the requested document by its ID"                     )

    public DocumentDTO getDocument(@PathVariable long id) {

        DocumentDTO document = documentService.getDocument(id).toDto();
        return document;
    }

    @PutMapping
    public DocumentDTO updateDocument(@RequestBody DocumentDTO documentDto) {

        System.out.println("POSTING " + documentDto.getId() + " " + documentDto.getText());

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
    public ResponseEntity<Void> deleteDocument(@PathVariable long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{documentId}/visibility")
    public ResponseEntity<DocumentDTO> changeVisibility(@PathVariable long documentId){

        return ResponseEntity.status(HttpStatus.OK).body(documentService.changeVisibility(documentId).toDto());
    }

    @GetMapping(params = {"genres", "page", "pageSize"})
    public List<DocumentDTO> getDocumentsByGenreAndPage(
            @RequestParam("genres") List<String> genres,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {

        return documentService.getDocumentsByGenres(genres,page,pageSize).stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());
    }


}