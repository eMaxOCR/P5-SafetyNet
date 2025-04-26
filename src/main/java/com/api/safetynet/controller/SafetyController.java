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
	 * Return a list of phone numbers of residents served by the fire station. 
	 * Used to send emergency text messages to specific households.
	 * @return List of phone numbers of residents served by fire station.
	 * @param fire station number
	 */
	@GetMapping("/phonealert")
	public ResponseEntity<Set<String>> getPersonPhoneNumberByFirestation(@RequestParam("firestation") String stationNumber){
		Set<String> result = personService.getPersonPhoneNumberByFirestation(stationNumber);
		if(result.isEmpty()) {
			log.error("No station found. Number {} invalid.", stationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Phone numbers founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	
	/**
	 * Return the email addresses of all the residents in the city.
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
	 * Return the name, address, age, email address, and medical history (medications with dosage, and allergies) of each resident. 
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
	 * Return a list of children (any individual aged 18 or under) living at that address. 
	 * The list include the first and last name of each child, their age, and a list of other household members. 
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
	 * Return a list of all households served by the fire station. 
	 * Grouped people by address it include the name, phone number, and age of the residents, and list their medical history (medications with dosage, and allergies) next to each name.
	 * @return Return a list of all homes served by the fire station
	 * @param List of station number (eg : /stations?2,4)
	 */
	@GetMapping("flood/stations")
	public ResponseEntity<List<HouseNearFireStationDTO>> getAllHousesServedByFireStationNumber(@RequestParam("stations") List<String> listOfStationNumber){
		List<HouseNearFireStationDTO> result = personService.getAllHousesServedByFireStationNumber(listOfStationNumber);
		if(result.isEmpty()) {
			log.error("No house found. List of station number {} invalid.", listOfStationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Houses founds : {}", result);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Return a list of people covered by the corresponding fire station. 
	 * So, if the station number = 1, it should return the 1  residents covered by station number 1. 
	 * The list  include the following specific information: first name, last name, address, phone number. 
	 * In addition, 2 provide a count of the number of adults and the number of children (any individual aged 18 or under) in the served area.
	 * @return Return a list of all persons served by the fire station
	 * @param Station number (eg: 1)
	 */
	@GetMapping("/firestation")
	public ResponseEntity<GroupOfPersonServedByFireStationDTO> getAllPersonServedByFireStationNumber(@RequestParam("stationNumber") String stationNumber){
		
		GroupOfPersonServedByFireStationDTO result = personService.getAllPersonServedByFireStationNumber(stationNumber);
		
		if(result.getResidents().isEmpty()) {
			log.error("No person found. Station number {} invalid.", stationNumber);
			return ResponseEntity.notFound().build();
		}
		log.info("Informations founds : {}", result);
		return ResponseEntity.ok(result);
	}
		
}