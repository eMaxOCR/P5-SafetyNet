package com.api.safetynet.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MedicalRecord {
	
	private String firstName;
	
	private String lastName;
	
	private Date birthdate;
	
	private List<Medication> medications;
	
	private List<String> allergies;

}
