package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface UserController {
    @GetMapping(value = "/info", consumes = MediaType.ALL_VALUE)
    String getInfo(@InsertUser User authenticatedUser);
}
