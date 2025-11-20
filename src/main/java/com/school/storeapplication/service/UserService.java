package com.school.storeapplication.service;

import com.school.storeapplication.domain.Role;
import com.school.storeapplication.domain.user.User;
import com.school.storeapplication.repo.UserRepository;
import com.school.storeapplication.web.RegisterForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCustomer(RegisterForm form) {
        if (users.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword())); // IMPORTANT
        user.getRoles().add(Role.BUYER);

        users.save(user);
    }
}
