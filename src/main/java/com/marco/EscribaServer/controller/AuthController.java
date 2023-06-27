package com.marco.EscribaServer.controller;

import com.marco.EscribaServer.entity.User;
import com.marco.EscribaServer.dto.JwtDto;
import com.marco.EscribaServer.dto.LoginDto;
import com.marco.EscribaServer.dto.RegisterDto;
import com.marco.EscribaServer.exceptions.User.AlreadyExistingUser;
import com.marco.EscribaServer.security.token.JwtProvider;
import com.marco.EscribaServer.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(err-> messages.add(err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(messages.stream().collect(Collectors.joining(";")));
    }

    @ExceptionHandler({AlreadyExistingUser.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> AlreadyExistingExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Operation(summary = "Registrar usuario", description = "Registra a un usuario en la aplicación")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto newUser){

        User user = new User(newUser.getUsername(),
                        newUser.getPassword(),newUser.getEmail());
        user.setImage(newUser.getProfileImage());

        userService.save(user);
        return new ResponseEntity("Registration was successful",HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Inicia la sesión de un usuario en la aplicación")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loggedUser){

        try {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loggedUser.getUsername(),
                loggedUser.getPassword(), Collections.emptyList()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);
        return new ResponseEntity(jwtDto, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid password or username", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Refresco de token", description = "Refresca un token válido que haya cadudado")
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }
}