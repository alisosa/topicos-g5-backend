package com.ucu.topicos.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ucu.topicos.model.ERole;
import com.ucu.topicos.model.Invitation;
import com.ucu.topicos.model.ProviderEntity;
import com.ucu.topicos.model.User;
import com.ucu.topicos.repository.InvitationRepository;
import com.ucu.topicos.repository.ProviderRepository;
import com.ucu.topicos.repository.UserRepository;
import dtos.InviteProviderRequest;
import dtos.RegistrationRequest;
import dtos.UserDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final EmailService emailService;
    private final InvitationRepository invitationRepository;
    private final ProviderRepository providerRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, FirebaseAuth firebaseAuth, EmailService emailService, InvitationRepository invitationRepository, ProviderRepository providerRepository) {
        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;
        this.emailService = emailService;
        this.invitationRepository = invitationRepository;
        this.providerRepository = providerRepository;
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
            return new UserDTO(decodedToken.getUid(), user.getRole().toString(), user.getRut(), user.getMail());

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
    public void invite(InviteProviderRequest inviteRequest, String inviterId, ERole role) {
        try {
            Optional<User> dataUser = userRepository.findFirstByRut(inviteRequest.getRut());

            if (dataUser.isPresent()) {
                this.addRelationToUser(inviterId, dataUser.get());
            }else {
                String password = UUID.randomUUID().toString().substring(0, 8);
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(inviteRequest.getEmail())
                        .setPassword(password);

                UserRecord userRecord = firebaseAuth.createUser(request);
                logger.info("Successfully created a new user: {}", userRecord.getUid());

            User user = new User();
            user.setId(userRecord.getUid());
            user.setMail(inviteRequest.getEmail());
            user.setRole(role);
            user.setName(inviteRequest.getName());
            user.setRut(inviteRequest.getRut());

                userRepository.save(user);

                logger.info("User information saved in the database");

                this.addRelationToUser(inviterId, user);

                ProviderEntity providerEntity = new ProviderEntity();
                providerEntity.setEmail(inviteRequest.getEmail());
                providerEntity.setRut(inviteRequest.getRut());
                providerRepository.save(providerEntity);

                emailService.sendSimpleMessage(inviteRequest.getEmail(), "Your password is " + password);
                logger.info("Invitation sent to user");
            }


//            throw new IllegalArgumentException("User with the same RUT already exists");



        } catch (FirebaseAuthException e) {
            logger.error("Error registering the user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    private void addRelationToUser(String inviterId, User user){
        UserDTO userDTO = verifyToken(inviterId);
        User inviter = userRepository.findById(userDTO.getUserId()).orElseThrow();

        Invitation invitation = new Invitation();
        invitation.setInviter(inviter);
        invitation.setInvitee(user);
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setUpdatedAt(LocalDateTime.now());
        invitationRepository.save(invitation);


    }
}
