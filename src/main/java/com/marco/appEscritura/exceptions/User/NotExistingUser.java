package com.marco.appEscritura.exceptions.User;

public class NotExistingUser extends RuntimeException{
    public NotExistingUser(String message) {
        super(message);
    }
}
