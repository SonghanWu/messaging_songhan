package com.songhanwu.messaging.controller;

import java.util.List;
import java.util.ArrayList;

import com.songhanwu.messaging.annotation.NeedAuth;
import com.songhanwu.messaging.request.SendFriendInvitationRequest;
import com.songhanwu.messaging.response.FriendInvitationResponse;
import com.songhanwu.messaging.response.UserResponse;
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
    @NeedAuth
    public void sendInvitation(@RequestBody SendFriendInvitationRequest sendFriendInvitationRequest) throws Exception {

        this.friendService.saveFriendInvitation(sendFriendInvitationRequest.getReceiverUserId(),
                                                sendFriendInvitationRequest.getMessage());

    }

    @GetMapping("/listPendingInvitations") // /listPendingInvitations
    @NeedAuth

    public List<FriendInvitationResponse> listPendingInvitations(@RequestParam(required = false, defaultValue = "1") int page)
            throws Exception {
        this.friendService.listPendingInvitations(page);
        return null;
    }

    @PostMapping("/accept")
    @NeedAuth

    public void accept(
                       @RequestParam int friendInvitationId) throws Exception {
        this.friendService.accept(friendInvitationId);

    }

    @NeedAuth
    @PostMapping("/decline")
    public void decline(
                        @RequestParam int friendInvitationId) throws Exception {
        this.friendService.decline(friendInvitationId);

    }

    @GetMapping("/listFriends")
    @NeedAuth
    public List<UserResponse> listFriends(
                                          @RequestParam(required = false, defaultValue = "1") int page) throws Exception {
        List<Integer> friendUserIds = this.friendService.listFriendUserIds(page);
        return null;

    }
}
