package com.api.safetynet.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.service.FirestationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/firestation")
@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;
	
	/**
	 * Get all fire stations
	 * @return All fire stations from json's data
	 */
	@GetMapping()
	public ResponseEntity<Iterable<Firestation>> getAllFirestation(){
		Iterable<Firestation> result = firestationService.getAllFirestation();
		log.info("All fire stations found : {} ",result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Get one fire station
	 * @param {address}/{station}
	 * @return One fire station from json's data
	 */
	@GetMapping("/{address}/{station}") 
	public ResponseEntity<Firestation> getOneFirestation(@PathVariable("address") final String address,@PathVariable("station") final Integer station){
		Firestation result = firestationService.getOneFirestationByAddressAndStationNumber(address, station);
		if(result == null) {
			log.error("No fire station found with the arguments: {} and {}. Element didn't exist.", address, station);
			return ResponseEntity.notFound().build();
		}
		log.info("One fire stations found: {}", result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Create/add a fire station
	 * @param  An object fire station. ("address", "station")
	 * @return The "fire station" object that has been saved.
	 */
	@PostMapping()
	public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
		Firestation result = firestationService.addFirestation(firestation);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{address}/{fireStation}")
                .buildAndExpand(result.getAddress(),result.getStation())
                .toUri(); //Sent URI to header.
		log.info("Fire station: {} created.", result);
		return ResponseEntity.created(location).body(result); 
	}
	
	/**
	 * Update information for a fire station by searching it address and station's number.
	 * Editable attribute : "firestation".
	 * @param {address}/{station}
	 * @return fire station with updated informations.
	 */
	@PutMapping("/{address}/{station}")
	public ResponseEntity<Firestation> updateFirestation(@PathVariable("address") final String address, @PathVariable("station") final int station, @RequestBody Firestation firestation) {
		Firestation firestationToEdit = firestationService.updateFirestation(address, station, firestation);
		
		if(firestationToEdit == null) {
			log.error("Can't update fire station informations with the arguments: {} {}. Element didn't exist", address, station);
			return ResponseEntity.notFound().build(); //build force when no body.
		}

		log.info("Fire station: {} updated.", firestationToEdit);
		return ResponseEntity.ok(firestationToEdit);
	}
	
	/**
	 * Delete a fire station
	 * @param {address}/{station}
	 */
	@DeleteMapping("/{address}/{station}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable("address") final String address,@PathVariable("station") final Integer station) {
		Boolean hasBeenDeleted = firestationService.deleteFirestation(address, station);
		if(!hasBeenDeleted) {
			log.error("Can't delete fire station with the arguments: {} {}. Element didn't exist", address,  station);
			return ResponseEntity.notFound().build(); //TODO Check with mentor about code number
		}
		log.info("Fire station: {} {} deleted.", address,  station);
		return ResponseEntity.noContent().build();
	}		
}