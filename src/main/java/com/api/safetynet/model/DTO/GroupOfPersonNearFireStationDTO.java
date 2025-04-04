package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupOfPersonNearFireStationDTO {
	
	//Attributes
	private int firesStation;
	private List<PersonNearFireStationDTO> residents;
	
	//Default constructor
	public GroupOfPersonNearFireStationDTO() {}

	//Getters & Setters
	public int getFiresStation() {
		return firesStation;
	}

	public void setFiresStation(int firesStation) {
		this.firesStation = firesStation;
	}

	public List<PersonNearFireStationDTO> getResidents() {
		return residents;
	}

	public void setResidents(List<PersonNearFireStationDTO> residents) {
		this.residents = residents;
	}

}
