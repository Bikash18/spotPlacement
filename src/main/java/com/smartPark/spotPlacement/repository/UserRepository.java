package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
   public User findByUsername(String username);
    Boolean existsByUsername(String username);

}
