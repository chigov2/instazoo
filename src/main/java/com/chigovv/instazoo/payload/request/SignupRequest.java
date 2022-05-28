package com.chigovv.instazoo.payload.request;

import com.chigovv.instazoo.annotations.PasswordMatches;
import com.chigovv.instazoo.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
     private String email;
    @NotEmpty(message = "Please enter your name")
     private String firstname;
    @NotEmpty(message = "Please enter your last name")
     private String lastname;
    @NotEmpty(message = "Please enter your username")
     private String username;
    @NotEmpty(message = "Password is required and length should be at least 5 characters")
    @Size(min = 6)
     private String password;
     private String confirmPassword;
}
