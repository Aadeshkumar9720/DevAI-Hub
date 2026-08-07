package com.devaihub.backend.service;

import com.devaihub.backend.jwt.JwtService;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.devaihub.backend.dto.RegisterRequest;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.enums.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.devaihub.backend.service.interfaces.EmailService;
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testRegisterUser() {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Aadesh");
        request.setLastName("Kumar");
        request.setUsername("aadesh");
        request.setEmail("aadesh@gmail.com");
        request.setPassword("Password123");
        request.setPhoneNumber("9876543210");
        request.setRole(Role.ADMIN);

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        User savedUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password("encodedPassword")
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("aadesh", result.getUsername());
        assertEquals("Aadesh", result.getFirstName());
        assertEquals(Role.ADMIN, result.getRole());

        verify(userRepository).save(any(User.class));
    }
    @Test
    void testRegisterUser_EmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest();

        request.setEmail("aadesh@gmail.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void testRegisterUser_UsernameAlreadyExists() {

        RegisterRequest request = new RegisterRequest();

        request.setEmail("aadesh@gmail.com");
        request.setUsername("aadesh");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}