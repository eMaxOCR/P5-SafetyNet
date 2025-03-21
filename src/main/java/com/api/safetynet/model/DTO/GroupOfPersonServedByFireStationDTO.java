package com.api.safetynet.model.DTO;

import java.util.List;

public class GroupOfPersonServedByFireStationDTO {
	
	//Attributes
	private List<PersonServedByFireStationDTO> resident;
	private int adultCount;
	private int childCount;
	
	//Default constructor
	public GroupOfPersonServedByFireStationDTO() {}

	//Getters & Setters
	public List<PersonServedByFireStationDTO> getResident() {
		return resident;
	}

	public void setResident(List<PersonServedByFireStationDTO> resident) {
		this.resident = resident;
	}

	public int getAdultCount() {
		return adultCount;
	}

	public void setAdultCount(int adultCount) {
		this.adultCount = adultCount;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	
}
