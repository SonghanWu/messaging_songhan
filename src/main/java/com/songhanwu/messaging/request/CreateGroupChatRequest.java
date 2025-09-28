package com.songhanwu.messaging.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateGroupChatRequest {
    List<Integer> userIds;
    String title;
    String description;
}
