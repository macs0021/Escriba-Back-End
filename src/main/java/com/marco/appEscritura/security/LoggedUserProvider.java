package com.marco.appEscritura.security;

import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggedUserProvider {

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.getByUsername(loggedInUsername);
        return loggedInUser;
    }
}