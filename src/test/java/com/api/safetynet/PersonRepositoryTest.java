package com.api.safetynet;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.api.safetynet.model.Person;
import com.api.safetynet.repository.PersonRepository;


public class PersonRepositoryTest {
	
	PersonRepository pr = new PersonRepository();
	
	//private Person person = new Person();
	
	@Test
	public void getOnePersonTest() {
		//GIVEN
		String personFirstName = "John";
		String personLastName = "Boyd";
		
		//WHEN
		Person person = pr.getOnePerson(personFirstName, personLastName);
		
		//THEN
		assertThat(person.getFirstName()).isEqualTo(personFirstName);
		assertThat(person.getLastName()).isEqualTo(personLastName);
	}
	
	@Test
	public void getAllPersonsTest() {
		//GIVEN
		List<Person> findAllPersons = new ArrayList<>();
		//WHEN
		findAllPersons = pr.findAllPersons();
		//THEN
		assertThat(findAllPersons).isNotNull();
	}
	
	@Test
	public void getAllPersonsFailedTest() {
		//GIVEN
		List<Person> findAllPersons = new ArrayList<>();
		//WHEN
		findAllPersons = null;
		//THEN
		assertThat(findAllPersons).isNull();
	}
	
	@Test
	public void getAllPersonsByLastNameTest() {
		//GIVEN
		List<Person> findAllPersons = new ArrayList<>();
		//WHEN
		findAllPersons = null;
		//THEN
		assertThat(findAllPersons).isNull();
	}

}
