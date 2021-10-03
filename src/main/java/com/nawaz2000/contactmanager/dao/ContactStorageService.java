package com.nawaz2000.contactmanager.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nawaz2000.contactmanager.entity.ContactDetails;

@Service
public class ContactStorageService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	public List<ContactDetails> findByUseridOrderByNameAsc(Integer userid){
		if (userid != null)
			return contactRepository.findByUseridOrderByNameAsc(userid);
		return new ArrayList<>();
	}
	
	public Page<ContactDetails> search(String search, Integer userId, Pageable pageable){
		if (search != null && userId != null)
			return contactRepository.search(search, userId, pageable);
		else
			return null;
	}
	
	public List<ContactDetails> findByFavouriteOrderByNameAsc(String favourite){
		if (favourite != null)
			return contactRepository.findByFavouriteOrderByNameAsc(favourite);
		return new ArrayList<>();
	}
	
	public Page<ContactDetails> findByUserid(int userId, Pageable pageable){
		return contactRepository.findByUserid(userId, pageable);
	}
	
	public void deleteById(Integer id) {
		contactRepository.deleteById(id);
	}
	
	public ContactDetails findById(Integer id) {
		return contactRepository.findById(id).get();
	}
	
	public ContactDetails save(ContactDetails newContact, MultipartFile multipartFile, Integer currUserId) throws IOException {
		newContact.setImage(multipartFile.getBytes());
		newContact.setUserid(currUserId);
		return contactRepository.save(newContact);
	}
	
}
