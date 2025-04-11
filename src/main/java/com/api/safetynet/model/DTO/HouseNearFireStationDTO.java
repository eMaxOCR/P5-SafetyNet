package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseNearFireStationDTO {
	
	//Attributes
	private String address;
	private List<PersonInfoWithMedicalRecordsDTO> resident;
	
}
