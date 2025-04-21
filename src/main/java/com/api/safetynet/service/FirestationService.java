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
	
	public Firestation getOneFirestationByAddressAndStationNumber(final String address, final String station){
		log.debug("Get one fire station");
		return firestationRepository.getOneFirestationByAddressAndNumber(address, station);
	}
	
	public String getFirestationNumberByAddress(final String address){
		return firestationRepository.getFirestationNumberByAddress(address);
	}
	
	public List<Firestation> getAllFirestationByStationNumberList(final List<String> listOfStationNumber){
		return firestationRepository.getAllFirestationByStationNumberList(listOfStationNumber);
	}
	
	public List<Firestation> getAllFirestationByStationNumber(final String stationNumber){
		return firestationRepository.getAllFirestationByStationNumber(stationNumber);
	}
	
	public Firestation addFirestation (final Firestation firestation) {
		Firestation addedFirestation = firestationRepository.addFirestation(firestation);
		return addedFirestation;
	}
	
	
	public Firestation updateFirestation (String address, String station, Firestation firestation) {
		return firestationRepository.updateFirestation(address, station, firestation);
	}
	
	
	public Boolean deleteFirestation(final String address, final String station) {
		return firestationRepository.deleteFirestation(address, station);
	}
	
		
}
