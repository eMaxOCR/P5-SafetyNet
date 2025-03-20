package com.api.safetynet.model.DTO;

import java.util.List;
import com.api.safetynet.model.Medication;

public class PersonNearFireStationDTO {
	
	//Attributes
	private String lastName;
	private String phoneNumber;
	private int age;
	private int fireStationNumber;
	private List<Medication> medications;
	private List<String> allergies;
	
	//Default constructor
	public PersonNearFireStationDTO() {}
	
	//Getters & Setters
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	public int getFireStationNumber() {
		return fireStationNumber;
	}

	public void setFireStationNumber(int fireStationNumber) {
		this.fireStationNumber = fireStationNumber;
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
