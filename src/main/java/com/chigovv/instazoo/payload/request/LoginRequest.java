package com.chigovv.instazoo.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = "Usermane can not be empty")
    private String username;

    @NotEmpty(message = "Password can not be empty")
    private String password;

}
