package com.api.safetynet.controller;

import java.util.ArrayList;
import java.util.List;
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
	private PersonService personService;
	
	/*
	 * http://localhost:8080/phoneAlert?firestation=<firestation_number>
	 * 
	 * Cette url doit retourner une liste des numéros de téléphone des résidents desservis 
	 * par la caserne de pompiers. Nous l'utiliserons pour envoyer des messages texte 
	 * d'urgence à des foyers spécifiques.
	 * 
	 * */
	
	@SuppressWarnings("null")
	@GetMapping()
	public List<Firestation> getPersonPhoneNumberByFirestation(@RequestParam("firestation") int stationNumber){
		
		//Attributs
		//List<String> phoneNumberNearStation;
		//Iterable<Person> personThatLivingNearStation;
		Iterable<Firestation> allFirestations;
		List<Firestation> firestationByNumber = new ArrayList<>();
		
		//Take all the firestation by station number.
		allFirestations = firestationService.getAllFirestation();
		
		//Only Take firestation by station's number.
		for(Firestation firestationStationFinder : allFirestations) {
			if(firestationStationFinder.getStation().equals(stationNumber)) {
				firestationByNumber.add(firestationStationFinder);
			}
		}
		
		return firestationByNumber;
	}
	
}