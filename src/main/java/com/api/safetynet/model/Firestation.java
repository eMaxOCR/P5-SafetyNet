package com.api.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Firestation {
	
	//Attributes
	private String address;
	private int station;
	
	
	//Default constructor
	public Firestation() {}
	
	
	//Getters & setters
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

}
