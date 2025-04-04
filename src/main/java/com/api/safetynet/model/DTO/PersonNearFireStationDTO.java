package com.api.safetynet.model.DTO;

import java.util.List;
import com.api.safetynet.model.Medication;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonNearFireStationDTO {
	
	//Attributes
	private String lastName;
	private String phoneNumber;
	private int age;
	private List<Medication> medications;
	private List<String> allergies;
	
	//Default constructor
	public PersonNearFireStationDTO() {}

}
