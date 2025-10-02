package com.groceryshop.service;

import com.groceryshop.dto.ApiResponse;
import com.groceryshop.dto.AuthRequest;
import com.groceryshop.security.JwtUtil;
import com.groceryshop.security.OtpAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private FirestoreService firestoreService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;


    public ResponseEntity<ApiResponse<Map<String, Object>>> adminRegistration(AuthRequest request){
        try {
            // Check if admin already exists
            List<Map<String, Object>> existingAdmins = firestoreService.findByField("users", "role", "ADMIN");
            if (!existingAdmins.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Admin already exists"));
            }

            // Create admin user
            Map<String, Object> adminData = new HashMap<>();
            adminData.put("email", request.getPhone());
            adminData.put("name", request.getName());
            adminData.put("phone", request.getPhone());
            adminData.put("role", "ADMIN");
            adminData.put("isVerified", true);
            adminData.put("createdAt", LocalDateTime.now().toString());
            adminData.put("updatedAt", LocalDateTime.now().toString());

            String adminId = firestoreService.save("users", adminData);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", adminId);
            response.put("email", request.getPhone());
            response.put("role", "ADMIN");

            return ResponseEntity.ok(ApiResponse.success("Admin registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<String>> sendOtp(AuthRequest request){
        try {
            String otp = notificationService.generateAndSendOTP(request.getPhone());
            return ResponseEntity.ok(ApiResponse.success("OTP sent successfully", otp));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to send OTP: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> userRegistration(AuthRequest request){
        try {
            System.out.println("OTP Verified !");

            // Check if user already exists
            List<Map<String, Object>> existingUsers = firestoreService.findByField("users", "email", request.getPhone());
            if (!existingUsers.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User already exists"));
            }

            // Create customer user
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", request.getPhone());
            userData.put("name", request.getName());
            userData.put("phone", request.getPhone());
            userData.put("role", "CUSTOMER");
            userData.put("isVerified", true);
            userData.put("createdAt", LocalDateTime.now().toString());
            userData.put("updatedAt", LocalDateTime.now().toString());

            System.out.println("user data: " + userData.toString());
            String userId = firestoreService.save("users", userData);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("email", request.getPhone());
            response.put("name", request.getName());
            response.put("role", "CUSTOMER");

            return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> userLogin(AuthRequest request) {
        try {
            // 🔎 Step 1: Check if user exists in Firestore
            List<Map<String, Object>> users = firestoreService.findByField("users", "phone", request.getPhone());
            if (users.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found"));
            }

            // 🔑 Step 2: Wrap phone + otp in Authentication
            Authentication authRequest = new OtpAuthenticationToken(request.getPhone(), request.getOtp());

            // 🔑 Step 3: Authenticate via OtpBasedAuthenticationProvider
            Authentication authResult = authenticationManager.authenticate(authRequest);

            // ✅ Step 4: OTP valid → generate JWT
            String token = jwtUtil.generateToken((String) authResult.getPrincipal());

            // 📦 Step 5: Build response with user details + JWT
            Map<String, Object> user = users.get(0);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.get("id"));
            response.put("email", user.get("email"));
            response.put("phone", user.get("phone"));
            response.put("name", user.get("name"));
            response.put("role", user.get("role"));
            response.put("token", token);

            return ResponseEntity.ok(ApiResponse.success("Login successful", response));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid OTP or login failed: " + e.getMessage()));
        }
    }

}
