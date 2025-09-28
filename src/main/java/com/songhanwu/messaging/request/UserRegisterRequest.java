package com.songhanwu.messaging.request;

import com.songhanwu.messaging.enumeration.Gender;
import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String repeatPassword;
    private String nickname;
    private String email; // 2FA second factor authentication
    private Gender gender;
    private String address;
}
