package com.majiang.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private String notifierName;
    private Long outId;
    private String outTitle;
    private Long gmtCreate;
    private Integer status;
    private Integer type;
    private String typeName;
}
