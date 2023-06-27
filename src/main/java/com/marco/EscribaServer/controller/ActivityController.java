package com.marco.EscribaServer.controller;

import com.marco.EscribaServer.entity.ActivityEvent;
import com.marco.EscribaServer.service.ActivityService;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@CrossOrigin(origins = "http://localhost:3000")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Operation(summary = "Actividad reciente", description = "Devuelve la información de las actividades que han realizado los usuarios adjuntos a la petición")
    @GetMapping("/recent")
    public ResponseEntity<List<ActivityEvent>> getRecentActivity(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam List<String> usernames) {
        List<ActivityEvent> activity = activityService.getRecentActivity(pageSize, pageNumber, usernames);
        return ResponseEntity.ok(activity);
    }

}