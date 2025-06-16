package com.songhanwu.messaging.dto;

import java.util.Date;

import com.songhanwu.messaging.enumeration.Gender;
import lombok.Data;

@Data
public class UserDTO { // data transfer object
    private int id; // primary key
    private String username;
    private String password;
    private String nickname;
    private String email; // 2FA second factor authentication
    private Gender gender;
    private String address;
    private Date registerTime;
    private Boolean isValid; //
}
