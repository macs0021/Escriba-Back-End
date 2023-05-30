package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Response;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import com.marco.appEscritura.security.LoggedUserProvider;
import com.marco.appEscritura.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /*@GetMapping("/all")
    public ResponseEntity<Iterable<DocumentDTO>> getAllDocuments(@RequestParam("page") int page,
                                                 @RequestParam("pageSize") int pageSize) {
        Iterable<Document> documents = documentService.getAllDocuments(page, pageSize);

        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);

        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }*/

    @GetMapping("/checkOwner/{documentId}")
    public ResponseEntity<Boolean> checkOwner(@PathVariable long documentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(documentService.checkOwner(username, documentId));
    }

    @GetMapping("/checkPublic/{documentId}")
    public ResponseEntity<Boolean> checkPublic(@PathVariable long documentId) {
        return ResponseEntity.ok(documentService.checkPublic(documentId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID",
            description = "Returns a DocumentDTO with the info of the requested document by its ID")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #id) || @documentService.checkPublic(#id)")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable long id) {

        DocumentDTO document = documentService.getDocument(id).toDto();
        return ResponseEntity.ok(document);
    }

    @GetMapping("/recommendation")
    public ResponseEntity<DocumentDTO> getRecommendation() {
        return ResponseEntity.ok(documentService.findMostLikedPost().get().toDto());
    }

    @PutMapping("/{id}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #id)")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable long id, @RequestBody DocumentDTO documentDto) {
        Document updatedDocument = documentService.updateDocument(id, documentDto);
        DocumentDTO updatedDocumentDto = updatedDocument.toDto();
        return ResponseEntity.ok(updatedDocumentDto);
    }

    @PostMapping
    public ResponseEntity<Long> createDocument(@RequestBody DocumentDTO documentDto) {
        Long documentId = documentService.createDocument(documentDto);
        return ResponseEntity.ok(documentId);
    }

    /*@GetMapping("/user/{username}")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsCreatedBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }*/

    @GetMapping("/public/{username}")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsPublicDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPublicDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }


    @GetMapping("/private/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsPrivateDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPrivateDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }

    @PostMapping("/saved/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Void> UserSavesDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userSavesDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/saved/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Void> UserUnsavedDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userUnsavedDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/saved/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsSavedBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentSavedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }

    @GetMapping("/read/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsBeingReadBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getDocumentsBeingReadBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #documentId)")
    public ResponseEntity<Void> deleteDocument(@PathVariable long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{documentId}/visibility")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #documentId)")
    public ResponseEntity<DocumentDTO> changeVisibility(@PathVariable long documentId) {

        return ResponseEntity.status(HttpStatus.OK).body(documentService.changeVisibility(documentId).toDto());
    }

    @GetMapping("/genres")
    public ResponseEntity<Iterable<DocumentDTO>> getDocumentsByGenreAndPage(
            @RequestParam("genres") List<String> genres,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("tittleFragment") String tittleFragment) {

        Iterable<Document> documents = documentService.getDocumentsByGenres(genres, tittleFragment, page, pageSize);

        Collection<Document> documentCollection = new ArrayList<>();

        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }


}