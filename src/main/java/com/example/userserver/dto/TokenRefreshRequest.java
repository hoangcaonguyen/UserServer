package com.example.userserver.dto;

import lombok.NonNull;


public class TokenRefreshRequest {
    @NonNull
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
