package com.api.safetynet.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonServedByFireStationDTO {
	
	//Attributes
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	@JsonIgnore
	private int age;
	
}
