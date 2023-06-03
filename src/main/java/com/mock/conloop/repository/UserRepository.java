package com.mock.conloop.repository;

import java.util.Optional;

import com.mock.conloop.model.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    void delete(User user);
    User save(User user);
}
