package com.ucu.topicos.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ucu.topicos.model.ERole;
import com.ucu.topicos.model.User;
import com.ucu.topicos.repository.UserRepository;
import dtos.InviteProviderRequest;
import dtos.RegistrationRequest;
import dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, FirebaseAuth firebaseAuth, EmailService emailService) {
        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;
        this.emailService = emailService;
    }

    public UserDTO verifyToken(String idToken) {
        FirebaseToken decodedToken = null;
        User user = null;
        try {
            decodedToken = firebaseAuth.verifyIdToken(idToken);
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

    @Transactional
    public void inviteProvider(InviteProviderRequest inviteRequest) {
        try {
            if (userRepository.findFirstByRut(inviteRequest.getRut()).isPresent()) {
                throw new IllegalArgumentException("User with the same RUT already exists");
            }

            String password = UUID.randomUUID().toString().substring(0, 8);
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(inviteRequest.getEmail())
                    .setPassword(password);

            UserRecord userRecord = firebaseAuth.createUser(request);
            logger.info("Successfully created a new user: {}", userRecord.getUid());

            User user = new User();
            user.setId(userRecord.getUid());
            user.setMail(inviteRequest.getEmail());
            user.setRole(ERole.PROVEEDOR);
            user.setName(inviteRequest.getName());
            user.setRut(inviteRequest.getRut());

            userRepository.save(user);
            logger.info("User information saved in the database");

            emailService.sendSimpleMessage(inviteRequest.getEmail(), "Your password is " + password);
            logger.info("Invitation sent to user");
        } catch (FirebaseAuthException e) {
            logger.error("Error registering the user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }
}
