package com.songhanwu.messaging.request;

import com.songhanwu.messaging.enumeration.MessageType;
import lombok.Data;

@Data
public class SendMessageRequest {
    private Integer groupChatId;
    private Integer receiverUserId; // null
    private String content;
    private MessageType messageType;
}
