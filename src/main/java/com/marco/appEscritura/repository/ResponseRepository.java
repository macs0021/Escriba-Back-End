package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Comment;
import com.marco.appEscritura.entity.Response;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ResponseRepository extends CrudRepository<Response, Long> {
    /*@Query("SELECT c FROM Comment c WHERE c INSTANCE OF Response AND c.review IS NOT NULL AND c.review.id = :reviewId")
    List<Comment> findResponsesByReviewId(@Param("reviewId") Long reviewId);*/

}