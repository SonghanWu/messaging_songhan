package com.songhanwu.messaging.dao;

import com.songhanwu.messaging.dto.MessageDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageDAO {
    @Insert("INSERT INTO \"message\" (\"receiver_user_id\", \"group_chat_id\", \"message_type\", \"content\", \"send_time\", \"sender_user_id\")" +
            " VALUES (#{receiverUserId}, #{groupChatId}, #{messageType}, #{content}, #{sendTime}, #{senderUserId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id") // messageDTO.setId(10);
    void insert(MessageDTO messageDTO);
}
