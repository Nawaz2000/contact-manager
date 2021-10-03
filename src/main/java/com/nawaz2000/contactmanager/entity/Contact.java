package com.nawaz2000.contactmanager.entity;

import javax.persistence.Lob;

public class Contact {
	
private int id;
	
	private String name;	
	private String gender;	
	private String email;	
	private String position;	
	private String phone;	
	private String address;
	private String image;	
	private int userid;	
	private String favourite;
	
	public Contact() {
		super();
	}

	public Contact(int id, String name, String gender, String email, String position, String phone, String address,
			String image, int userid, String favourite) {
		super();
		this.id = id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
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
		return "Contact [id=" + id + ", name=" + name + ", gender=" + gender + ", email=" + email + ", position="
				+ position + ", phone=" + phone + ", address=" + address + ", image=" + image + ", userid=" + userid
				+ ", favourite=" + favourite + "]";
	}
	
	
	
}
