package com.marco.EscribaServer.repository;

import com.marco.EscribaServer.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ReviewRepository extends CommentRepository {
    @Query("SELECT r FROM Review r WHERE r.postedIn.id = :documentId AND r.postedBy.username = :username")
    Optional<Review> findReviewByPostedIn_IdAndPostedBy_Username(Long documentId, String username);
}
