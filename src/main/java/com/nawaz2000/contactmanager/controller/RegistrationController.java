package com.nawaz2000.contactmanager.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nawaz2000.contactmanager.dao.UserStorageService;
import com.nawaz2000.contactmanager.entity.User;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserStorageService userStorageService;
	
	@GetMapping("/register")
	public String getRegistrationPage(Model model) {
		model.addAttribute("newUser", new User());
		System.out.println("get registration page");
		return "register";
	}
	
	@PostMapping("/register")
	public String saveNewUser(Model model, @ModelAttribute(name = "newUser") User user) throws IOException {
		System.out.println("save new user");
		System.out.println("----------> New User:" + user);
		if (userStorageService.findByEmail(user.getEmail()).isEmpty())
			userStorageService.saveUser(null, user);
		return "redirect:/login";
	}
	
}
