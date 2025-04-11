package com.api.safetynet.model.DTO;

import java.util.List;
import com.api.safetynet.model.Medication;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonNearFireStationDTO {
	
	//Attributes
	private String lastName;
	private String phoneNumber;
	private int age;
	private List<Medication> medications;
	private List<String> allergies;
	
}
