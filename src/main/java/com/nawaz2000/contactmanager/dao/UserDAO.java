package com.nawaz2000.contactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nawaz2000.contactmanager.entity.User;

public interface UserDAO extends JpaRepository<User, Integer> {

}
