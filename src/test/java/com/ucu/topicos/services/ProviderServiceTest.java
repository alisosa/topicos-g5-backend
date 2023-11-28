package com.ucu.topicos.services;

import com.ucu.topicos.model.ProviderEntity;
import com.ucu.topicos.repository.InvitationRepository;
import com.ucu.topicos.repository.ProviderRepository;
import dtos.ProvidersResponse;
import dtos.UserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

public class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;
    @Mock
    UserDTO userDTO;
    @Mock
    ProviderEntity providerEntity;
    @Mock
    private UserService userService;

    @InjectMocks
    private ProviderService providerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProvidersAsAdmin() throws Exception {
        String userToken = "userToken";
        when(userDTO.getRole()).thenReturn("ADMIN");
        when(userService.verifyToken(userToken)).thenReturn(userDTO);
        when(providerEntity.getName()).thenReturn("name");
        when(providerEntity.getRut()).thenReturn("123");
        when(providerEntity.getScore()).thenReturn(5.0);
        when(providerEntity.getCategory()).thenReturn("textil");
        when(providerRepository.findAll()).thenReturn(List.of(providerEntity));

        ProvidersResponse response = providerService.getProviders("userToken", "name", "123", 1, 10, 0, "textil");

        assertEquals(0, response.getPages());
        assertEquals(1, response.getProviders().size());
    }


}

