package com.marco.EscribaServer.exceptions.Comment;

public class AlreadyExistingReview extends RuntimeException{
    public AlreadyExistingReview(String message) {
        super(message);
    }
}
