package com.nawaz2000.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nawaz2000.contactmanager.dao.UserDAO;
import com.nawaz2000.contactmanager.entity.User;

@Controller
public class HomeController {
	
	@Autowired
	private UserDAO userDAO;
	
	@GetMapping("/")
	public String getHome(Model model) {
		User user = userDAO.getById(1);
		model.addAttribute("qqq", user.getUsername());
		return "home";
	}
	
}
