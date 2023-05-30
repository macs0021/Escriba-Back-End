package com.marco.appEscritura.service;

import com.marco.appEscritura.Utils.CommentType;
import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.*;
import com.marco.appEscritura.exceptions.Comment.AlreadyExistingReview;
import com.marco.appEscritura.exceptions.Comment.NotExistingComment;
import com.marco.appEscritura.repository.CommentRepository;
import com.marco.appEscritura.repository.ResponseRepository;
import com.marco.appEscritura.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserService userService;

    @Autowired
    DocumentService documentService;

    @Autowired
    ActivityService activityService;


    public Comment saveComment(CommentDTO commentDTO) {
        switch (commentDTO.getCommentType()) {
            case REVIEW:
                Optional<Review> reviewOptional =
                        reviewRepository.findReviewByPostedIn_IdAndPostedBy_Username(commentDTO.getPostedIn(), commentDTO.getPostedBy());

                if (reviewOptional.isPresent()) {
                    throw new AlreadyExistingReview("Review by " + commentDTO.getPostedBy() + " in " + commentDTO.getPostedIn() + " does already exist");
                }

                documentService.addRating(commentDTO.getRating(), commentDTO.getPostedIn());
                Review review = reviewRepository.save(DTOtoReview(commentDTO));
                activityService.createdReviewEvent(review.getPostedBy().getUsername(), review.getId());
                return review;

            case RESPONSE:
                Optional<Comment> optionalReview = reviewRepository.findById(commentDTO.getResponding());
                if (!optionalReview.isPresent()) {
                    throw new NotExistingComment("Review with id: " + commentDTO.getResponding() + " does not exist");
                }
                Review reviewResponse = (Review) optionalReview.get();

                Response response = DTOtoResponse(commentDTO);

                response.setReview(reviewResponse);

                Response finalResponse = responseRepository.save(response);

                reviewResponse.getResponses().add(finalResponse);
                reviewRepository.save(reviewResponse);

                activityService.replyToReviewEvent(finalResponse.getPostedBy().getUsername(), finalResponse.getId());

                return finalResponse;
            case INLINE:
                return null;
        }
        return null;
    }

    public Comment getCommentByID(Long commentID) {
        Optional<Comment> commentOptional = commentRepository.findById(commentID);

        if (!commentOptional.isPresent()) {
            throw new NotExistingComment("Comment with id: " + commentID + " does not exist");
        }

        return commentOptional.get();
    }

    public Review getReviewByID(Long reviewID) {
        Optional<Comment> reviewOptional = reviewRepository.findById(reviewID);

        if (!reviewOptional.isPresent()) {
            throw new NotExistingComment("Review with id: " + reviewID + " does not exist");
        }

        return (Review) reviewOptional.get();
    }

    public Response getResponseByID(Long responseID) {
        Optional<Comment> responseOptional = responseRepository.findById(responseID);

        if (!responseOptional.isPresent()) {
            throw new NotExistingComment("Reply with id: " + responseID + " does not exist");
        }
        return (Response) responseOptional.get();
    }

    public List<Response> getResponsesOfReview(Long reviewId) {
        Optional<Comment> optionalReview = reviewRepository.findById(reviewId);

        if (!optionalReview.isPresent()) {
            throw new NotExistingComment("Review with id: " + reviewId + " does not exist");
        }

        Review review = (Review) optionalReview.get();
        return review.getResponses();
    }

    public List<Review> getReviewsOfDocument(Long documentId) {
        Document document = documentService.getDocument(documentId);
        return document.getReviews();
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent())
            throw new NotExistingComment("Comment with id: " + commentId + " does not exist");
        commentRepository.deleteById(commentId);
    }

    public Long updateComment(Long commentId, CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (!commentOptional.isPresent())
            throw new NotExistingComment("Comment with id: " + commentId + " does not exist");

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

    public Review DTOtoReview(CommentDTO commentDTO) {
        User user = userService.getByUsername(commentDTO.getPostedBy());
        Document document = documentService.getDocument(commentDTO.getPostedIn());
        return new Review(commentDTO.getText(), user, document, commentDTO.getRating());
    }

    public Response DTOtoResponse(CommentDTO commentDTO) {
        User user = userService.getByUsername(commentDTO.getPostedBy());
        Document document = documentService.getDocument(commentDTO.getPostedIn());
        Optional<Comment> respondingReview = reviewRepository.findById(commentDTO.getResponding());
        if (!respondingReview.isPresent() || !(respondingReview.get() instanceof Review)) {
        }

        return new Response(commentDTO.getText(), user, document, (Review) respondingReview.get());
    }

}
