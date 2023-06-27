package com.marco.EscribaServer.exceptions.Genre;

public class NotExistingGenre extends RuntimeException{
    public NotExistingGenre(String message) {
        super(message);
    }
}
