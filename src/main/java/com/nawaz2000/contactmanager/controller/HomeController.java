package com.nawaz2000.contactmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nawaz2000.contactmanager.dao.ContactDetailsDAO;
import com.nawaz2000.contactmanager.dao.UserDAO;
import com.nawaz2000.contactmanager.entity.ContactDetails;
import com.nawaz2000.contactmanager.entity.User;

@Controller
public class HomeController {
	
	@Autowired
	@Qualifier("contactDetailsDAO")
	private ContactDetailsDAO contactDAO;
	
	@GetMapping("/")
	public String getHome(Model model) {
		List<ContactDetails> contacts = contactDAO.findAll();
		model.addAttribute("allContacts", contacts);
		return "home";
	}
	
}
