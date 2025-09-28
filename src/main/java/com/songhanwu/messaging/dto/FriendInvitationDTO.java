package com.songhanwu.messaging.dto;

import java.util.Date;

import com.songhanwu.messaging.enumeration.FriendInvitationStatus;
import lombok.Data;

@Data
public class FriendInvitationDTO { // data transfer object
    private int id; // primary key
    private int senderUserId;
    private int receiverUserId;
    private String message;
    private Date createTime;
    private FriendInvitationStatus status;
}
