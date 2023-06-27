package com.marco.EscribaServer.exceptions.Activity;

public class NotExistingActivity extends RuntimeException{
    public NotExistingActivity(String message) {
        super(message);
    }
}