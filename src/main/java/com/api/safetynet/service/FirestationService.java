package com.api.safetynet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.repository.FirestationRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class FirestationService {
	
	@Autowired
	private FirestationRepository firestationRepository;
	
	public Iterable<Firestation> getAllFirestation(){
		return firestationRepository.getAllFirestations();
	}
	
	public Firestation getOneFirestationByAddressAndStationNumber(final String address, final Integer station){
		log.debug("Get one fire station");
		return firestationRepository.getOneFirestationByAddressAndNumber(address, station);
	}
	
	public int getFirestationNumberByAddress(final String address){
		return firestationRepository.getFirestationNumberByAddress(address);
	}
	
	public List<Firestation> getAllFirestationByStationNumberList(final List<Integer> listOfStationNumber){
		return firestationRepository.getAllFirestationByStationNumberList(listOfStationNumber);
	}
	
	public List<Firestation> getAllFirestationByStationNumber(final int stationNumber){
		return firestationRepository.getAllFirestationByStationNumber(stationNumber);
	}
	
	public Firestation addFirestation (final Firestation firestation) {
		Firestation addedFirestation = firestationRepository.addFirestation(firestation);
		return addedFirestation;
	}
	
	public Firestation updateFirestation (String address, Integer station, Firestation firestation) {
		Firestation firestationToEdit = getOneFirestationByAddressAndStationNumber(address, station);
		Integer stationVar = firestation.getStation();
		if(stationVar != null) {
			firestationToEdit.setStation(stationVar);
		}
		//TODO : SAVE MISSING VIA firestationService.
		return firestationToEdit;
	}
	
	
	public Boolean deleteFirestation(final String address, final Integer station) {
		return firestationRepository.deleteFirestation(address, station);
	}
	
		
}
