package com.songhanwu.messaging.controller;

import java.util.List;
import java.util.ArrayList;

import com.songhanwu.messaging.dto.FriendInvitationDTO;
import com.songhanwu.messaging.request.SendFriendInvitationRequest;
import com.songhanwu.messaging.service.FriendService;
import com.songhanwu.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired private UserService userService;
    @Autowired private FriendService friendService;

    @PostMapping("/sendInvitation")
    public void sendInvitation(@CookieValue("login_token") String senderLoginToken,
                               @RequestBody SendFriendInvitationRequest sendFriendInvitationRequest) throws Exception {

        this.friendService.saveFriendInvitation(senderLoginToken,
                                                sendFriendInvitationRequest.getReceiverUserId(),
                                                sendFriendInvitationRequest.getMessage());

    }

    @GetMapping("/listPendingInvitations")
    public List<FriendInvitationDTO> listPendingInvitations(@CookieValue("login_token") String loginToken) throws Exception {
        // TODO: 实现获取待处理邀请的功能
        return new ArrayList<>();
    }

    @PostMapping("/accept")
    public void accept(@CookieValue("login_token") String receiverLoginToken,
                       @RequestParam int friendInvitationId) throws Exception {
        this.friendService.accept(receiverLoginToken, friendInvitationId);

    }

    @PostMapping("/decline")
    public void decline(@CookieValue("login_token") String receiverLoginToken,
                       @RequestParam int friendInvitationId) throws Exception {
        this.friendService.decline(receiverLoginToken, friendInvitationId);

    }
}
