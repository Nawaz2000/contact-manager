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
	private UserRepository docRepository;
	
	public User saveUser(MultipartFile file, User user) throws IOException {		
		if (!file.isEmpty())
			user.setImage(file.getBytes());
		return docRepository.save(user);
	}
	
	public Optional<User> findUserById(Integer id) {
		if (id != null) {
			return docRepository.findById(id);
		}else
			return null;
	}
	
	public Optional<User> findByUsername(String username){
		if (username != null)
			return docRepository.findByUsername(username);
		return Optional.empty();
	}
	
}
