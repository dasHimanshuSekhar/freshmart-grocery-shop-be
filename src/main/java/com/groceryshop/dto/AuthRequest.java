package com.groceryshop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String phone;

    private String otp;
    private String name;
    private String email;
}
