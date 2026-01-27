package com.personalfinancetracker.personalfinancetracker.service;

import com.personalfinancetracker.personalfinancetracker.dto.LoginRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserResponseDTO;
import com.personalfinancetracker.personalfinancetracker.exception.InvalidRequestException;
import com.personalfinancetracker.personalfinancetracker.exception.ResourceNotFoundException;
import com.personalfinancetracker.personalfinancetracker.model.User;
import com.personalfinancetracker.personalfinancetracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepo repo){
        this.repo = repo;
    }

    public UserResponseDTO getUserById(int id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        UserResponseDTO userRes = new UserResponseDTO();
        userRes.setId(user.getId());
        userRes.setUsername(user.getName());
        userRes.setEmail(user.getEmail());
        userRes.setCreatedAt(user.getCreatedAt());
        return userRes;
    }

    public String encryptPassword(String password){
        return encoder.encode(password);
    }


    public UserResponseDTO register(UserRequestDTO userDto) {
        if (repo.findByEmail(userDto.getEmail()) != null) {
            throw new InvalidRequestException("Email already exists: " + userDto.getEmail());
        }
        if (repo.findByName(userDto.getUsername()) != null) {
            throw new InvalidRequestException("Username already exists: " + userDto.getUsername());
        }

        User user = new User();
        user.setName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encryptPassword(userDto.getPassword())); // plain for now
        user.setCreatedAt(LocalDateTime.now());

        User saved = repo.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(saved.getId());
        response.setUsername(saved.getName());
        response.setEmail(saved.getEmail());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }


    public UserResponseDTO login(LoginRequestDTO login) {
        User user = repo.findByNameAndPassword(login.getUsername(), encryptPassword(login.getPassword()));

        if (user == null) {
            throw new InvalidRequestException("Invalid username or password");
        }

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }


    public List<UserResponseDTO> getAllUsers() {
        return repo.findAll().stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getName());
            dto.setEmail(user.getEmail());
            dto.setCreatedAt(user.getCreatedAt());
            return dto;
        }).toList();
    }

}
