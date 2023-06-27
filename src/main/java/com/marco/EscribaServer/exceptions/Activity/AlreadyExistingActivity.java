package com.marco.EscribaServer.exceptions.Activity;

public class AlreadyExistingActivity extends RuntimeException{
    public AlreadyExistingActivity(String message) {
        super(message);
    }
}