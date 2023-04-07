package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
}
