package com.ecommerce.videocalling.repo;

import com.ecommerce.videocalling.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);
}
