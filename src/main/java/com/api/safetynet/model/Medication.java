package com.api.safetynet.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Medication {
	
	//Attributes
	private String medicationName;
	private String quantityInMg;
	
	//Custom constructor to define content registration
	public Medication(String medicationString) {
		String[] parts = medicationString.split(":");
		if (parts.length == 2) {
			this.medicationName = parts[0];
			this.quantityInMg = parts[1];
		}
	}
}
