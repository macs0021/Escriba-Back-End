package com.marco.appEscritura.security.controller;

import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.security.dto.JwtDto;
import com.marco.appEscritura.security.dto.LoginDto;
import com.marco.appEscritura.security.dto.RegisterDto;
import com.marco.appEscritura.security.token.JwtProvider;
import com.marco.appEscritura.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto newUser){

        if(userService.existsByUsername(newUser.getUsername()))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        //if(userService.existsByEmail(newUser.getEmail()))
            //return new ResponseEntity(HttpStatus.BAD_REQUEST);

        User user = new User(newUser.getUsername(),
                        newUser.getPassword(),newUser.getEmail());

        userService.save(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

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
            return new ResponseEntity<>("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }
}