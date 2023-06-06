package com.example.userserver.repository;

import com.example.userserver.entity.RefreshToken;
import com.example.userserver.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUserId(String userId);
    void deleteByToken(String refreshToken);
}
