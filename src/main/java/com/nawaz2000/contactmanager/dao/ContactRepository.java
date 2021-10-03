package com.nawaz2000.contactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nawaz2000.contactmanager.entity.ContactDetails;

@Repository
public interface ContactRepository extends JpaRepository<ContactDetails, Integer> {
	
	public List<ContactDetails> findByUseridOrderByNameAsc(int userid);
	
	@Query(value = "select * from contactdetails where lower(name) LIKE %?1%  and userid = ?2 order by name"
			,nativeQuery = true)
	public Page<ContactDetails> search(String search, int userId, Pageable pageable);
	
	public List<ContactDetails> findByFavouriteOrderByNameAsc(String favourite);
	
	@Query(value="select * from contactdetails where userid = ?1 order by name", nativeQuery = true)
	public Page<ContactDetails> findByUserid(int userId, Pageable pageable);
	
}
