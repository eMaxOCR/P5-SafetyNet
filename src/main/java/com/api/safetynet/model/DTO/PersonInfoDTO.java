package com.api.safetynet.model.DTO;

import java.util.Date;
import java.util.List;
import com.api.safetynet.model.Medication;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonInfoDTO {
	
	//Attributes
	private String lastName;
	private String address;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date birthdate;
	private String email;
	private List<Medication> medications;
	private List<String> allergies;
	
	//Default constructor
	public PersonInfoDTO() {}
	
}
