package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByGenre(String genre);

    @Query("SELECT DISTINCT d FROM Document d JOIN d.genres g WHERE d.isPublic = true AND g.genre IN ?1 AND (?2 = '' OR d.tittle LIKE %?2%)")
    List<Document> findPublicDocumentsByGenreNamesAndTittleFragment(List<String> genreNames, String tittleFragment);
}
