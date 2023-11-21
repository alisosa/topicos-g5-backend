package com.ucu.topicos.controllers;

import com.ucu.topicos.services.UserService;
import dtos.InviteProviderRequest;
import dtos.RegistrationRequest;
import dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestParam String idToken) {
        UserDTO user = userService.verifyToken(idToken);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registerRequest) {
        userService.registerUser(registerRequest);
        return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/inviteProvider")
    public ResponseEntity<?> inviteProvider(@Valid @RequestBody InviteProviderRequest inviteRequest) {
        try {
            userService.inviteProvider(inviteRequest);
            return new ResponseEntity<>("Invitation sent to user", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
