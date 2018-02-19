package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.auth.EmailAlreadyExistsException;
import com.dexmohq.imadex.auth.UserService;
import com.dexmohq.imadex.auth.User;
import com.dexmohq.imadex.auth.UsernameAlreadyExistsException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerNew(@RequestBody UserRegistrationData userRegistrationData) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        return userService.registerUser(userRegistrationData.username,
                userRegistrationData.email, userRegistrationData.password);
    }

    @Data
    public static class UserRegistrationData {

        private String username;
        private String email;
        private String password;

    }
}
