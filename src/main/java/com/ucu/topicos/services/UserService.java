package com.ucu.topicos.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ucu.topicos.model.ERole;
import com.ucu.topicos.model.User;
import com.ucu.topicos.repository.UserRepository;
import dtos.RegistrationRequest;
import dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDTO verifyToken(String idToken) {
        FirebaseToken decodedToken = null;
        User user = null;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
             user = userRepository.findById(decodedToken.getUid()).orElseThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (decodedToken != null) {
            return new UserDTO(decodedToken.getUid(), user.getRole().toString());
        }
        return null;
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
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
