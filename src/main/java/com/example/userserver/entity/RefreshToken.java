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
  private long id;
  @Field(name = "user")
  @Indexed(unique = true)
  @NonNull
  private User user;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
