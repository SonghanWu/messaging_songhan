package com.songhanwu.messaging.service;

import org.springframework.stereotype.Service;

@Service
public class GroupChatService {

    public void verifyUserIsAMember(int userId, int groupChatId) {
        // select * from group_chat_member where user_id = #{userId} and group_chat_id = #{groupChatId}
        //throw new Exception();
    }
}
