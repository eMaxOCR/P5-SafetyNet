package com.api.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter @Setter
public class Allergies {
	
	//Attribute
	private String allergyName;
	
	//Default constructor
	public Allergies(){}

}
