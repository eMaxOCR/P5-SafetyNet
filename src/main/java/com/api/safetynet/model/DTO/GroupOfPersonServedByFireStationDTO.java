package com.api.safetynet.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter @Setter
public class GroupOfPersonServedByFireStationDTO {
	
	//Attributes
	private List<PersonServedByFireStationDTO> residents;
	private int adultCount;
	private int childCount;
	
	//Default constructor
	public GroupOfPersonServedByFireStationDTO() {}
	
}
