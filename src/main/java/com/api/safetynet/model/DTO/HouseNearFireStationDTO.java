package com.api.safetynet.model.DTO;

import java.util.List;
import com.api.safetynet.model.Medication;

public class HouseNearFireStationDTO {
	
	//Attributes
	private String address;
	private List<PersonInfoWithMedicalRecordsDTO> resident;
	
	//Default constructor
	public HouseNearFireStationDTO() {}

	//Getters & Setters
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<PersonInfoWithMedicalRecordsDTO> getResident() {
		return resident;
	}

	public void setResident(List<PersonInfoWithMedicalRecordsDTO> resident) {
		this.resident = resident;
	}
	
}
