package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupOfPersonServedByFireStationDTO {
	
	//Attributes
	private List<PersonServedByFireStationDTO> residents;
	private int adultCount;
	private int childCount;
	
}
