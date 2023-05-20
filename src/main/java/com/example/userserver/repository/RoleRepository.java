package com.example.userserver.repository;


import com.example.userserver.entity.ERole;
import com.example.userserver.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
