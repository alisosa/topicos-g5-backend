package com.ucu.topicos.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.ucu.topicos.model.ERole;
import com.ucu.topicos.model.User;
import com.ucu.topicos.repository.UserRepository;
import dtos.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    @Autowired
    private UserRepository userRepository;

    public void registerUser(RegistrationRequest registrationRequest) {
        try {
            CreateRequest request = new CreateRequest()
                    .setEmail(registrationRequest.getEmail())
                    .setPassword(registrationRequest.getPassword());

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            logger.info("Successfully created a new user: {}", userRecord.getUid());

            User user = new User();
            user.setId(userRecord.getUid());
            user.setMail(registrationRequest.getEmail());
            user.setRole(ERole.valueOf(registrationRequest.getRole()));
            user.setName(registrationRequest.getName());

            userRepository.save(user);
            logger.info("User information saved in the database");

        } catch (Exception e) {
            logger.error("Error registering the user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }
}
