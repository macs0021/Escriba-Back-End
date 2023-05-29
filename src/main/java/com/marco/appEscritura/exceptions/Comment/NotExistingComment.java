package com.marco.appEscritura.exceptions.Comment;

public class NotExistingComment extends RuntimeException {
    public NotExistingComment(String message) {
        super(message);
    }
}
