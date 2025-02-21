package com.api.safetynet.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.safetynet.model.Person;
import com.api.safetynet.repository.PersonRepository;

import lombok.Data;

@Data
@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	public Iterable<Person> getAllPerson() {
		return personRepository.findAllPersons();
	}
	
	public Person getOnePerson(final String firstName, final String lastName) {
		return personRepository.getOnePerson(firstName, lastName);
	}
	
	public Person addPerson(Person person) {
		Person addedPerson = personRepository.addPerson(person);
		return addedPerson;
	}

	public void deletePerson(final String firstName, final String lastName) {
		personRepository.deletePerson(firstName, lastName);
	}
}
