package com.marco.EscribaServer.service;

import com.marco.EscribaServer.entity.Document;
import com.marco.EscribaServer.entity.Genre;
import com.marco.EscribaServer.exceptions.Genre.AlreadyExistingGenre;
import com.marco.EscribaServer.exceptions.Genre.NotExistingGenre;
import com.marco.EscribaServer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;

    public Genre findByGenre(String genre) {
        Optional<Genre> optionalGenre = genreRepository.findByGenre(genre);
        if(!optionalGenre.isPresent()){
            throw new NotExistingGenre("Genre " + genre + " does not exist");
        }
        return optionalGenre.get();
    }
    public Genre create(String genre) {

        Iterable<Genre> genres1 = genreRepository.findAll();
        Genre createdGenre = new Genre(genre);

        Optional<Genre> optionalGenre = genreRepository.findByGenre(genre);
        if(optionalGenre.isPresent()){
            throw new AlreadyExistingGenre("Genre " + genre + " already exist");
        }

        return genreRepository.save(createdGenre);
    }

    public Genre update(String genre) {

        Optional<Genre> optionalGenre = genreRepository.findByGenre(genre);
        if(!optionalGenre.isPresent()){
            throw new NotExistingGenre("Genre " + genre + " does not exist");
        }

        Genre newGenre = optionalGenre.get();
        newGenre.setGenre(genre);

        return genreRepository.save(newGenre);
    }

    public void addDocumentToGenre(String genre, Document document){

        Optional<Genre> optionalGenre = genreRepository.findByGenre(genre);
        if(!optionalGenre.isPresent()){
            throw new NotExistingGenre("Genre " + genre + " does not exist");
        }

        Genre newGenre = optionalGenre.get();
        newGenre.getDocumentsOfGenre().add(document);
        genreRepository.save(newGenre);

    }

    public List<Document> findDocumentsByGenreName(List<String> genreName, String tittleFragment){
        return genreRepository.findPublicDocumentsByGenreNamesAndTittleFragment(genreName, tittleFragment);
    }

    public void removeDocumentToGenre(String genre, Document document){

        Optional<Genre> optionalGenre = genreRepository.findByGenre(genre);
        if(!optionalGenre.isPresent()){
            throw new NotExistingGenre("Genre " + genre + " does not exist");
        }

        Genre newGenre = optionalGenre.get();
        newGenre.getDocumentsOfGenre().remove(document);
        genreRepository.save(newGenre);


    }
}
