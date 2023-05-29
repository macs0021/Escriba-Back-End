package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.ReadingID;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.Document.NotExistingDocument;
import com.marco.appEscritura.exceptions.Reading.AlreadyExistingReading;
import com.marco.appEscritura.exceptions.Reading.NotExistingReading;
import com.marco.appEscritura.exceptions.User.NotExistingUser;
import com.marco.appEscritura.repository.DocumentRepository;
import com.marco.appEscritura.repository.ReadingRepository;
import com.marco.appEscritura.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReadingService {

    @Autowired
    ReadingRepository readingRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityService activityService;

    public void createReading(ReadingDTO readingDTO) {

        Optional<User> userOptional = userRepository.findOneByUsername(readingDTO.getUsername());
        Optional<Document> documentOptional = documentRepository.findById(readingDTO.getDocument());

        if(!documentOptional.isPresent()) throw new NotExistingDocument("Document with id: " + readingDTO.getDocument() + " does not exist");
        if(!userOptional.isPresent()) throw new NotExistingUser("User with username: " + readingDTO.getUsername() + " does not exist");

        User user = userOptional.get();
        Document document = documentOptional.get();

        ReadingID readingID = new ReadingID(user, document);
        Optional<Reading> reading = readingRepository.findById(readingID);

        if (reading.isPresent()) {
            throw new AlreadyExistingReading("Reading of document with tittle: "
                    + document.getTittle() + " and being read by " + user.getUsername() + " does already exist");
        }

        activityService.startedReadingEvent(user.getUsername(), document.getId());
        readingRepository.save(DtoToReading(readingDTO));

        user.getReading().add(DtoToReading(readingDTO));
        document.getBeingRead().add(DtoToReading(readingDTO));

        userRepository.save(user);
        documentRepository.save(document);
    }

    public boolean checkReading(String username, long documentID){
        Optional<User> userOptional = userRepository.findOneByUsername(username);
        Optional<Document> documentOptional = documentRepository.findById(documentID);

        if(!documentOptional.isPresent()) throw new NotExistingDocument("Document with id: " + documentID + " does not exist");
        if(!userOptional.isPresent()) throw new NotExistingUser("User with username: " + username + " does not exist");

        User user = userOptional.get();
        Document document = documentOptional.get();

        ReadingID readingID = new ReadingID(user, document);
        return readingRepository.findById(readingID).isPresent();
    }

    public Reading getReading(String username, long documentID) {

        Optional<User> userOptional = userRepository.findOneByUsername(username);
        Optional<Document> documentOptional = documentRepository.findById(documentID);

        if(!documentOptional.isPresent()) throw new NotExistingDocument("Document with id: " + documentID + " does not exist");
        if(!userOptional.isPresent()) throw new NotExistingUser("User with username: " + username + " does not exist");

        User user = userOptional.get();
        Document document = documentOptional.get();

        ReadingID readingID = new ReadingID(user, document);

        Optional<Reading> reading = readingRepository.findById(readingID);

        if (!reading.isPresent()) {
            throw new NotExistingReading("Reading of document with tittle: "
                    + document.getTittle() + " and being read by " + user.getUsername() + " does not exist");
        }
        return reading.get();

    }

    public void modifyReading(ReadingDTO readingDTO){

        Optional<User> userOptional = userRepository.findOneByUsername(readingDTO.getUsername());
        Optional<Document> documentOptional = documentRepository.findById(readingDTO.getDocument());

        if(!documentOptional.isPresent()) throw new NotExistingDocument("Document with id: " + readingDTO.getDocument() + " does not exist");
        if(!userOptional.isPresent()) throw new NotExistingUser("User with username: " + readingDTO.getUsername() + " does not exist");

        User user = userOptional.get();
        Document document = documentOptional.get();

        ReadingID readingID = new ReadingID(user, document);

        Optional<Reading> reading = readingRepository.findById(readingID);

        if (!reading.isPresent()) {
            throw new NotExistingReading("Reading of document with tittle: "
                    + document.getTittle() + " and being read by " + user.getUsername() + " does not exist");
        }


        readingRepository.save(DtoToReading(readingDTO));
    }

    public Reading DtoToReading(ReadingDTO readingDTO) {
        User user = userRepository.findOneByUsername(readingDTO.getUsername()).get();
        Document document = documentRepository.findById(readingDTO.getDocument()).get();
        return new Reading(user, document, readingDTO.getReadingSpot());
    }

}
