package com.mock.conloop.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mock.conloop.constant.ContextConstant;
import com.mock.conloop.model.Role;
import com.mock.conloop.model.User;
import com.mock.conloop.model.UserDto;
import com.mock.conloop.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(User user) {
        User _user;
        UserDto userDto;
        List<String> roles = Arrays.asList(ContextConstant.ROLE_USER);
        user.setRoles(roles.stream().map((String role) -> new Role(null, role)).toList());

        try {
            _user = userRepository.save(user);
            userDto = new UserDto(_user);
            return userDto;
        } catch (Exception ex) {
            throw new IllegalArgumentException("invaid user name");
        }
    }

}
