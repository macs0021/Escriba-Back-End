package com.marco.appEscritura.controller;

import com.marco.appEscritura.entity.ActivityEvent;
import com.marco.appEscritura.service.ActivityService;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@CrossOrigin(origins = "http://localhost:3000")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/recent")
    public ResponseEntity<List<ActivityEvent>> getRecentActivity(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam List<String> usernames) {
        List<ActivityEvent> activity = activityService.getRecentActivity(pageSize, pageNumber, usernames);
        return ResponseEntity.ok(activity);
    }

}