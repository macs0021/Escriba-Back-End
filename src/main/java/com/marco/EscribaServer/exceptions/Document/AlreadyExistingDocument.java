package com.marco.EscribaServer.exceptions.Document;

public class AlreadyExistingDocument extends RuntimeException{
    public AlreadyExistingDocument(String message) {
        super(message);
    }
}
