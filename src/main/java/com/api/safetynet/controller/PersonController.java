package com.api.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.PersonService;

@RestController
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	/**
	 * Get all persons
	 * @return All persons from json's data
	 */
	@GetMapping("/person") 
	public Iterable<Person> getAllPerson(){
		return personService.getAllPerson();
	}
	
	/**
	 * Get one person
	 * @param /person/{firstName}/{lastName}
	 * @return One person from json's data
	 */
	@GetMapping("/person/{firstName}/{lastName}") 
	public Person getOnePerson(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName){
		return personService.getOnePerson(firstName, lastName);
	}
	
	/**
	 * Create/add a person
	 * @param person An object person. ("firstName", "lastName", "address", "city", "zip", "phone", "email")
	 * @return The "Person" object saved.
	 */
	@PostMapping("/person")
	public Person addPerson(@RequestBody Person person) {
		return personService.addPerson(person);
	}
	
	@PutMapping("/person/{firstName}/{lastName}")
	public Person updatePerson(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName, @RequestBody Person person) {
		Person personToEdit = personService.getOnePerson(firstName, lastName);//Take Person object that have to be edited.
		
		String address = person.getAddress();
		if(address != null) {
			personToEdit.setAddress(address);
		}
		
		String city = person.getCity();
		if(city != null) {
			personToEdit.setCity(city);
		}
		
		Integer zip = person.getZip();
		if(zip != null) {
			personToEdit.setZip(zip);
		}
		
		String phone = person.getPhone();
		if(phone != null) {
			personToEdit.setPhone(phone);
		}
		
		String email = person.getEmail();
		if(email != null) {
			personToEdit.setEmail(email);
		}
		
		return personToEdit;

	}
	
	/**
	 * Delete a person
	 * @param /person/{firstName}/{lastName}
	 */
	@DeleteMapping("/person/{firstName}/{lastName}")
	public void deletePerson(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName) {
		personService.deletePerson(firstName, lastName);
	}
	
}
