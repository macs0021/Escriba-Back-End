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
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/contains/{usernameFragment}")
    public  ResponseEntity<List<UserDTO>> getByContains(@PathVariable String usernameFragment){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByFragment(usernameFragment).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }

    @GetMapping("/recommendations/{username}")
    public  ResponseEntity<List<UserDTO>> getRecommendationsFor(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRecommendationFor(username).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO){
        userService.updateUser(id,userDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PatchMapping("/{username}/followers/{follower}")
    public ResponseEntity<Void> updateFollowers(@PathVariable String username, @PathVariable String follower){
        userService.updateFollowers(username,follower);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowingOf(username).stream()
                .map(user -> user.toDto())  
                .collect(Collectors.toList()));
    }
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowersOf(username).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }

}
