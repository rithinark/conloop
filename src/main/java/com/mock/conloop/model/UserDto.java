package com.mock.conloop.model;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private String userId;
    private String username;
    private String email;
    private List<String> roles;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map((Role role) -> role.getRoleName()).toList();
    }
}
