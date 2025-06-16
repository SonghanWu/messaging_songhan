package com.songhanwu.messaging.request;

import lombok.Data;

@Data
public class UserActivationRequest {
    private String username;
    private String validationCode;
}
