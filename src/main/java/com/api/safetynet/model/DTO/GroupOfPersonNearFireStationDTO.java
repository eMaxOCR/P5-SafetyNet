package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupOfPersonNearFireStationDTO {
	
	//Attributes
	private String firesStation;
	private List<PersonNearFireStationDTO> residents;
}
