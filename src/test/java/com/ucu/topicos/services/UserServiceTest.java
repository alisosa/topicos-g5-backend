package com.ucu.topicos.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.ucu.topicos.model.ERole;
import com.ucu.topicos.model.User;
import com.ucu.topicos.repository.UserRepository;
import dtos.UserDTO;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock User user;
    @Mock
    FirebaseToken firebaseToken;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifyToken() throws FirebaseAuthException {
        when(firebaseToken.getUid()).thenReturn("uniqueUserId");
        when(firebaseAuth.verifyIdToken("idToken")).thenReturn(firebaseToken);
        when(userRepository.findById("uniqueUserId")).thenReturn(Optional.ofNullable(user));
        when(user.getRole()).thenReturn(ERole.SOCIO);
        when(user.getRut()).thenReturn("rut");
        when(user.getMail()).thenReturn("mail");

        UserDTO result = userService.verifyToken("idToken");

        assertEquals("uniqueUserId", result.getUserId());

    }

}
