package com.nawaz2000.contactmanager.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nawaz2000.contactmanager.dao.ContactStorageService;
import com.nawaz2000.contactmanager.dao.UserStorageService;
import com.nawaz2000.contactmanager.entity.ContactDetails;
import com.nawaz2000.contactmanager.entity.User;

@Controller
public class HomeController {
	
//	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images";
	public static String uploadDirectory;
	private static int currUserId;
	private static User pUser;
	private List<ContactDetails> favourites;
	private String search;
	
	
	@Autowired
	private UserStorageService userStorageService;
	
	@Autowired
	private ContactStorageService contactStorageService;
	
	@GetMapping({"/","/home"})
	public String getHome(Model model, HttpSession session, @RequestParam(name = "page",defaultValue = "0") Integer page) throws IOException {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currUsername = "";
		
		if (principal instanceof UserDetails) {
		  currUsername = ((UserDetails)principal).getUsername();
		} else {
		  currUsername = principal.toString();
		}
		
		System.out.println("---------------------------> currUser: " + currUsername);
		
		if (!currUsername.equals("anonymousUser")) {
			
			User currUser = userStorageService.findByUsername(currUsername).get();
//			User currUser = docStorageService.findByUsername(currUsername).get();
			
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
		Page<ContactDetails> contacts = contactStorageService.findByUserid(currUserId, pagable);
		
		System.out.println("\n\n\n\n\n\n===========Pringting found contacts:");
		System.out.println("Page: " + page);
		
		List<ContactDetails> contacts2 = contactStorageService.findByUseridOrderByNameAsc(currUserId);
		model.addAttribute("allContacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		model.addAttribute("totalContacts", contacts2.size());
		
		
		// adding favourites to model
		favourites = contactStorageService.findByFavouriteOrderByNameAsc("1");
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
		
		Pageable pageable = PageRequest.of(page, 3);
		Page<ContactDetails> searchResult = contactStorageService.search(search, currUserId, pageable);
		System.out.println("\n\n========================>> Search results");
		System.out.println("total pages: " + searchResult.getTotalPages());
		for (ContactDetails c : searchResult)
			System.out.println(c);
		model.addAttribute("searchResults", searchResult);
		model.addAttribute("favourites", favourites);		
		
		System.out.println("\n\n\n\n\n\n===========Pagination sql:");
		System.out.println("Page: " + page + " UId: " + currUserId);
		
		List<ContactDetails> contacts2 = contactStorageService.findByUseridOrderByNameAsc(currUserId);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("totalContacts", contacts2.size());
		
		
		return "search-results";
	}
	
	public static String getImgData(byte[] byteData) {
	    return Base64.encodeBase64String(byteData);
	}
	
	@GetMapping("/profile")
	public String getProfile(Model model) {		
		model.addAttribute("profile", pUser);
		model.addAttribute("favourites", favourites);
		model.addAttribute("imgUtil", getImgData(pUser.getImage()));
		return "profile";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute(name = "profile") User user,
			@RequestParam(name = "image12", required = false) MultipartFile multipartFile) throws IOException {
		System.out.println(user);
//		uploadDirectory = new File(".").getCanonicalPath() + "/src/main/resources/static/images";
		
		User retrievedUser = userStorageService.saveUser(multipartFile, user);
				
		userStorageService.saveUser(multipartFile, retrievedUser);
		
		System.out.println(user);
		
		return "redirect:/home";
	}
	
	
	@GetMapping("/deleteContact")
	public String deleteContact(@RequestParam(name = "param") String param) {
		contactStorageService.deleteById(Integer.parseInt(param));
		return "redirect:/home";
	}
	
	@GetMapping("/updateContact")
	public String updateContact(@RequestParam(name = "param") String param, Model model) {
		ContactDetails contact = contactStorageService.findById(Integer.parseInt(param));
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
							@RequestParam(name = "image12", required = false) MultipartFile multipartFile) throws IOException {
		
		newContact.setImage(multipartFile.getBytes());
		newContact.setUserid(currUserId);
		ContactDetails savedUser = contactStorageService.save(newContact);
		
		
		
//		contactDAO.save(retrievedUser);
		
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
