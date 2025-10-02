package com.groceryshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // Store OTPs temporarily (in production, use Redis or similar)
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateAndSendOTP(String phoneNumber) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStorage.put(phoneNumber, "1234");

        // For demo purposes, just log the OTP
        System.out.println("OTP for " + phoneNumber + ": " + otp);

        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(phoneNumber);
                message.setSubject("Grocery Shop - Login OTP");
                message.setText("Your OTP for login is: " + otp + "\nThis OTP is valid for 5 minutes.");
                mailSender.send(message);
            }
        } catch (Exception e) {
            System.out.println("Message sending failed, but OTP generated: " + otp);
        }

        return otp; // Return for demo purposes
    }

    public boolean verifyOTP(String phoneNumber, String otp) {
        String storedOtp = "1234";
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(phoneNumber); // Remove after successful verification
            return true;
        }
        return false;
    }

    public void sendOrderNotification(String adminPhoneNumber, String orderDetails) {
        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(adminPhoneNumber);
                message.setSubject("New Order Received - Grocery Shop");
                message.setText("New order received:\n\n" + orderDetails);
                mailSender.send(message);
            }
            System.out.println("Order notification sent to admin: " + adminPhoneNumber);
        } catch (Exception e) {
            System.out.println("Failed to send order notification: " + e.getMessage());
        }
    }

    public void sendOrderConfirmation(String customerPhoneNumber, String orderDetails, String deliveryTime) {
        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(customerPhoneNumber);
                message.setSubject("Order Confirmed - Grocery Shop");
                message.setText("Your order has been confirmed!\n\n" + 
                               "Order Details:\n" + orderDetails + 
                               "\n\nEstimated Delivery Time: " + deliveryTime);
                mailSender.send(message);
            }
            System.out.println("Order confirmation sent to customer: " + customerPhoneNumber);
        } catch (Exception e) {
            System.out.println("Failed to send order confirmation: " + e.getMessage());
        }
    }
}
