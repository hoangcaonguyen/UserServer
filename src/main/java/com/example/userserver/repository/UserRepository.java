package com.example.userserver.repository;

import com.example.userserver.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String > {
    @Query("{'userName': ?0}")
    User findByUserName(String userName);
    User findAllByUserName(String userName);
    User findByEmail (String email);
    List<User> findAllByStatus (int status);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

}
