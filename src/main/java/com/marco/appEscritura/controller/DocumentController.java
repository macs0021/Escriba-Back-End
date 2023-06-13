package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Response;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.Document.AlreadyExistingDocument;
import com.marco.appEscritura.exceptions.Document.NotExistingDocument;
import com.marco.appEscritura.exceptions.User.AlreadyExistingUser;
import com.marco.appEscritura.exceptions.User.NotExistingUser;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import com.marco.appEscritura.security.LoggedUserProvider;
import com.marco.appEscritura.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.ConstraintViolationException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NotExistingDocument.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> NotExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AlreadyExistingDocument.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> AlreadyExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Operation(
            summary = "Verificar propietario del documento",
            description = "Comprueba si el usuario autenticado es el propietario del documento dado su ID."
    )
    @GetMapping("/checkOwner/{documentId}")
    public ResponseEntity<Boolean> checkOwner(@PathVariable long documentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(documentService.checkOwner(username, documentId));
    }

    @Operation(
            summary = "Verificar si un documento es público",
            description = "Comprueba si un documento es público dado su ID."
    )
    @GetMapping("/checkPublic/{documentId}")
    public ResponseEntity<Boolean> checkPublic(@PathVariable long documentId) {
        return ResponseEntity.ok(documentService.checkPublic(documentId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener documento mediante ID",
            description = "Devuelve la información del documento solicitado")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #id) || @documentService.checkPublic(#id)")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable long id) {

        DocumentDTO document = documentService.getDocument(id).toDto();
        return ResponseEntity.ok(document);
    }

    @Operation(
            summary = "Obtener recomendación global",
            description = "Devuelve la información de un documento que se recomienda de forma global."
    )
    @GetMapping("/recommendation")
    public ResponseEntity<DocumentDTO> getRecommendation() {
        return ResponseEntity.ok(documentService.findMostLikedPost().get().toDto());
    }

    @Operation(
            summary = "Actualizar documento",
            description = "Actualiza los valores de un documento dado su ID y los nuevos valores."
    )
    @PutMapping("/{id}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #id)")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable long id, @RequestBody DocumentDTO documentDto) {
        Document updatedDocument = documentService.updateDocument(id, documentDto);
        DocumentDTO updatedDocumentDto = updatedDocument.toDto();
        return ResponseEntity.ok(updatedDocumentDto);
    }

    @Operation(
            summary = "Crea un documento",
            description = "Crea un documento en la base de datos a partir de los datos que se adjuntan a la petición."
    )
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

    @Operation(
            summary = "Obtener documentos publicos de un usuario",
            description = "Devuelve los documentos con visibilidad pública de un usuario a partir de su nombre de usuario"
    )
    @GetMapping("/public/{username}")
    public ResponseEntity<Iterable<DocumentDTO>> getPublicDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPublicDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }


    @Operation(
            summary = "Obtener documentos privados de un usuario",
            description = "Devuelve los documentos con visibilidad privada de un usuario a partir de su nombre de usuario"
    )
    @GetMapping("/private/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Iterable<DocumentDTO>> getPrivateDocumentsBy(@PathVariable String username) {
        Iterable<Document> documents = documentService.getPrivateDocumentsCreatedBy(username);
        Collection<Document> documentCollection = new ArrayList<>();
        documents.forEach(documentCollection::add);
        return ResponseEntity.ok(documentCollection.stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList()));
    }

    @Operation(
            summary = "Guarda un documento para un usuario",
            description = "Guarda un documento para un usuario a partir de su nombre de usuario e ID del documento"
    )
    @PostMapping("/saved/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Void> UserSavesDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userSavesDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Desmarcar documento guardado", description = "El usuario puede desmarcar un documento previamente guardado.")
    @DeleteMapping("/saved/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Void> UserUnsavedDocument(@PathVariable String username, @RequestParam Long savedDocument) {
        documentService.userUnsavedDocument(username, savedDocument);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Obtener documentos guardados", description = "Obtiene los documentos guardados por el usuario.")
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

    @Operation(summary = "Obtener documentos en lectura", description = "Obtiene los documentos que el usuario está leyendo.")
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
    @Operation(summary = "Eliminar documento", description = "El propietario del documento puede eliminarlo.")
    @DeleteMapping("/{documentId}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #documentId)")
    public ResponseEntity<Void> deleteDocument(@PathVariable long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "Cambiar visibilidad", description = "El propietario del documento puede cambiar su visibilidad.")
    @PatchMapping("/{documentId}/visibility")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #documentId)")
    public ResponseEntity<DocumentDTO> changeVisibility(@PathVariable long documentId) {

        return ResponseEntity.status(HttpStatus.OK).body(documentService.changeVisibility(documentId).toDto());
    }
    @Operation(summary = "Obtener documentos por género y página", description = "Obtiene los documentos públicos basados en los géneros proporcionados y el fragmento del título.")
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