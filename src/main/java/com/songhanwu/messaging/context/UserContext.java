package com.songhanwu.messaging.context;

import com.songhanwu.messaging.dto.UserDTO;

public class UserContext {

    private static ThreadLocal<UserDTO> CURRENT_USER = new ThreadLocal<>();

    public static void setCurrentUser(UserDTO userDTO) {
        CURRENT_USER.set(userDTO);
    }

    public static UserDTO getCurrentUser() {
        return CURRENT_USER.get();
    }
}
