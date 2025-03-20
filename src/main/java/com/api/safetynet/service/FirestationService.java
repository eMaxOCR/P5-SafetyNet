package com.api.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.repository.FirestationRepository;
import lombok.Data;

@Data
@Service
public class FirestationService {
	
	@Autowired
	private FirestationRepository firestationRepository;
	
	public Iterable<Firestation> getAllFirestation(){
		return firestationRepository.getAllFirestations();
	}
	
	public Firestation getOneFirestationByAddressAndStationNumber(final String address, final Integer station){
		return firestationRepository.getOneFirestationByAddressAndNumber(address, station);
	}
	
	public int getFirestationNumberByAddress(final String address){
		return firestationRepository.getFirestationNumberByAddress(address);
	}
	
	public List<Firestation> getAllFirestationByStationNumber(final List<Integer> listOfStationNumber){
		return firestationRepository.getAllFirestationByStationNumber(listOfStationNumber);
	}
	
	public Firestation addFirestation (final Firestation firestation) {
		Firestation addedFirestation = firestationRepository.addFirestation(firestation);
		return addedFirestation;
	}
	
	public void deleteFirestation(final String address, final Integer station) {
		firestationRepository.deleteFirestation(address, station);
	}
	
//	/**
//	 * @param List of station's number. (eg 1, 3)
//	 * @return Return an list of fire station by their number.
//	 */
//	public List<Firestation> getListOfFirestationByStationNumber(List<Integer> listStationNumber){
//		
//		List<Firestation> listOfFireStations = new ArrayList<Firestation>();
//		
//		for(Integer stationNumner : listStationNumber) {
//			listOfFireStations.add(getAllFirestationByStationNumber(stationNumner));
//		}
//		
//		return listOfFireStations;
//	
//	}
		
}
