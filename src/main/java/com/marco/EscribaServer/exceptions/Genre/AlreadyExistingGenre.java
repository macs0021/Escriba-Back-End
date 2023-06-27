package com.marco.EscribaServer.exceptions.Genre;

public class AlreadyExistingGenre extends RuntimeException {
    public AlreadyExistingGenre(String message) {
        super(message);
    }
}
