package com.marco.appEscritura.controller;

import com.marco.appEscritura.entity.ActivityEvent;
import com.marco.appEscritura.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.AssertTrue;
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