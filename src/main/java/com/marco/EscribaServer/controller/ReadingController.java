package com.marco.EscribaServer.controller;

import com.marco.EscribaServer.dto.ReadingDTO;
import com.marco.EscribaServer.entity.Reading;
import com.marco.EscribaServer.exceptions.Reading.AlreadyExistingReading;
import com.marco.EscribaServer.exceptions.Reading.NotExistingReading;
import com.marco.EscribaServer.service.ReadingService;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/readings")
@CrossOrigin(origins = "http://localhost:3000")
public class ReadingController {

    @Autowired
    ReadingService readingService;


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }

    @ExceptionHandler({NotExistingReading.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> NotExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AlreadyExistingReading.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> AlreadyExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Operation(summary = "Crear lectura", description = "Crea una lectura.")
    @PostMapping
    @PreAuthorize("authentication.principal.getUsername() == #readingDto.getUsername()")
    public ResponseEntity<Void> createReading(@RequestBody ReadingDTO readingDto) {
        readingService.createReading(readingDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Obtener lectura", description = "Devuelve la lectura de un usuario en un documento.")
    @GetMapping("/get/{username}/{documentID}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<ReadingDTO> getReading(@PathVariable String username, @PathVariable Long documentID) {
        Reading reading = readingService.getReading(username, documentID);
        return ResponseEntity.status(HttpStatus.OK).body(reading.toDto());
    }

    @Operation(summary = "Comprobar existencia lectura", description = "Comprueba que un usuario está leyendo un documento.")
    @GetMapping("/check/{username}/{documentID}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Boolean> checkReading(@PathVariable String username, @PathVariable Long documentID) {
        return ResponseEntity.status(HttpStatus.OK).body(readingService.checkReading(username,documentID));
    }

    @Operation(summary = "Actualiza lectura", description = "Actualiza el valor de una lectura.")
    @PutMapping
    @PreAuthorize("authentication.principal.getUsername() == #readingDto.getUsername()")
    public ResponseEntity<Void> modifyReading(@RequestBody ReadingDTO readingDto) {
        readingService.modifyReading(readingDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}