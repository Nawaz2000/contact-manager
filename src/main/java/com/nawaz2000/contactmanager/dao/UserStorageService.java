package com.nawaz2000.contactmanager.dao;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nawaz2000.contactmanager.entity.User;

@Service
public class UserStorageService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(MultipartFile file, User user) throws IOException {
//		if (file != null && (user.getImage() != null))
//			user.setImage(file.getBytes());
//		if(user.getId() != null)
//			user.setImage(file.getBytes());
		if(file != null)
			user.setImage(file.getBytes());
		if(file.isEmpty()) {
			System.out.println("from database before update: " + userRepository.findById(user.getId()).get().getImage().length);
			user.setImage(userRepository.findById(user.getId()).get().getImage());
			
		}
		return userRepository.save(user);
	}
	
	public Optional<User> findUserById(Integer id) {
		if (id != null) {
			return userRepository.findById(id);
		}else
			return null;
	}
	
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
	public Optional<User> findByEmail(String email){
		return userRepository.findByEmail(email);
	}

	public void deleteById(Integer id) {
		userRepository.deleteById(id);
		
	}
	
}
