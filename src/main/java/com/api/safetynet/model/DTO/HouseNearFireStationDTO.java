package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HouseNearFireStationDTO {
	
	//Attributes
	private String address;
	private List<PersonInfoWithMedicalRecordsDTO> resident;
	
	//Default constructor
	public HouseNearFireStationDTO() {}
	
}
