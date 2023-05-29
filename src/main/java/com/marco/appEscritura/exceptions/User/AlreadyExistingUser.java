package com.marco.appEscritura.exceptions.User;

public class AlreadyExistingUser extends RuntimeException{
    public AlreadyExistingUser(String message) {
        super(message);
    }
}
