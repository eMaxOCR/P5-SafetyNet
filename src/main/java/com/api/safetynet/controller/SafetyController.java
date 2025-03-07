package com.api.safetynet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.service.FirestationService;
import com.api.safetynet.service.MedicalRecordService;
import com.api.safetynet.service.PersonService;

@RestController
public class SafetyController {
	
	@Autowired
	private PersonService personService;
	
	/*
	 * TODO Explain what function do
	 * */
	@GetMapping("/phonealert")
	public Set<String> getPersonPhoneNumberByFirestation(@RequestParam("firestation") int stationNumber){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonPhoneNumberByFirestation(stationNumber);
	}
	
	
	/*
	 * TODO Explain what function do
	 * */
	@GetMapping("/communityemail")
	public Set<String> getPersonEmailByCity(@RequestParam("city") String cityToFind){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonEmailByCity(cityToFind);
	}
	
	
	/*
	 * TODO Explain what function do
	 * */
	@GetMapping("/personinfolastname")
	public List<PersonInfoDTO> getPersonInformationByName(@RequestParam("lastName") String lastNameToFind){
		//TODO Add error code. 200 or 400* (IF)
		return personService.getPersonInformationByName(lastNameToFind);
	}
	
}