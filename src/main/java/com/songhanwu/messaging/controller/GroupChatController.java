package com.songhanwu.messaging.controller;

import java.util.List;

import com.songhanwu.messaging.annotation.NeedAuth;
import com.songhanwu.messaging.dto.GroupChatDTO;
import com.songhanwu.messaging.request.CreateGroupChatRequest;
import com.songhanwu.messaging.response.UserResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupchats")
public class GroupChatController {

    @PostMapping("/create")
    @NeedAuth
    public void createGroupChat(@CookieValue("login_token") String userLoginToken,
                                @RequestBody CreateGroupChatRequest createGroupChatRequest) {
        // verify userIds are my friends
        // save 1 record to group_chat
        // save N records to group_chat_members
    }

    @GetMapping("/listMyGroupChats")
    @NeedAuth

    public List<GroupChatDTO> listMyGroupChats(@CookieValue("login_token") String userLoginToken,
                                               @RequestParam int page) {
        return null;
    }

    @GetMapping("/listGroupChatMembers")
    @NeedAuth

    public List<UserResponse> listGroupChatMembers(@CookieValue("login_token") String userLoginToken,
                                                   @RequestParam int groupChatId,
                                                   @RequestParam int page) {
        // verify user is a member of the group chat
        return null;
    }

    @PostMapping("addMembers")
    @NeedAuth

    public void addMembers() { // List of userIds, group chat id {
    }
}

