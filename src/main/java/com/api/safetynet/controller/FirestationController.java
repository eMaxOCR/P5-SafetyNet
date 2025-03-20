package com.api.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.service.FirestationService;

@RequestMapping("/api/firestation")
@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;
	
	@GetMapping()
	public Iterable<Firestation> getAllFirestation(){
		return firestationService.getAllFirestation();
	}
	
	@GetMapping("/{address}/{station}") 
	public Firestation getOneFirestation(@PathVariable("address") final String address,@PathVariable("station") final Integer station){
		return firestationService.getOneFirestationByAddressAndStationNumber(address, station);
	}
	
	@PostMapping()
	public Firestation addFirestation(@RequestBody Firestation firestation) {
		return firestationService.addFirestation(firestation);
	}
	
	@PutMapping("/{address}/{station}")
	public Firestation updateFirestation(@PathVariable("address") final String address, @PathVariable("station") final int station, @RequestBody Firestation firestation) {
		Firestation firestationToEdit = firestationService.getOneFirestationByAddressAndStationNumber(address, station);
		
		Integer stationVar = firestation.getStation();
		if(stationVar != null) {
			firestationToEdit.setStation(stationVar);
		}
		//TODO : SAVE MISSING VIA firestationService.
		return firestationToEdit;
	}
	
	@DeleteMapping("/{address}/{station}")
	public void deleteFirestation(@PathVariable("address") final String address,@PathVariable("station") final Integer station) {
		firestationService.deleteFirestation(address, station);
	}		
}