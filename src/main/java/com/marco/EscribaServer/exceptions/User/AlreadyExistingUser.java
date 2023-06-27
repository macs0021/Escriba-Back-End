package com.marco.EscribaServer.exceptions.User;

public class AlreadyExistingUser extends RuntimeException{
    public AlreadyExistingUser(String message) {
        super(message);
    }
}
