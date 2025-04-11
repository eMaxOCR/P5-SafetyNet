package com.api.safetynet.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.safetynet.model.DTO.ChildInfoDTO;
import com.api.safetynet.model.DTO.GroupOfPersonNearFireStationDTO;
import com.api.safetynet.model.DTO.GroupOfPersonServedByFireStationDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.service.PersonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SafetyController {
	
	
	@Autowired
	private PersonService personService;
		
	
	/**
	 * @return List of phone numbers of residents served by fire station.
	 * @param fire station number
	 */
	@GetMapping("/phonealert")
	public ResponseEntity<Set<String>> getPersonPhoneNumberByFirestation(@RequestParam("firestation") int stationNumber){
		Set<String> result = personService.getPersonPhoneNumberByFirestation(stationNumber);
		if(result.isEmpty()) {
			log.error("No station found. Number {} invalid.", stationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Phone numbers founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	
	/**
	 * @return Return list of mail of person from the city.
	 * @param City (eg: Culver)
	 */
	@GetMapping("/communityemail")
	public ResponseEntity<Set<String>> getPersonEmailByCity(@RequestParam("city") String cityToFind){
		Set<String> result = personService.getPersonEmailByCity(cityToFind);
		if(result.isEmpty()) {
			log.error("No mail found. City {} invalid.", cityToFind);
			return ResponseEntity.notFound().build();
		}
		log.info("Mails founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	
	/**
	 * @return All person's informations (Medications with posology and allergies)
	 * @param Lastname (eg: Boyd)
	 */
	@GetMapping("/personinfolastname")
	public ResponseEntity<List<PersonInfoDTO>> getPersonInformationByName(@RequestParam("lastName") String lastNameToFind){
		List<PersonInfoDTO> result = personService.getListOfPersonInformationByLastName(lastNameToFind);
		if(result.isEmpty()) {
			log.error("No person found. Lastname {} invalid.", lastNameToFind);
			return ResponseEntity.notFound().build();
		}
		log.info("Persons founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	
	/**
	 * @return Return a list of children by address with an list of family member.
	 * @param Address (eg: 947 E. Rose Dr)
	 */
	@GetMapping("/childalert")
	public ResponseEntity<List<ChildInfoDTO>> getChildInformationsByAddress(@RequestParam("address") String addressToFind){
		List<ChildInfoDTO> result = personService.getChildInformationsByAddress(addressToFind);
		if(result.isEmpty()) {
			log.error("No children found. Adress {} invalid.", addressToFind);
			return ResponseEntity.notFound().build();
		}
		log.info("Childrens founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * @return List of person who lives at this address with the fire station nearby.
	 * @param Address (eg: 947 E. Rose Dr)
	 */
	@GetMapping("/fire")
	public ResponseEntity<GroupOfPersonNearFireStationDTO> getPersonsAndFireStationNumberByAddress(@RequestParam("address") String addressToFind){
		GroupOfPersonNearFireStationDTO result = personService.getPersonsAndFireStationNumberByAddress(addressToFind);
		if(result.getResidents().isEmpty()) {
			log.error("No person found. Adress {} invalid.", addressToFind);
			return ResponseEntity.notFound().build();
		}
		log.info("Persons founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * @return Return a list of all homes served by the fire station
	 * @param List of station number (eg : /stations?2,4)
	 */
	@GetMapping("flood/stations")
	public ResponseEntity<List<HouseNearFireStationDTO>> getAllHousesServedByFireStationNumber(@RequestParam("stations") List<Integer> listOfStationNumber){
		List<HouseNearFireStationDTO> result = personService.getAllHousesServedByFireStationNumber(listOfStationNumber);
		if(result.isEmpty()) {
			log.error("No house found. List of station number {} invalid.", listOfStationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Houses founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * @return Return a list of all persons served by the fire station
	 * @param Station number (eg: 1)
	 */
	@GetMapping("/firestation")
	public ResponseEntity<GroupOfPersonServedByFireStationDTO> getAllPersonServedByFireStationNumber(@RequestParam("stationNumber") int stationNumber){
		
		GroupOfPersonServedByFireStationDTO result = personService.getAllPersonServedByFireStationNumber(stationNumber);
		
		if(result.getResidents().isEmpty()) {
			log.error("No person found. Station number {} invalid.", stationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Informations founds : {}", result);
		return ResponseEntity.ok(result);
	}
		
}