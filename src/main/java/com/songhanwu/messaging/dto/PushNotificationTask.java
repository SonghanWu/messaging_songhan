package com.songhanwu.messaging.dto;

import lombok.Data;

@Data
public class PushNotificationTask {
    private Integer userIdToNotify;
    private String content;
}
