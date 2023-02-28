package com.marco.appEscritura.service;

import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.exceptions.NotExistingDocument;
import com.marco.appEscritura.repository.DocumentRepository;
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

    public Document getDocument(Long id){
        Optional<Document> documentOptional = documentRepository.findById(id);
        if(!documentOptional.isPresent()){
           throw new NotExistingDocument("The document " + documentOptional.get().getName() + " does not exist");
        }
        return documentOptional.get();
    }
    public Iterable<Document> getAllDocuments(){
        Iterable<Document> documents = documentRepository.findAll();
        return documents;
    }
    public Long createDocument(Document document){
        return documentRepository.save(document).getId();
    }
    public Long updateDocument(Document document){
        return documentRepository.save(document).getId();
    }
}
