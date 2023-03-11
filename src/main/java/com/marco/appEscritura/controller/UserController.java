package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public void registerUser(@RequestBody User user){
        userService.save(user);
    }
    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

}
