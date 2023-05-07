package com.example.userserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private int id;
    @Field(name = "userName")
    @NonNull
    private String userName;
    @Field(name = "passWord")
    @JsonIgnore
    @NonNull
    private String passWord;
    @Field(name = "roles")
    private int role;
    private String phoneNumber;
    @Field(name = "email")
    @Indexed(unique = true)
    @NonNull
    private String email;
    @Field(name = "status")
    private int status;
    @Field(name = "upDateTime")
    public  long upDateTime;
}
