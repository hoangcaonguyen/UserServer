package com.example.userserver.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;



@Getter
@Setter
public class LoginDTO {
    @NonNull
    private String userName;
    @NonNull
    private String passWord;
}
