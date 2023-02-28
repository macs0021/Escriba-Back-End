package com.marco.appEscritura.service;

import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> getById(UUID id){
        return userRepository.findById(id);
    }
    public Optional<User> getByUsername(String username){ return userRepository.findOneByUsername(username);}

    public boolean existsByUsername(String username){ return userRepository.existsByUsername(username);}

    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
