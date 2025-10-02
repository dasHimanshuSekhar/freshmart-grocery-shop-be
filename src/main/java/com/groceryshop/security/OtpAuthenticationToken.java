package com.groceryshop.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OtpAuthenticationToken extends AbstractAuthenticationToken {

    private final String phone;
    private final String otp;

    public OtpAuthenticationToken(String phone, String otp) {
        super(null);
        this.phone = phone;
        this.otp = otp;
        setAuthenticated(false);
    }

    public OtpAuthenticationToken(String phone, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.otp = null;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return otp;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
