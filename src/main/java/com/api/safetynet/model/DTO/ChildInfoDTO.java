package com.api.safetynet.model.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
	public ChildInfoDTO() {
		familyMember = new ArrayList<>();
	}
	
}
