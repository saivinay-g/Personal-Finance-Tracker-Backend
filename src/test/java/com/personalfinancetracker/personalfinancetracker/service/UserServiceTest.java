package com.personalfinancetracker.personalfinancetracker.service;

import com.personalfinancetracker.personalfinancetracker.dto.LoginRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserResponseDTO;
import com.personalfinancetracker.personalfinancetracker.exception.InvalidRequestException;
import com.personalfinancetracker.personalfinancetracker.exception.ResourceNotFoundException;
import com.personalfinancetracker.personalfinancetracker.model.User;
import com.personalfinancetracker.personalfinancetracker.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getUserById_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(user.getName(), result.getUsername());
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void register_Success() {
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("pass");

        when(userRepo.findByEmail(anyString())).thenReturn(null);
        when(userRepo.findByName(anyString())).thenReturn(null);
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.register(request);

        assertNotNull(result);
        verify(userRepo).save(any(User.class));
    }

    @Test
    void register_EmailExists() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@example.com");

        when(userRepo.findByEmail(request.getEmail())).thenReturn(user);

        assertThrows(InvalidRequestException.class, () -> userService.register(request));
    }

    @Test
    void login_Success() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setUsername("testuser");
        login.setPassword("password");

        when(userRepo.findByNameAndPassword("testuser", "password")).thenReturn(user);

        UserResponseDTO result = userService.login(login);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void login_Failure() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setUsername("wrong");
        login.setPassword("wrong");

        when(userRepo.findByNameAndPassword("wrong", "wrong")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> userService.login(login));
    }

    @Test
    void getAllUsers_Success() {
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> results = userService.getAllUsers();

        assertEquals(1, results.size());
        assertEquals(user.getName(), results.get(0).getUsername());
    }
}