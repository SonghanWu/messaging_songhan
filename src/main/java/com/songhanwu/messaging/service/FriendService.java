package com.songhanwu.messaging.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.songhanwu.messaging.context.UserContext;
import com.songhanwu.messaging.dao.FriendInvitationDAO;
import com.songhanwu.messaging.dto.FriendInvitationDTO;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.enumeration.FriendInvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    private static final int PAGE_LENGTH = 15;
    @Autowired private FriendInvitationDAO friendInvitationDAO;
    @Autowired private UserService userService;

    public void saveFriendInvitation(int receiverUserId, String message) throws Exception {
        UserDTO sender = UserContext.getCurrentUser();
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

    public void accept(int friendInvitationId) throws Exception {
        UserDTO userDTO = UserContext.getCurrentUser();
        FriendInvitationDTO friendInvitationDTO = this.friendInvitationDAO.selectById(friendInvitationId);
        if (friendInvitationDTO == null || friendInvitationDTO.getReceiverUserId() != userDTO.getId()) {
            throw new Exception("Friend invitation does not exist");
        }
        this.friendInvitationDAO.updateToAccepted(friendInvitationDTO.getId());
    }

    public void decline(int friendInvitationId) throws Exception {
        UserDTO userDTO = UserContext.getCurrentUser();
        FriendInvitationDTO friendInvitationDTO = this.friendInvitationDAO.selectById(friendInvitationId);
        if (friendInvitationDTO == null || friendInvitationDTO.getReceiverUserId() != userDTO.getId()) {
            throw new Exception("Friend invitation does not exist");
        }
        this.friendInvitationDAO.updateToAccepted(friendInvitationDTO.getId());
    }

    public List<FriendInvitationDTO> listPendingInvitations(int page) throws Exception {
        UserDTO userDTO = UserContext.getCurrentUser();

        int limit = PAGE_LENGTH;
        int start = (page - 1) * PAGE_LENGTH;
        return this.friendInvitationDAO.selectPendingInvitationsByReceiverUserId(userDTO.getId(), start, limit);

    }

    public List<Integer> listFriendUserIds(int page) throws Exception {
        UserDTO userDTO = UserContext.getCurrentUser();

        int limit = PAGE_LENGTH;
        int start = (page - 1) * PAGE_LENGTH;
        var acceptedInvitations = this.friendInvitationDAO.selectAcceptedInvitations(userDTO.getId(), start, limit);
        List<Integer> friendUserIds = new ArrayList<>();
        for (var acceptedInvitation : acceptedInvitations) {
            if (userDTO.getId() == acceptedInvitation.getSenderUserId()) {
                friendUserIds.add(acceptedInvitation.getReceiverUserId());
            } else {
                friendUserIds.add(acceptedInvitation.getSenderUserId());
            }
        }
        return friendUserIds;

    }
}
