package com.majiang.community.enums;

public enum NotificationEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论");
    private int status;
    private String name;

    NotificationEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }
}
