package com.marco.appEscritura.exceptions.Genre;

public class NotExistingGenre extends RuntimeException{
    public NotExistingGenre(String message) {
        super(message);
    }
}
