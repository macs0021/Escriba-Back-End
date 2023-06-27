package com.marco.EscribaServer.repository;

import com.marco.EscribaServer.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
}
