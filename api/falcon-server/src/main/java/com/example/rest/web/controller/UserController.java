package com.example.rest.web.controller;

import com.example.core.model.User;
import com.example.rest.web.response.*;
import com.example.core.security.UserPrincipal;
import com.example.core.security.CurrentUser;
import com.example.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public UserProfile getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        User user = userService.findByUsername(userPrincipal.getUsername());

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
    }

    @GetMapping("/check_username_availability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new UserIdentityAvailability(userService.existsUsername(username));
    }

    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {

        User user = userService.findByUsername(username);

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
    }

}
