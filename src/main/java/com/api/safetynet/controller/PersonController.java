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
import com.api.safetynet.model.Person;
import com.api.safetynet.service.PersonService;

@RequestMapping("/api/person")
@RestController
public class PersonController {
	
	
	@Autowired
	private PersonService personService;
	
	/**
	 * Get all persons
	 * @return All persons from json's data
	 */
	@GetMapping() 
	public ResponseEntity<Iterable<Person>> getAllPerson(){
		Iterable<Person> result = personService.getAllPerson();
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Get one person
	 * @param /api/person/{firstName}/{lastName}
	 * @return One person from json's data
	 */
	@GetMapping("/{firstName}/{lastName}") 
	public ResponseEntity<Person> getOnePerson(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName){
		Person result = personService.getOnePerson(firstName, lastName);
		if(result == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Create/add a person
	 * @param An object person. ("firstName", "lastName", "address", "city", "zip", "phone", "email")
	 * @return The "Person" object saved.
	 */
	@PostMapping()
	public ResponseEntity<Person> addPerson(@RequestBody Person person) { 
		Person result = personService.addPerson(person);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{firstName}/{lastName}")
                .buildAndExpand(result.getFirstName(),result.getLastName())
                .toUri(); //Sent URI to header.
		
		return ResponseEntity.created(location).body(result);
	}
	
	/**
	 * Can update information for a person by searching his firstname and lastname.
	 * Editable attributes : "address", "city", "zip", "phone" and "email".
	 * @param /api/person/{firstName}/{lastName}
	 * @return Update information.
	 */
	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<Person> updatePerson(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName, @RequestBody Person person) {
		Person personToEdit = personService.getOnePerson(firstName, lastName);//Take Person object that have to be updated.
		
		if(personToEdit == null) {
			return ResponseEntity.notFound().build(); //build force when no body.
		}
		
		String address = person.getAddress();
		if(address != null) {
			personToEdit.setAddress(address);
		}
		
		String city = person.getCity();
		if(city != null) {
			personToEdit.setCity(city);
		}
		
		int zip = person.getZip();
		if(zip != 0) {
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
		
		return ResponseEntity.ok(personToEdit);

	}
	
	/**
	 * Delete a person
	 * @param {firstName}/{lastName}
	 */
	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName) {
		Boolean hasBeenDeleted = personService.deletePerson(firstName, lastName);
		if(!hasBeenDeleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
	
}
