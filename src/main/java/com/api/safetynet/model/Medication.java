package com.api.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
	
	//Attributes
	private String medicationName;
	private String quantityInMg;
	
	//Default constructor
	public Medication() {}
	
	//Custom constructor to define content registration
	public Medication(String medicationString) {
		String[] parts = medicationString.split(":");
		if (parts.length == 2) {
			this.medicationName = parts[0];
			this.quantityInMg = parts[1];
		}
	}
	
	//Getters & setters
	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getQuantityInMg() {
		return quantityInMg;
	}

	public void setQuantityInMg(String quantityInMg) {
		this.quantityInMg = quantityInMg;
	}

}
