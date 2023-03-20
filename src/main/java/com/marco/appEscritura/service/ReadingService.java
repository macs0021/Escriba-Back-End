package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.ReadingID;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.ReadingRepository;
import com.marco.appEscritura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReadingService {

    @Autowired
    ReadingRepository readingRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    public void createReading(ReadingDTO readingDTO) {

        User user = userRepository.findOneByUsername(readingDTO.getUsername()).get();
        Document document = documentRepository.findById(readingDTO.getDocument()).get();

        ReadingID readingID = new ReadingID(user, document);
        Optional<Reading> reading = readingRepository.findById(readingID);

        if (reading.isPresent()) {
        }

        user.getReading().add(DtoToReading(readingDTO));
        document.getBeingRead().add(DtoToReading(readingDTO));
        userRepository.save(user);
        documentRepository.save(document);
        readingRepository.save(DtoToReading(readingDTO));
    }

    public Reading getReading(String username, long documentID) {
        User user = userRepository.findOneByUsername(username).get();
        Document document = documentRepository.findById(documentID).get();

        ReadingID readingID = new ReadingID(user, document);

        Optional<Reading> reading = readingRepository.findById(readingID);

        if (!reading.isPresent()) {
        }
        return reading.get();

    }

    public Reading DtoToReading(ReadingDTO readingDTO) {
        User user = userRepository.findOneByUsername(readingDTO.getUsername()).get();
        Document document = documentRepository.findById(readingDTO.getDocument()).get();
        return new Reading(user, document, readingDTO.getReadingSpot());


    }

}
