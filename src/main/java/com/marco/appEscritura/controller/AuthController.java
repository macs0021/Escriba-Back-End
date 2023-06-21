package com.marco.appEscritura.controller;

import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.dto.JwtDto;
import com.marco.appEscritura.dto.LoginDto;
import com.marco.appEscritura.dto.RegisterDto;
import com.marco.appEscritura.security.token.JwtProvider;
import com.marco.appEscritura.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Registrar usuario", description = "Registra a un usuario en la aplicación")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto newUser){

        if(userService.existsByUsername(newUser.getUsername()))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        User user = new User(newUser.getUsername(),
                        newUser.getPassword(),newUser.getEmail());
        user.setImage(newUser.getProfileImage());

        userService.save(user);
        return new ResponseEntity(HttpStatus.CREATED);
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
            return new ResponseEntity<>("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
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