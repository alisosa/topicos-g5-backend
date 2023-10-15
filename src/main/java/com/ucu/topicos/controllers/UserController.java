package com.ucu.topicos.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.ucu.topicos.model.User;
import com.ucu.topicos.services.UserService;
import dtos.RegistrationRequest;
import dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registerRequest) {
        userService.registerUser(registerRequest);
        return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
    }

}
