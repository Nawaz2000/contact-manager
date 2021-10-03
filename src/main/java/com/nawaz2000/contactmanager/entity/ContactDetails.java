package com.nawaz2000.contactmanager.entity;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "contactdetails")
public class ContactDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String gender;
	
	private String email;
	
	private String position;
	
	private String phone;
	
	private String address;
	
	@Lob
	private byte[] image;
	
	private int userid;
	
	private String favourite;

	public ContactDetails() {
		super();
	}

	public ContactDetails(String name, String gender, String email, String position, String phone, String address,
			byte[] image, int userid, String favourite) {
		super();
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.position = position;
		this.phone = phone;
		this.address = address;
		this.image = image;
		this.userid = userid;
		this.favourite = favourite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getFavourite() {
		return favourite;
	}

	public void setFavourite(String favourite) {
		this.favourite = favourite;
	}

	@Override
	public String toString() {
		return "ContactDetails [id=" + id + ", name=" + name + ", gender=" + gender + ", email=" + email + ", position="
				+ position + ", phone=" + phone + ", address=" + address + ", image=" + Arrays.toString(image)
				+ ", userid=" + userid + ", favourite=" + favourite + "]";
	}
	
	
}
