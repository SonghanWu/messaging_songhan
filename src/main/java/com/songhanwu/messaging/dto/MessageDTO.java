package com.songhanwu.messaging.dto;

import java.util.Date;

import com.songhanwu.messaging.enumeration.MessageType;
import lombok.Data;

@Data
public class MessageDTO {
    private int id;
    private int senderUserId;
    private Integer receiverUserId; // null
    private Integer groupChatId; // null
    private MessageType messageType;
    private String content;
    private Date sendTime;
}
