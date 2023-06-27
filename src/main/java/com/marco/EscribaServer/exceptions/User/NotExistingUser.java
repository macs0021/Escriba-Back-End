package com.marco.EscribaServer.exceptions.User;

public class NotExistingUser extends RuntimeException{
    public NotExistingUser(String message) {
        super(message);
    }
}
