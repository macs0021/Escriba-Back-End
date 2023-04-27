package com.marco.appEscritura.service;

import com.marco.appEscritura.Utils.CommentType;
import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.*;
import com.marco.appEscritura.repository.CommentRepository;
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
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;


    public Comment saveComment(CommentDTO commentDTO) {
        switch (commentDTO.getCommentType()) {
            case REVIEW:
                documentService.addRating(commentDTO.getRating(), commentDTO.getPostedIn());
                return reviewRepository.save(DTOtoReview(commentDTO));
            case RESPONSE:
                System.out.println("Intentando guardar respuesta " + commentDTO.toString());

                Optional<Comment> optionalReview = reviewRepository.findById(commentDTO.getResponding());
                if(!optionalReview.isPresent()){

                }
                Review review = (Review) optionalReview.get();
                Response response = DTOtoResponse(commentDTO);

                response.setReview(review);

                reviewRepository.save(review);

                return responseRepository.save(response);
            case INLINE:
                return null;
        }
        return null;
    }

    public List<Response> getResponsesOfReview(Long reviewId) {
        Optional<Comment> optionalReview = reviewRepository.findById(reviewId);

        if(!optionalReview.isPresent()){

        }

        Review review = (Review) optionalReview.get();
        return review.getResponses();
    }

    public List<Review> getReviewsOfDocument(Long documentId){
        Document document = documentService.getDocument(documentId);

        return document.getReviews();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Long updateComment(Long commentId, CommentDTO commentDTO){

        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        switch (commentDTO.getCommentType()) {
            case REVIEW:


                Review review = (Review) commentOptional.get();

                documentService.updateRating(commentDTO.getRating(), review.getRating(), review.getPostedIn().getId());

                review.setRating(commentDTO.getRating());
                review.setText(commentDTO.getText());

                return reviewRepository.save(review).getId();
            case RESPONSE:
                Response response = (Response) commentOptional.get();
                response.setText(commentDTO.getText());
                return reviewRepository.save(response).getId();

            case INLINE:
                return null;
        }

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
        Optional<Comment> respondingReview = reviewRepository.findById(commentDTO.getResponding());
        if (!respondingReview.isPresent() || !(respondingReview.get() instanceof Review)){}

        return new Response(commentDTO.getText(), user, document, (Review)respondingReview.get());
    }

}
