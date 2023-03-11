package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.NotExistingDocument;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    public Document getDocument(Long id){
        Optional<Document> documentOptional = documentRepository.findById(id);
        if(!documentOptional.isPresent()){
           throw new NotExistingDocument("The document " + documentOptional.get().getTittle() + " does not exist");
        }
        return documentOptional.get();
    }
    public Iterable<Document> getAllDocuments(){
        Iterable<Document> documents = documentRepository.findAll();
        return documents;
    }
    public Long createDocument(DocumentDTO documentDto){
        return documentRepository.save(DtoToDocument(documentDto)).getId();
    }
    public Long updateDocument(DocumentDTO documentDto){
        return documentRepository.save(DtoToDocument(documentDto)).getId();
    }

    public Iterable<Document> getDocumentsCreatedBy(String username){
        Optional<List<Document>> documents = documentRepository.findByCreatorUsername(username);
        return documents.get().stream().collect(Collectors.toList());
    }

    public void deleteDocument(long id){
        documentRepository.deleteById(id);
    }


    private Document DtoToDocument(DocumentDTO documentDto){

            System.out.println(documentDto);
            Optional<User> user = userRepository.findOneByUsername(documentDto.getCreatorUsername());
            Document document = new Document();
            document.setId(documentDto.getId());
            document.setSynopsis(documentDto.getSynopsis());
            document.setTittle(documentDto.getTittle());
            document.setPrivateText(documentDto.getPrivateText());
            document.setCover(documentDto.getCover());
            document.setCreator(user.get());
            document.setGenres(documentDto.getGenres());

            return document;

    }

}
