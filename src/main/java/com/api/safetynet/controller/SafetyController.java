package com.api.safetynet.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.safetynet.model.Person;
import com.api.safetynet.model.DTO.ChildInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.service.PersonService;

@RestController
public class SafetyController {
	
	@Autowired
	private PersonService personService;
		
	
	/**
	 * TODO Explain what function do
	 * @param firestation number
	 */
	@GetMapping("/phonealert")
	public Set<String> getPersonPhoneNumberByFirestation(@RequestParam("firestation") int stationNumber){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonPhoneNumberByFirestation(stationNumber);
	}
	
	
	/**
	 * TODO Explain what function do
	 * @param City
	 */
	@GetMapping("/communityemail")
	public Set<String> getPersonEmailByCity(@RequestParam("city") String cityToFind){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonEmailByCity(cityToFind);
	}
	
	
	/**
	 * @return all person's informations (Medications with posology and allergies)
	 * @param Lastname
	 */
	@GetMapping("/personinfolastname")
	public List<PersonInfoDTO> getPersonInformationByName(@RequestParam("lastName") String lastNameToFind){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonInformationByName(lastNameToFind);
	}
	
	
	/**
	 * TODO Explain what function do
	 * @param Address
	 */
	@GetMapping("/childalert")
	public List<ChildInfoDTO> getChildInformationsByAddress(@RequestParam("address") String addressToFind){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getChildInformationsByAddress(addressToFind);
	}
	
	/**
	 * @return List of person who lives at this address with the fire station nearby.
	 * @param Address
	 */
	@GetMapping("/fire")
	public List<Person> getPersonsByAddressAndFireStationNumber(@RequestParam("address") String addressToFind){
		return	personService.getPersonsByAddressAndFireStationNumber(addressToFind);
	}
	
	
}