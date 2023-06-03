package com.mock.conloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.mock.conloop.constant.UserQueryConstant;
import com.mock.conloop.model.Query;
import com.mock.conloop.model.User;
import com.mock.conloop.repository.QueryRespository;
import com.mock.conloop.repository.UserRepository;
import com.mock.conloop.service.UserService;

@RestController
public class Test {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/users")
    public ResponseEntity<Object> getTest(){
        User user = userRepository.findByEmail("rithin@gmail.com").get();
        return new ResponseEntity<>(user, null, 200);
    }
}
