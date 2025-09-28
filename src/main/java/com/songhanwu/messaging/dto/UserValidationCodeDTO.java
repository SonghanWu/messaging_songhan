package com.songhanwu.messaging.dto;

import lombok.Data;

@Data
public class UserValidationCodeDTO {
    private int id; // primary key
    private int userId; // foreign key UserValidationCode.userId == user.id
    private String validationCode;

}
