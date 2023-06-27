package com.marco.EscribaServer.Utils;

public enum EntityType {
    USER("user"),
    DOCUMENT("document"),
    REVIEW("review"),
    REPLY("reply");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
