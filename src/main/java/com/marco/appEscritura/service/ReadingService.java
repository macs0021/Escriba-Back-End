package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.ReadingID;
import com.marco.appEscritura.entity.User;
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
    private ActivityService activityService;

    public void createReading(ReadingDTO readingDTO) {

        User user = userRepository.findOneByUsername(readingDTO.getUsername()).get();
        Document document = documentRepository.findById(readingDTO.getDocument()).get();

        ReadingID readingID = new ReadingID(user, document);
        Optional<Reading> reading = readingRepository.findById(readingID);

        if (reading.isPresent()) {
        }

        activityService.startedReadingEvent(user.getUsername(), document.getId());
        readingRepository.save(DtoToReading(readingDTO));
        user.getReading().add(DtoToReading(readingDTO));
        document.getBeingRead().add(DtoToReading(readingDTO));
        userRepository.save(user);
        documentRepository.save(document);
    }

    public Reading getReading(String username, long documentID) {
        User user = userRepository.findOneByUsername(username).get();
        Document document = documentRepository.findById(documentID).get();

        ReadingID readingID = new ReadingID(user, document);

        Optional<Reading> reading = readingRepository.findById(readingID);

        if (!reading.isPresent()) {
            return null;
        }
        return reading.get();

    }

    public void modifyReading(ReadingDTO readingDTO){
        readingRepository.save(DtoToReading(readingDTO));
    }

    public Reading DtoToReading(ReadingDTO readingDTO) {
        User user = userRepository.findOneByUsername(readingDTO.getUsername()).get();
        Document document = documentRepository.findById(readingDTO.getDocument()).get();
        return new Reading(user, document, readingDTO.getReadingSpot());
    }

}
