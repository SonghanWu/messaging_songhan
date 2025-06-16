package com.songhanwu.messaging.response;

import java.util.Date;

import com.songhanwu.messaging.enumeration.FriendInvitationStatus;
import lombok.Data;

@Data
public class FriendInvitationResponse {
    private int id; // primary key
    private UserResponse sender;
    private UserResponse receiver;
    private String message;
    private Date createTime;
    private FriendInvitationStatus status;
}
