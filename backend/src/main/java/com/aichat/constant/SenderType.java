package com.aichat.constant;

public enum SenderType {
    AI("1","AI"),
    USER("2","USER");

    // 2. Define private fields to store the data
    private final String code;
    private final String description;

    SenderType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
