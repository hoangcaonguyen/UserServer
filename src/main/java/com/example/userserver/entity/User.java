package com.example.userserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "user")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User /*implements UserDetails*/ {

    @Id
    private String id;
    @Field(name = "userName")
    @NonNull
    private String userName;
    @Field(name = "passWord")
    @JsonIgnore
    @NonNull
    private String passWord;
    @Field(name = "roles")
    private Set<Role> roles = new HashSet<>();
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
