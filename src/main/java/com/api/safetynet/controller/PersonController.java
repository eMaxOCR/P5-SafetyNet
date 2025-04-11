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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("Persons founds : {}", result);
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
			log.error("Person {} {} cannot be found.", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
		log.info("Person found : {}", result);
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
		
		log.info("Person {} added.", result);
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
		
		Person personToEdit = personService.updatePerson(firstName, lastName, person);
		
		if(personToEdit == null) {
			log.error("Person {} {} cannot be found.", firstName, lastName);
			return ResponseEntity.notFound().build(); //build force when no body.
		}
		
		log.info("Person {} {} updated.", personToEdit.getFirstName(), personToEdit.getLastName());
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
			log.error("Person {} {} cannot be found.", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
		log.info("Person {} {} deleted.", firstName, lastName);
		return ResponseEntity.noContent().build();
	}
	
}
