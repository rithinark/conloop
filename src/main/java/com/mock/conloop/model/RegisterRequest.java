package com.mock.conloop.model;

import lombok.Data;

@Data
public class RegisterRequest extends AuthRequest{
    private String username;

    public User toUser(){
        User user = new User();
        user.setEmail(this.getEmail());
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        return user;
    }
}
