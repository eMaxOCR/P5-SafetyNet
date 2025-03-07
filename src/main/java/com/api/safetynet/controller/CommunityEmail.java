package com.api.safetynet.controller;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.safetynet.model.Person;
import com.api.safetynet.service.PersonService;

@RestController
@RequestMapping("/communityemail")
public class CommunityEmail {
	
	@Autowired
	private PersonService personService;
	
	@GetMapping()
	public Set<String> getPersonEmailByCity(@RequestParam("city") String cityToFind){
		
		//Attribute
		Iterable<Person>allPersons;
		Set<String>personEmail = new HashSet<>();
		
		allPersons = personService.getAllPerson(); //Find all persons.
		
		//Only take person that their city is equal to city from parameter
		for (Person personCityFinder : allPersons) {
			if(personCityFinder.getCity().toString().equals(cityToFind)) {
				personEmail.add(personCityFinder.getEmail());
			}
		}
		
		return personEmail;
	}
	

}
