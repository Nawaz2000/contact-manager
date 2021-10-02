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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private List<ContactDetails> favourites;
	
	@Autowired
	@Qualifier("contactDetailsDAO")
	private ContactDetailsDAO contactDAO;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@GetMapping({"/","/home"})
	public String getHome(Model model, HttpSession session, @RequestParam(name = "page",defaultValue = "0") Integer page) {
		
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
		
		
		// adding pagination
		Pageable pagable = PageRequest.of(page, 3);
		
		System.out.println("\n\n\n\n\n\n===========Pagination sql:");
		System.out.println("Page: " + page + " UId: " + currUserId);
		Page<ContactDetails> contacts = contactDAO.findByUserid(currUserId, pagable);
		
		System.out.println("\n\n\n\n\n\n===========Pringting found contacts:");
		System.out.println("Page: " + page);
		
		List<ContactDetails> contacts2 = contactDAO.findByUseridOrderByNameAsc(currUserId);
		model.addAttribute("allContacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		model.addAttribute("totalContacts", contacts2.size());
		
		
		// adding favourites to model
		favourites = contactDAO.findByFavouriteOrderByNameAsc("1");
//		System.out.println("\n\n\nFavourites result\n\n");
//		for (ContactDetails c : favourites)
//			System.out.println(c);
		model.addAttribute("favourites", favourites);
		
		return "home";
	}
	
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "param") String search,
						Model model,
						@RequestParam(name = "page",defaultValue = "0") Integer page) {
		List<ContactDetails> searchResult = contactDAO.search(search);
		System.out.println("\n\n========================>> Search results");
		for (ContactDetails c : searchResult)
			System.out.println(c);
		model.addAttribute("searchResults", searchResult);
		model.addAttribute("favourites", favourites);
		
		
		Pageable pagable = PageRequest.of(page, 3);
		
		System.out.println("\n\n\n\n\n\n===========Pagination sql:");
		System.out.println("Page: " + page + " UId: " + currUserId);
		Page<ContactDetails> contacts = contactDAO.findByUserid(currUserId, pagable);
		
		System.out.println("\n\n\n\n\n\n===========Pringting found contacts:");
		System.out.println("Page: " + page);
		
		List<ContactDetails> contacts2 = contactDAO.findByUseridOrderByNameAsc(currUserId);
		model.addAttribute("allContacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		model.addAttribute("totalContacts", contacts2.size());
		
		
		return "search-results";
	}
	
	@GetMapping("/profile")
	public String getProfile(Model model) {		
		model.addAttribute("profile", pUser);
		model.addAttribute("favourites", favourites);
		return "profile";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute(name = "profile") User user,
			@RequestParam(name = "image12", required = false) MultipartFile multipartFile) {
		System.out.println(user);
		
		
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		user.setImage("https://bootdey.com/img/Content/avatar/avatar3.png");
//		user.setUserid(currUserId);
		System.out.println("\n\n=======> For add/update" + user);
		User savedUser = userDAO.save(user);
		
		
		
		User retrievedUser = userDAO.findById(savedUser.getId()).get();
		
		if (!user.getImage().isEmpty()) {
			fileName = "images/" + savedUser.getId() + ".jpg";
			System.out.println("---------------> Image name: " + fileName);
			retrievedUser.setImage(fileName);
		}
		
		
		retrievedUser.setImage(null);
		
		userDAO.save(retrievedUser);
		
		System.out.println(user);
		
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
		model.addAttribute("favourites", favourites);
		return "update-contact";
	}
	
	@GetMapping("/addContact")
	public String showAddContact(Model model) {
		model.addAttribute("newContact", new ContactDetails());
		model.addAttribute("favourites", favourites);
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
