package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.Comment;
import com.marco.appEscritura.service.CommentService;
import com.marco.appEscritura.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public CommentDTO saveComment(@RequestBody CommentDTO commentDTO) {
        System.out.println("Recibiendo comentario: " + commentDTO.getText());
        return commentService.saveComment(commentDTO).toDto();
    }
    @GetMapping("/{document}/review")
    public List<CommentDTO> getReviewsFromDocument(@PathVariable Long document){
        return commentService.getReviewsOfDocument(document).stream().map(Comment::toDto).collect(Collectors.toList());
    }




}
