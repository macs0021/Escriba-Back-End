package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

}
