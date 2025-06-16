package com.songhanwu.messaging.response;

import com.songhanwu.messaging.dto.UserDTO;
import lombok.Data;

@Data
public class UserResponse {

    private int id;
    private String username;
    private String nickname;

    public UserResponse(int id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }

    public static UserResponse from(UserDTO userDTO) {
        return new UserResponse(userDTO.getId(), userDTO.getUsername(), userDTO.getNickname());
    }
}
