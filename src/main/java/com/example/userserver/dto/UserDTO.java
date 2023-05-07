package com.example.userserver.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String passWord;
    private String phoneNumber;
    private String email;

}
