package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.repository.ReadingRepository;
import com.marco.appEscritura.service.ReadingService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/readings")
@CrossOrigin(origins = "http://localhost:3000")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @PostMapping
    public ResponseEntity<Void> createReading(@RequestBody ReadingDTO readingDto) {
        System.out.println("CREANDO LECTURA");
        readingService.createReading(readingDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get/{documentID}/{username}")
    public ResponseEntity<Reading> getReading(@PathVariable Long documentID, @PathVariable String username) {
        Reading reading = readingService.getReading(username, documentID);
        return ResponseEntity.status(HttpStatus.OK).body(reading);

    }
}