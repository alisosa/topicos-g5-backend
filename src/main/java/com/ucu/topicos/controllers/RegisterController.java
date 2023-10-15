package com.ucu.topicos.controllers;

import com.ucu.topicos.services.RegisterService;
import dtos.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registerRequest) {
        registerService.registerUser(registerRequest);
        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
    }
}
