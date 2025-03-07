package com.api.safetynet.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.MedicalRecordService;
import com.api.safetynet.service.PersonService;

@RestController
@RequestMapping("/personinfolastname")
public class PersonInfoLastNameController {
	/*
	 * http://localhost:8080/personInfolastName=<lastName>
	 * Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail 
	 * et les antécédents médicaux (médicaments, posologie et allergies) de chaque habitant. 
	 * Si plusieurs personnes portent le même nom, elles doivent toutes apparaître.
	 * 
	 * */
	@Autowired
	private PersonService personService;
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	public List<Person> getPersonInformationByName(@RequestParam("lastName") String lastNameToFind){
		
		//Attribute
		Iterable<Person>allPersons;
		Iterable<MedicalRecord>allMedicalRecord;
		
		List<Person>personByLastName = new ArrayList<>();
		
		//Liste qui va contenir objet A et B ?
		
		allPersons = personService.getAllPerson();
		allMedicalRecord = medicalRecordService.getAllMedicalRecord();
		
		for (Person personLastNameFinder : allPersons) {
			if(personLastNameFinder.getLastName().toString().equals(lastNameToFind)) {
				personByLastName.add(personLastNameFinder);
			}
		}
		
		return null;
	}
	
	
	
}
