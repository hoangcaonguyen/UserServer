package com.example.userserver.dto;

import lombok.NonNull;

import java.util.Set;

public class SignupRequest {
    @NonNull
    private String userName;

    @NonNull
    private String email;
    
    private Set<String> roles;

    @NonNull
    private String passWord;

    @NonNull
    private String phoneNumber;
  
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassWord() {
        return passWord;
    }
 
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    
    public Set<String> getRoles() {
      return this.roles;
    }
    
    public void setRole(Set<String> roles) {
      this.roles = roles;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
