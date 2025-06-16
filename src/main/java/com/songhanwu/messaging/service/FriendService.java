package com.songhanwu.messaging.service;

import java.util.Date;

import com.songhanwu.messaging.dao.FriendInvitationDAO;
import com.songhanwu.messaging.dto.FriendInvitationDTO;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.enumeration.FriendInvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired private FriendInvitationDAO friendInvitationDAO;
    @Autowired private UserService userService;

    public void saveFriendInvitation(String senderLoginToken, int receiverUserId, String message) throws Exception {
        UserDTO sender = this.userService.authenticate(senderLoginToken);
        UserDTO receiver = this.userService.findUserByUserId(receiverUserId);
        // check if they are friends already
        // check if they have pending invitations

        FriendInvitationDTO friendInvitationDTO = new FriendInvitationDTO();
        friendInvitationDTO.setSenderUserId(sender.getId());
        friendInvitationDTO.setReceiverUserId(receiver.getId());
        friendInvitationDTO.setMessage(message);
        friendInvitationDTO.setCreateTime(new Date());
        friendInvitationDTO.setStatus(FriendInvitationStatus.PENDING);
        this.friendInvitationDAO.insert(friendInvitationDTO);

    }

    public void accept(String receiverLoginToken, int friendInvitationId) throws Exception {
        UserDTO userDTO = this.userService.authenticate(receiverLoginToken);
        FriendInvitationDTO friendInvitationDTO = this.friendInvitationDAO.selectById(friendInvitationId);
        if (friendInvitationDTO == null || friendInvitationDTO.getReceiverUserId() != userDTO.getId()) {
            throw new Exception("Friend invitation does not exist");
        }
        this.friendInvitationDAO.updateToAccepted(friendInvitationDTO.getId());
    }

    public void decline(String receiverLoginToken, int friendInvitationId) throws Exception {
        UserDTO userDTO = this.userService.authenticate(receiverLoginToken);
        FriendInvitationDTO friendInvitationDTO = this.friendInvitationDAO.selectById(friendInvitationId);
        if (friendInvitationDTO == null || friendInvitationDTO.getReceiverUserId() != userDTO.getId()) {
            throw new Exception("Friend invitation does not exist");
        }
        this.friendInvitationDAO.updateToAccepted(friendInvitationDTO.getId());
    }
}
