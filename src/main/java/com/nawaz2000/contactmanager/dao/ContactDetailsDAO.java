package com.nawaz2000.contactmanager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nawaz2000.contactmanager.entity.ContactDetails;

@Repository("contactDetailsDAO")
public interface ContactDetailsDAO extends JpaRepository<ContactDetails, Integer>{
	
	public List<ContactDetails> findByUseridOrderByNameAsc(int userid);
	
	@Query(value = "select * from contacts.contactdetails where lower(name) LIKE %?1% order by name"
			,nativeQuery = true)
	public List<ContactDetails> search(String search);
	
	public List<ContactDetails> findByFavouriteOrderByNameAsc(String favourite);
	
}
