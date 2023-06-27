package com.marco.EscribaServer.controller;

import com.marco.EscribaServer.dto.CommentDTO;
import com.marco.EscribaServer.entity.Comment;
import com.marco.EscribaServer.exceptions.Comment.AlreadyExistingReview;
import com.marco.EscribaServer.exceptions.Comment.NotExistingComment;
import com.marco.EscribaServer.service.CommentService;
import com.marco.EscribaServer.service.DocumentService;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    DocumentService documentService;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }

    @ExceptionHandler({NotExistingComment.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> NotExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AlreadyExistingReview.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> AlreadyExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @Operation(summary = "Guardar comentario", description = "Guarda un nuevo comentario en un documento específico.")
    @PostMapping
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #commentDTO.getPostedIn()) || @documentService.checkPublic(#commentDTO.getPostedIn())")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.saveComment(commentDTO).toDto());
    }

    @Operation(summary = "Obtener reseñas de un documento", description = "Obtiene todas las reseñas de un documento específico.")
    @GetMapping("/{document}/review")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #document) || @documentService.checkPublic(#document)")
    public ResponseEntity<List<CommentDTO>> getReviewsFromDocument(@PathVariable Long document){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getReviewsOfDocument(document)
                        .stream().map(Comment::toDto)
                        .collect(Collectors.toList()));
    }

    @Operation(summary = "Eliminar comentario", description = "Elimina un comentario específico por su ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("@commentService.getCommentByID(#id).getPostedBy().getUsername() == authentication.principal.getUsername()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Actualizar comentario", description = "Actualiza un comentario específico por su ID.")
    @PutMapping("/{commentID}")
    @PreAuthorize("@commentService.getCommentByID(#commentID).getPostedBy().getUsername() == authentication.principal.getUsername()")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentID, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentID,commentDTO));
    }

    @Operation(summary = "Crear respuesta", description = "Crea una respuesta para una reseña específica.")
    @PostMapping("/{reviewID}/replies")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #commentDTO.getPostedIn().getId()) || @documentService.checkPublic(#commentDTO.getPostedIn().getId())")
    public ResponseEntity<CommentDTO> createReply(@PathVariable Long reviewID, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.saveComment(commentDTO).toDto());
    }

    @Operation(summary = "Obtener respuestas de una reseña", description = "Obtiene todas las respuestas para una reseña específica.")
    @GetMapping("/{reviewID}/replies")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#reviewID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#reviewID).getPostedIn().getId())")
    public ResponseEntity<List<CommentDTO>> getRepliesOfReview(@PathVariable Long reviewID){
        return ResponseEntity.status(HttpStatus.OK).
                body(commentService.getResponsesOfReview(reviewID)
                        .stream().map(Comment::toDto).collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener reseña por ID", description = "Obtiene una reseña específica por su ID.")
    @GetMapping("/reviews/{reviewID}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#reviewID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#reviewID).getPostedIn().getId())")
    public ResponseEntity<CommentDTO> getReviewByID(@PathVariable Long reviewID){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getReviewByID(reviewID).toDto());
    }

    @Operation(summary = "Obtener respuesta por ID", description = "Obtiene una respuesta específica por su ID.")
    @GetMapping("/responses/{responseID}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#responseID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#responseID).getPostedIn().getId())")
    public ResponseEntity<CommentDTO> getResponseByID(@PathVariable Long responseID){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getResponseByID(responseID).toDto());
    }

}
