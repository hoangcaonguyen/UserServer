package com.example.userserver.repository;

import com.example.userserver.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, Integer > {
    @Query("{'userName': ?0}")
    User findByUserName(String userName);
    User findAllByUserName(String userName);
    User findByEmail (String email);
    User findById (int id);
    List<User> findAllByStatus (int status);
}
