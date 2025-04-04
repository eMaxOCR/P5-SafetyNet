package com.api.safetynet.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter @Setter
public class PersonServedByFireStationDTO {
	
	//Attributes
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	@JsonIgnore
	private int age;
	
	//Default constructor
	public PersonServedByFireStationDTO() {}
	
}
