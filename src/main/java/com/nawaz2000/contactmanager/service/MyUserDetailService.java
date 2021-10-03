package com.nawaz2000.contactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nawaz2000.contactmanager.dao.UserRepository;
import com.nawaz2000.contactmanager.dao.UserStorageService;
import com.nawaz2000.contactmanager.entity.User;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("------------>Username: " + username);
		
		 User user = userRepository.findByUsername(username).get();
	        if (user == null) {
	            throw new UsernameNotFoundException(username);
	        }
	        return new MyUserPrincipal(user);
	}

}
