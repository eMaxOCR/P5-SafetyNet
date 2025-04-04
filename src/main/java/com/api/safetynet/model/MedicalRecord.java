package com.api.safetynet.model;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter @Setter
public class MedicalRecord {
	
	//Attributes
	private String firstName;
	private String lastName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date birthdate; 
	private List<Medication> medications;
	private List<String> allergies;
	
	//Default constructor
	public MedicalRecord() {}
	
}
