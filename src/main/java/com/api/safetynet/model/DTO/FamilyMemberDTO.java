package com.api.safetynet.model.DTO;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FamilyMemberDTO {
	
	//Attributes
	private String firstName;
	private String lastName;
	@JsonIgnore 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date birthdate;
	private int age;
		
	//Default constructor
	public FamilyMemberDTO() {}

}
