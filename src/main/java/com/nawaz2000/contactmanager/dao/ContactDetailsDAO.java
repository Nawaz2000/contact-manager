package com.nawaz2000.contactmanager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nawaz2000.contactmanager.entity.ContactDetails;

@Repository("contactDetailsDAO")
public interface ContactDetailsDAO extends JpaRepository<ContactDetails, Integer>{
	
	public List<ContactDetails> findByUserid(int userid);
	
}
