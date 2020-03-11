package com.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTOIN_NOT_FOUND("你访问的问题不在了，要不换个试试？");

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
