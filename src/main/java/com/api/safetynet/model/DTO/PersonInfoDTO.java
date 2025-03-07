package com.api.safetynet.model.DTO;

import java.util.Date;
import java.util.List;
import com.api.safetynet.model.Medication;

public class PersonInfoDTO {
	
	//Attributes
	private String lastName;
	private String address;
	private Date birthdate;
	private String email;
	private List<Medication> medications;
	private List<String> allergies;
	
	//Default constructor
	public PersonInfoDTO() {}
	
	//Getters & Setters
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
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Medication> getMedications() {
		return medications;
	}
	public void setMedications(List<Medication> medications) {
		this.medications = medications;
	}
	public List<String> getAllergies() {
		return allergies;
	}
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
}
