package com.songhanwu.messaging.dto;

import lombok.Data;

@Data
public class GroupChatMemberDTO {

    private int id;
    private int userId; // index
    private int groupChatId; // index
}

// select * from group_chat_member where user_id = xxx;
// select * from group_chat_member where group_chat_id = xxx;
