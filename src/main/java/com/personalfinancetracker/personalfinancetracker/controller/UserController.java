package com.personalfinancetracker.personalfinancetracker.controller;

import com.personalfinancetracker.personalfinancetracker.dto.UserResponseDTO;
import com.personalfinancetracker.personalfinancetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://localhost:5173"})
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UserResponseDTO> getUserData(@PathVariable int id){
        UserResponseDTO dto = service.getUserById(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsersData(){

        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }
}
