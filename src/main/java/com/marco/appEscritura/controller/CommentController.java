package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Comment;
import com.marco.appEscritura.service.CommentService;
import com.marco.appEscritura.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
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

    @PostMapping
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #commentDTO.getPostedIn()) || @documentService.checkPublic(#commentDTO.getPostedIn())")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.saveComment(commentDTO).toDto());
    }
    @GetMapping("/{document}/review")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #document) || @documentService.checkPublic(#document)")
    public ResponseEntity<List<CommentDTO>> getReviewsFromDocument(@PathVariable Long document){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getReviewsOfDocument(document).stream().map(Comment::toDto).collect(Collectors.toList()));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@commentService.getCommentByID(#id).postedBy() == authentication.principal.getUsername()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{commentID}")
    @PreAuthorize("@commentService.getCommentByID(#commentID).postedBy() == authentication.principal.getUsername()")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentID, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentID,commentDTO));
    }

    @PostMapping("/{reviewID}/replies")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), #commentDTO.getPostedIn().getId()) || @documentService.checkPublic(#commentDTO.getPostedIn().getId())")
    public ResponseEntity<CommentDTO> createReply(@PathVariable Long reviewID, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.saveComment(commentDTO).toDto());
    }

    @GetMapping("/{reviewID}/replies")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#reviewID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#reviewID).getPostedIn().getId())")
    public ResponseEntity<List<CommentDTO>> getRepliesOfReview(@PathVariable Long reviewID){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getResponsesOfReview(reviewID).stream().map(Comment::toDto).collect(Collectors.toList()));
    }
    @GetMapping("/reviews/{reviewID}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#reviewID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#reviewID).getPostedIn().getId())")
    public ResponseEntity<CommentDTO> getReviewByID(@PathVariable Long reviewID){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getReviewByID(reviewID).toDto());
    }
    @GetMapping("/responses/{responseID}")
    @PreAuthorize("@documentService.checkOwner(authentication.principal.getUsername(), @commentService.getCommentByID(#responseID).getPostedIn().getId()) || @documentService.checkPublic(@commentService.getCommentByID(#responseID).getPostedIn().getId())")
    public ResponseEntity<CommentDTO> getResponseByID(@PathVariable Long responseID){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getResponseByID(responseID).toDto());
    }

}
