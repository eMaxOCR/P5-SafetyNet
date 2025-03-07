package com.api.safetynet.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.FirestationService;
import com.api.safetynet.service.PersonService;

@RestController
@RequestMapping("/phonealert")
public class PhoneAlertController {

	@Autowired
	private FirestationService firestationService;
	@Autowired
	private PersonService personService;
	
	@GetMapping()
	public Set<String> getPersonPhoneNumberByFirestation(@RequestParam("firestation") int stationNumber){
		
		//Attribute
		Iterable<Firestation> allFirestations;
		Iterable<Person> allPersons;
		List<Firestation> firestationByNumber = new ArrayList<>();
		List<Person> personByAddress = new ArrayList<Person>();
		Set<String> personPhoneWithoutDouble = new HashSet<>(); //HashSet to void double reg
		
	
		allFirestations = firestationService.getAllFirestation();	//Take all the fire station.
		allPersons = personService.getAllPerson();	//Take all the person
		
		//Only Take fire station by station's number. (We have addresses that we will compare to person's addresses)
		for(Firestation firestationStationFinder : allFirestations) {
			if(firestationStationFinder.getStation().equals(stationNumber)) {
				firestationByNumber.add(firestationStationFinder);
			}
		}
		
		//By fire station's address, look all person's address and put it in personByAddress.
		for (Firestation firestationFounded : firestationByNumber) {
			for (Person personAddressFinder : allPersons){
				if(personAddressFinder.getAddress().toString().equals(firestationFounded.getAddress().toString()))
				personByAddress.add(personAddressFinder);
			}
		}
		
		//Convert list of person to list of String.
		for (Person personPhoneNumberToTake : personByAddress) {
			personPhoneWithoutDouble.add(personPhoneNumberToTake.getPhone());
		}
						
		return personPhoneWithoutDouble;
	}
	
}