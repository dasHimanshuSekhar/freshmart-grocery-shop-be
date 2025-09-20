package com.groceryshop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AuthRequest {
    @Email
    @NotNull
    private String email;
    
    private String otp;
    private String name;
    private String phone;

//    // Constructors
//    public AuthRequest() {}
//
//    public AuthRequest(String email, String otp) {
//        this.email = email;
//        this.otp = otp;
//    }
//
//    // Getters and Setters
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getOtp() { return otp; }
//    public void setOtp(String otp) { this.otp = otp; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
}
