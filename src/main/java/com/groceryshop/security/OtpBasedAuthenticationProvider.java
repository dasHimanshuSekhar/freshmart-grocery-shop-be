package com.groceryshop.security;

import com.groceryshop.service.AuthService;
import com.groceryshop.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class OtpBasedAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private NotificationService notificationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = (String) authentication.getPrincipal();
        String otp = (String) authentication.getCredentials();

        if(notificationService.verifyOTP(phoneNumber, otp)){
            return new OtpAuthenticationToken(phoneNumber, otp);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
