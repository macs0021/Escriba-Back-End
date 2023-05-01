package com.marco.appEscritura.Utils;
public enum EventType {
    USER_FOLLOWED("user_followed"),
    BOOK_PUBLISHED("book_published"),
    BOOK_REVIEWED("book_review"),
    REVIEW_REPLY("review_reply"),
    BOOK_READ("book_read");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}