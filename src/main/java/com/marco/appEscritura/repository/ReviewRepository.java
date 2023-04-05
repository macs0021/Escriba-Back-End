package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Comment;
import com.marco.appEscritura.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ReviewRepository extends CrudRepository<Review, Long> {


}
