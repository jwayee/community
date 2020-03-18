package com.majiang.community.model;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long notifier;
    private Long receiver;
    private Long outId;
    private int type;
    private Long gmt_create;
    private int status;

}
