package com.marco.EscribaServer.exceptions.Comment;

public class NotExistingComment extends RuntimeException {
    public NotExistingComment(String message) {
        super(message);
    }
}
