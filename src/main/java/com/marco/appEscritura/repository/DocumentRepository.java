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
    @Query("SELECT DISTINCT d FROM Document d JOIN d.genres g WHERE d.isPublic = true AND (:genresSize = 0 OR g IN (:genres)) AND (:tittleFragment = '' OR d.tittle LIKE %:tittleFragment%) ORDER BY d.rating DESC, d.tittle ASC LIMIT :pageSize OFFSET :offset")
    List<Document> findAllByGenresAndTittleFragment(List<String> genres, String tittleFragment, int pageSize, int offset, @Param("genresSize") int genresSize);
    @Query("SELECT DISTINCT d FROM Document d WHERE d.isPublic = false ORDER BY d.rating DESC, d.tittle ASC")
    List<Document> findAllOrderByRatingAndTitle();

    @Query(value = "SELECT * FROM Document ORDER BY rating, tittle LIMIT ?1 OFFSET ?2",
            nativeQuery = true)
    Iterable<Document> getPageDocuments(int pageSize, int offset);

}
