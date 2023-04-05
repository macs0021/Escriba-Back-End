package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.*;
import com.marco.appEscritura.repository.ResponseRepository;
import com.marco.appEscritura.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;


    public Comment saveComment(CommentDTO commentDTO) {
        switch (commentDTO.getCommentType()) {
            case REVIEW:
                return reviewRepository.save(DTOtoReview(commentDTO));
            case RESPONSE:
                return responseRepository.save(DTOtoResponse(commentDTO));
            case INLINE:
                return null;
        }
        return null;
    }


    public List<Comment> getResponsesForReview(Long reviewId) {
        /*return commentRepository.findResponsesByReviewId(reviewId);*/
        return null;
    }

    public List<Comment> getReviewsOfDocument(Long Document){
        return null;
    }


    public Review DTOtoReview(CommentDTO commentDTO){
        User user = userService.getByUsername(commentDTO.getPostedBy());
        Document document = documentService.getDocument(commentDTO.getPostedIn());
        return new Review(commentDTO.getText(), user, document, commentDTO.getRating());
    }
    public Response DTOtoResponse(CommentDTO commentDTO){
        User user = userService.getByUsername(commentDTO.getPostedBy());
        Document document = documentService.getDocument(commentDTO.getPostedIn());
        Optional<Review> respondingReview = reviewRepository.findById(commentDTO.getResponding());
        if (!respondingReview.isPresent()){}
            //Excepcion

        return new Response(commentDTO.getText(), user, document, respondingReview.get());
    }
}
