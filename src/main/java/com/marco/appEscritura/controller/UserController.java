package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.dto.UserDTO;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public UserDTO getUserByUsername(@PathVariable String username){
        return userService.getByUsername(username).toDto();
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO){
        userService.updateUser(username,userDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PatchMapping("/{username}/followers/{follower}")
    public ResponseEntity<Void> updateFollowers(@PathVariable String username, @PathVariable String follower){
        userService.updateFollowers(username,follower);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
