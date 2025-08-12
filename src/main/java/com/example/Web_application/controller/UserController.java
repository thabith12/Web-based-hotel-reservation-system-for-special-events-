package com.example.Web_application.controller;

import com.example.Web_application.model.User;
import com.example.Web_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        User user = userService.loginUser(email, password);
        if (user != null) {
            return "redirect:/view/" + user.getId();
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        try {
            User registeredUser = userService.registerUser(user);
            model.addAttribute("email", registeredUser.getEmail());
            return "verify";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify")
    public String showVerifyForm(@RequestParam(required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "verify";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model) {
        if (userService.verifyOtp(email, otp)) {
            return "redirect:/login?verified=true";
        }
        model.addAttribute("error", "Invalid OTP or OTP expired");
        model.addAttribute("email", email);
        return "verify";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotpassword";
    }

    @PostMapping("/forgot-password")
    public String sendResetPasswordOtp(@RequestParam String email, Model model) {
        if (userService.sendResetPasswordOtp(email)) {
            model.addAttribute("email", email);
            return "resetpassword";
        }
        model.addAttribute("error", "Email not found");
        return "forgotpassword";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "resetpassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword,
                                Model model) {
        if (userService.resetPassword(email, otp, newPassword)) {
            return "redirect:/login?reset=true";
        }
        model.addAttribute("error", "Invalid OTP or OTP expired");
        model.addAttribute("email", email);
        return "resetpassword";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "view";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/update")
    public String updateUser(User user) {
        userService.updateUser(user);
        return "redirect:/view/" + user.getId();
    }
}