package com.nawaz2000.contactmanager.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nawaz2000.contactmanager.entity.User;

@Repository("userDAO")
public interface UserDAO extends JpaRepository<User, Integer> {
	
	public Optional<User> findByUsername(String username);
	
}
