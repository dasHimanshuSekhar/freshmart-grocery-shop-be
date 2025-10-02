package com.groceryshop.controller;

import com.groceryshop.dto.ApiResponse;
import com.groceryshop.dto.AuthRequest;
import com.groceryshop.service.AuthService;
import com.groceryshop.service.NotificationService;
import com.groceryshop.service.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FirestoreService firestoreService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerAdmin(@Valid @RequestBody AuthRequest request) {
        return authService.adminRegistration(request);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOTP(@Valid @RequestBody AuthRequest request) {
        return authService.sendOtp(request);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody AuthRequest request) {
        return authService.userRegistration(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody AuthRequest request) {
        return authService.userLogin(request);
    }
}
