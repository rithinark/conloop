package com.mock.conloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mock.conloop.model.User;
import com.mock.conloop.repository.UserRepository;

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
