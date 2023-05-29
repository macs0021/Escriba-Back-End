package com.marco.appEscritura.exceptions.Comment;

public class AlreadyExistingReview extends RuntimeException{
    public AlreadyExistingReview(String message) {
        super(message);
    }
}
