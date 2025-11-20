package com.school.storeapplication.web;

import com.school.storeapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService users;

    public RegisterController(UserService users) {
        this.users = users;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("form") RegisterForm form) {
        users.registerCustomer(form);
        return "redirect:/login?registered";
    }
}
