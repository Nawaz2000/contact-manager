package com.nawaz2000.contactmanager.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nawaz2000.contactmanager.dao.ContactDetailsDAO;
import com.nawaz2000.contactmanager.dao.UserDAO;
import com.nawaz2000.contactmanager.entity.ContactDetails;
import com.nawaz2000.contactmanager.entity.User;

@Controller
public class HomeController {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images";
	private static int currUserId;
	private static User pUser;
	
	@Autowired
	@Qualifier("contactDetailsDAO")
	private ContactDetailsDAO contactDAO;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@GetMapping({"/","/home"})
	public String getHome(Model model, HttpSession session) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currUsername = "";
		
		if (principal instanceof UserDetails) {
		  currUsername = ((UserDetails)principal).getUsername();
		} else {
		  currUsername = principal.toString();
		}
		
		System.out.println("---------------------------> currUser: " + currUsername);
		
		if (!currUsername.equals("anonymousUser")) {
			
			User currUser = userDAO.findByUsername(currUsername).get();
			System.out.println("----------------------------> CurrUser id: " + currUser.getId());
			
			model.addAttribute("currUser", currUser);
			session.setAttribute("user", currUser);
			currUserId = currUser.getId();
			pUser = currUser;
			
		}
		
		
		
		List<ContactDetails> contacts = contactDAO.findByUserid(currUserId);
		model.addAttribute("allContacts", contacts);
		model.addAttribute("totalContacts", contacts.size());
		return "home";
	}
	
	@GetMapping("/deleteContact")
	public String deleteContact(@RequestParam(name = "param") String param) {
		contactDAO.deleteById(Integer.parseInt(param));
		return "redirect:/home";
	}
	
	@GetMapping("/updateContact")
	public String updateContact(@RequestParam(name = "param") String param, Model model) {
		ContactDetails contact = contactDAO.findById(Integer.parseInt(param)).get();
		System.out.println("==========> For update: " + contact);
//		model.addAttribute("newContact", new ContactDetails());
		model.addAttribute("updateContact", contact);
		return "update-contact";
	}
	
	@GetMapping("/addContact")
	public String showAddContact(Model model) {
		model.addAttribute("newContact", new ContactDetails());
		return "add-contact";
	}
	
	@PostMapping("/addContact")
	public String addContact(@ModelAttribute(name = "newContact") ContactDetails newContact,
							@RequestParam(name = "image12", required = false) MultipartFile multipartFile) {
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		newContact.setImage("https://bootdey.com/img/Content/avatar/avatar3.png");
		newContact.setUserid(currUserId);
		System.out.println("\n\n=======> For add/update" + newContact);
		ContactDetails savedUser = contactDAO.save(newContact);
		
		
		
		ContactDetails retrievedUser = contactDAO.findById(savedUser.getId()).get();
		
		if (!newContact.getImage().isEmpty()) {
			fileName = "images/" + savedUser.getId() + ".jpg";
			System.out.println("---------------> Image name: " + fileName);
			retrievedUser.setImage(fileName);
		}
		
		
		retrievedUser.setImage(null);
		
		contactDAO.save(retrievedUser);
		
		System.out.println(newContact);
		
		if (savedUser.getImage() != null) {
			System.out.println("\n\n\n\nImage not empty");
			Path fileNameAndPath = Paths.get(uploadDirectory, savedUser.getId() + ".jpg");
			try {
				Files.write(fileNameAndPath, multipartFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		
		return "redirect:/home";
	}
	
}
