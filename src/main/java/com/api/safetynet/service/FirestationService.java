package com.api.safetynet.service;

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
	
	public Firestation getOneFirestation(final String address, final Integer station){
		return firestationRepository.getOneFirestation(address, station);
	}
	
	public Firestation addFirestation (Firestation firestation) {
		Firestation addedFirestation = firestationRepository.addFirestation(firestation);
		return addedFirestation;
	}
	
	public void deleteFirestation(final String address, final Integer station) {
		firestationRepository.deleteFirestation(address, station);
	}
}
