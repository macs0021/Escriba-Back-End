package com.marco.appEscritura.controller;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import com.marco.appEscritura.dto.UserDTO;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.exceptions.Document.NotExistingDocument;
import com.marco.appEscritura.exceptions.User.AlreadyExistingUser;
import com.marco.appEscritura.exceptions.User.NotExistingUser;
import com.marco.appEscritura.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerRestrictions(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NotExistingUser.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> NotExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }
    @Operation(summary = "Obtener usuarios", description = "Retorna una lista con todos los usuarios.")
    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @Operation(summary = "Obtener usuario por nombre de usuario", description = "Devuelve los datos del usuario especificado por el nombre de usuario.")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getByUsername(username).toDto());
    }

    @Operation(summary = "Verificar existencia de usuario", description = "Verifica si un usuario con un nombre de usuario específico existe.")
    @GetMapping("/exists/{username}")
    public boolean existsUsername(@PathVariable String username){
        return userService.checkExistence(username);
    }

    @Operation(summary = "Obtener usuarios por fragmento de nombre de usuario", description = "Devuelve una lista de usuarios cuyo nombre de usuario contiene el fragmento proporcionado.")
    @GetMapping("/contains/{usernameFragment}")
    public  ResponseEntity<List<UserDTO>> getByContains(@PathVariable String usernameFragment){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByFragment(usernameFragment).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }
    
    @Operation(summary = "Obtener recomendaciones para un usuario", description = "Devuelve una lista de recomendaciones para el usuario especificado por su nombre de usuario.")
    @GetMapping("/recommendations/{username}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public  ResponseEntity<List<UserDTO>> getRecommendationsFor(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRecommendationFor(username).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario específico.")
    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.username == @userService.getById(#id)?.getUsername()")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO){
        userService.updateUser(id,userDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Actualizar seguidores", description = "Añade o elimina a un seguidor del usuario especificado.")
    @PatchMapping("/{username}/followers/{follower}")
    @PreAuthorize("authentication.principal.getUsername() == #username")
    public ResponseEntity<Void> updateFollowers(@PathVariable String username, @PathVariable String follower){
        userService.updateFollowers(username,follower);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Obtener seguidos", description = "Devuelve una lista de usuarios que el usuario especificado está siguiendo.")
    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowingOf(username).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener seguidores", description = "Devuelve una lista de usuarios que están siguiendo al usuario especificado.")
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getFollowersOf(username).stream()
                .map(user -> user.toDto())
                .collect(Collectors.toList()));
    }


}
