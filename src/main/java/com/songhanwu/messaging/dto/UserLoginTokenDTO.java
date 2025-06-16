package com.songhanwu.messaging.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserLoginTokenDTO {
    private int id;
    private int userId;
    private String loginToken;
    private Date loginTime;
}
