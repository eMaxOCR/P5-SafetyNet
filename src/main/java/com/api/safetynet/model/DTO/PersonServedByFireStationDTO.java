package com.api.safetynet.model.DTO;

public class PersonServedByFireStationDTO {
	
	//Attributes
	private String firsttName;
	private String lastName;
	private String address;
	private String phoneNumber;
	//@JsonIgnore
	private int age;
	
	//Default constructor
	public PersonServedByFireStationDTO() {}

	//Getters & Setters
	public String getFirsttName() {
		return firsttName;
	}

	public void setFirsttName(String firsttName) {
		this.firsttName = firsttName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
