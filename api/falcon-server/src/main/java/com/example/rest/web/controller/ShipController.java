package com.example.rest.web.controller;

import com.example.core.model.User;
import com.example.core.security.CurrentUser;
import com.example.core.security.UserPrincipal;
import com.example.core.service.UserService;
import com.example.rest.web.response.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/ship")
public class ShipController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ShipController.class);

    @GetMapping("/command")
    public UserProfile getCurrentUser(String command, @CurrentUser UserPrincipal userPrincipal) {

        User user = userService.findByUsername(userPrincipal.getUsername());

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
    }

}
