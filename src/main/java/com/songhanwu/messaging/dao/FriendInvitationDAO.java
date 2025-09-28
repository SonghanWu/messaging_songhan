package com.songhanwu.messaging.dao;

import java.util.List;

import com.songhanwu.messaging.dto.FriendInvitationDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FriendInvitationDAO {

    @Insert("")
    void insert(FriendInvitationDTO friendInvitationDTO);

    @Select("")
    FriendInvitationDTO selectById(int friendInvitationId);

    @Update("UPDATE \"friend_invitation\" SET \"status\"='ACCEPTED' WHERE \"id\" = #{id}")
    void updateToAccepted(int id);

    @Update("UPDATE \"friend_invitation\" SET \"status\"='DECLINED' WHERE \"id\" = #{id}")
    void updateToDeclined(int id);

    @Select("SELECT * FROM \"friend_invitation\" WHERE \"status\"='PENDING' AND \"receiver_user_id\" = #{receiverUserId} LIMIT #{startIndex}, #{limit}")
    List<FriendInvitationDTO> selectPendingInvitationsByReceiverUserId(int receiverUserId, int startIndex, int limit);

    @Select("SELECT * FROM \"friend_invitation\" WHERE \"status\"='ACCEPTED' AND (\"receiver_user_id\" = #{userId} OR \"sender_user_id\" = #{userId}) LIMIT #{startIndex}, #{limit}")
    List<FriendInvitationDTO> selectAcceptedInvitations(int userId, int start, int limit);
}
