package com.example.Web_application.service;

import com.example.Web_application.model.User;
import com.example.Web_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.example.Web_application.service.EmailService emailService;

    public User registerUser(User user) {
        user.setEnabled(false);
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Send verification email
        String subject = "Email Verification OTP";
        String text = "Your OTP for email verification is: " + otp +
                "\nThis OTP is valid for 5 minutes.";
        emailService.sendEmail(user.getEmail(), subject, text);

        return savedUser;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password) && user.isEnabled()) {
            return user;
        }
        return null;
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getOtp() != null &&
                user.getOtp().equals(otp) &&
                user.getOtpGeneratedTime().plusMinutes(5).isAfter(LocalDateTime.now())) {
            user.setEnabled(true);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean sendResetPasswordOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);

            String subject = "Password Reset OTP";
            String text = "Your OTP for password reset is: " + otp +
                    "\nThis OTP is valid for 5 minutes.";
            emailService.sendEmail(email, subject, text);
            return true;
        }
        return false;
    }

    public boolean resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getOtp() != null &&
                user.getOtp().equals(otp) &&
                user.getOtpGeneratedTime().plusMinutes(5).isAfter(LocalDateTime.now())) {
            user.setPassword(newPassword);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}