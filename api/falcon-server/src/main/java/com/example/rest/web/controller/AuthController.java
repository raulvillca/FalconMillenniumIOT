package com.example.rest.web.controller;

import com.example.core.security.CurrentUser;
import com.example.core.security.UserPrincipal;
import com.example.core.model.socket.Message;
import com.example.rest.web.response.AuthResponse;
import com.example.core.response.JwtAuthenticationResponse;
import com.example.rest.web.response.AuthRequest;
import com.example.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest authRequest) {

        JwtAuthenticationResponse response = userService.generateToken(
                authRequest.getUsername(), authRequest.getPassword());

        return new ResponseEntity(new AuthResponse(true, "Sesion iniciada"),
                HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequest authRequest) {

        boolean isRegistered = userService.registerUser(
                authRequest.getName(),
                authRequest.getUsername(),
                authRequest.getPassword());

        if (isRegistered) {
            return new ResponseEntity(
                    new AuthResponse(true, "Registro completo"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(
                    new AuthResponse(false, "Username ya existe"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/push")
    public void push(@RequestBody Message message, @CurrentUser UserPrincipal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), "/notification", message);
    }
}
