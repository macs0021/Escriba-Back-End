package com.marco.appEscritura.service;

import com.marco.appEscritura.dto.UserDTO;
import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public User getByUsername(String username) {

        Optional<User> user = userRepository.findOneByUsername(username);
        if (!user.isPresent()) {
            //excepcion
        }

        return user.get();
    }

    public void updateUser(String username, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findOneByUsername(username);

        if (!userOptional.isPresent()) {
            //excepcion
        }

        User user = userOptional.get();

        if (userDTO.getName() != null)
            user.setUsername(userDTO.getName());
        if (userDTO.getImage() != null)
            user.setImage(userDTO.getImage());
        if (userDTO.getDescription() != null)
            user.setDescription(userDTO.getDescription());
        if (userDTO.getEmail() != null)
            user.setEmail(userDTO.getEmail());

        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List) userRepository.findAll();
    }

    public void updateFollowers(String username, String follower) {
        Optional<User> followingUserOptional = userRepository.findOneByUsername(username);
        Optional<User> followerUserOptional = userRepository.findOneByUsername(follower);

        if (!followingUserOptional.isPresent() || !followerUserOptional.isPresent()) {
            //excepcion
        }

        User followingUser = followingUserOptional.get();
        User followerUser = followerUserOptional.get();

        if (!followingUser.getFollowers().contains(followerUser)) {
            followingUser.getFollowers().add(followerUser);
            followerUser.getFollowing().add(followingUser);
            System.out.println("siguiendo...");
        } else {
            followingUser.getFollowers().remove(followerUser);
            followerUser.getFollowing().remove(followingUser);
            System.out.println("dejando de seguir...");
        }

        userRepository.save(followerUser);
        userRepository.save(followingUser);
    }

}
