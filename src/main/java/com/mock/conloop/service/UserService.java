package com.mock.conloop.service;

import com.mock.conloop.model.User;
import com.mock.conloop.model.UserDto;

public interface UserService {
    public UserDto createUser(User user);
}
