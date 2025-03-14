package com.api.safetynet.model.DTO;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChildInfoDTO {
	
	//Attributes
	private String firstName;
	private String lastName;
	@JsonIgnore 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date birthdate;
	private int age;
	private List<FamilyMemberDTO> familyMember;
		
	//Default constructor
	public ChildInfoDTO() {}

	//Getters & Setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public List<FamilyMemberDTO> getFamilyMember() {
		return familyMember;
	}

	public void setFamilyMember(List<FamilyMemberDTO> familyMember) {
		this.familyMember = familyMember;
	}
	
}
