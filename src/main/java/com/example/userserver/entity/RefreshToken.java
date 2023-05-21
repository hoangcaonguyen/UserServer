package com.example.userserver.entity;

import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "refreshToken")
public class RefreshToken {
  @Id
  private String id;
  @Field(name = "userId")
  @Indexed(unique = true)
  @NonNull
  private String userId;
  @Field(name = "token")
  @Indexed(unique = true)
  @NonNull
  private String token;
  @Field(name = "expiryDate")
  @Indexed(unique = true)
  @NonNull
  private Instant expiryDate;

  public RefreshToken() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

}
