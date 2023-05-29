package com.marco.appEscritura.exceptions.Document;

public class AlreadyExistingDocument extends RuntimeException{
    public AlreadyExistingDocument(String message) {
        super(message);
    }
}
