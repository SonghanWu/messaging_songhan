package com.songhanwu.messaging.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GroupChatDTO { // metadata

    private int id;
    private String title;
    private String description;
    private Date createTime;
    private int creatorUserId;
}
