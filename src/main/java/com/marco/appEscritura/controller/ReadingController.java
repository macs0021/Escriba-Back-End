package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.repository.ReadingRepository;
import com.marco.appEscritura.service.ReadingService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/readings")
@CrossOrigin(origins = "http://localhost:3000")
public class ReadingController {

    @Autowired
    ReadingService readingService;

    @PostMapping
    @PreAuthorize("authentication.principal.getUsername() == #readingDto.getUsername()")
    public ResponseEntity<Void> createReading(@RequestBody ReadingDTO readingDto) {
        readingService.createReading(readingDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get/{username}/{documentID}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<ReadingDTO> getReading(@PathVariable String username, @PathVariable Long documentID) {
        Reading reading = readingService.getReading(username, documentID);
        return ResponseEntity.status(HttpStatus.OK).body(reading.toDto());
    }

    @GetMapping("/check/{username}/{documentID}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Boolean> checkReading(@PathVariable String username, @PathVariable Long documentID) {
        return ResponseEntity.status(HttpStatus.OK).body(readingService.checkReading(username,documentID));
    }

    @PutMapping
    @PreAuthorize("authentication.principal.getUsername() == #readingDto.getUsername()")
    public ResponseEntity<Void> modifyReading(@RequestBody ReadingDTO readingDto) {
        readingService.modifyReading(readingDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}