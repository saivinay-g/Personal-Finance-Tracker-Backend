package com.personalfinancetracker.personalfinancetracker.controller;

import com.personalfinancetracker.personalfinancetracker.dto.LoginRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.UserResponseDTO;
import com.personalfinancetracker.personalfinancetracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthController {
    @Autowired
    private UserService service;

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userDto){
        return new ResponseEntity<>(service.register(userDto), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginRequestDTO loginDto){
        return new ResponseEntity<>(service.login(loginDto), HttpStatus.OK);
    }

    @GetMapping("get-token")
    public ResponseEntity<CsrfToken> getAuthToken(HttpServletRequest req){
        return new ResponseEntity<>((CsrfToken) req.getAttribute("_csrf"),HttpStatus.OK);
    }

    @GetMapping("get-sessionId")
    public ResponseEntity<String> getSessionId(HttpSession session){
        return new ResponseEntity<>(session.getId(), HttpStatus.OK);
    }
}
