package com.marco.EscribaServer.repository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ResponseRepository extends CommentRepository {

}