package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Document;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DocumentRepository extends CrudRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.creator.username = ?1")
    Optional<List<Document>> findByCreatorUsername(String username);
    @Query("SELECT DISTINCT d FROM Document d JOIN d.genres g WHERE g IN (?1) AND d.isPublic = true")
    Iterable<Document> findAllByGenres(List<String> genres);

}
