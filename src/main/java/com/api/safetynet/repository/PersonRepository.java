package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PersonRepository {
	
	@Autowired
	DataParsing dataParsing;
	
	//Attribute
	private List<Person> persons; //Initiate list
	
	//Constructor
	public PersonRepository() {
		this.persons = parseJsonPerson();//Call method at app's lunch.
	}

	//Functions
	private List<Person> parseJsonPerson(){ //Parse JSON 
		log.debug("Loading JSON Person.");
		ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<Person> personList = new ArrayList<Person>();//Create list of person and put information into Java object.
		
		//TODO: Put in DataParsing.class
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode personsNode = rootNode.get("persons"); //Extract array "persons".
			
			personList = objectMapper.readValue(personsNode.toString(), new TypeReference<List<Person>>() {});
			
		} catch (Exception e) {
			log.debug(e.getMessage());
		} 
		
		return personList;
	}
	
	public List<Person> findAllPersons(){
		log.debug("List of all person : {} ", persons);
		return persons;	
	}
	
	public Person getOnePerson(final String firstName, final String lastName){
		log.debug("Searching person : {} {} ..." , firstName, lastName);
		for(Person personFinder : persons) {
			if(personFinder.getFirstName().equals(firstName) & personFinder.getLastName().equals(lastName)) {
				log.debug("Person found : {}" , personFinder);
				return personFinder;
			}
		}
		log.debug("Cannot find person. No informations found");
		return null; //Return null if no person detected.
	}
	
	public List<Person> findAllPersonsByLastName(final String lastName){
		log.debug("Searching for all person by last name : {}", lastName);
		List<Person> personsListByLastName = new ArrayList<Person>();//Create list of person and put information into Java object.
		for (Person person : persons) {
			if(person.getLastName().toString().equals(lastName)) {
				personsListByLastName.add(person);
			}
		}
		log.debug("List of person found : {}", lastName);
		return personsListByLastName;
	}
	
	public List<Person> getAllPersonsFromSameAddress(final String address){
		log.debug("Searching all person from same addres : {}", address);
		List<Person> peronsFromSameAddress = new ArrayList<Person>();//Create list of person and put information into Java object.
		for (Person person : persons) {
			if(person.getAddress().toString().equals(address)) {
				peronsFromSameAddress.add(person);
			}
		}
		log.debug("Liste of person found : {}", peronsFromSameAddress);
		return peronsFromSameAddress;
	}
	
	public Person addPerson(final Person person) {
		log.debug("Adding person : {} ...", person);
		persons.add(person);
		log.debug("Person added");
		//TODO Persister dans le fichier JSON. //Lecture = data.json, ecriture = databis.json le temps des tests.
		return person;
	}
	
	public Boolean deletePerson(final String firstName, final String lastName) {
		log.debug("Deleting person : {} {}");
		if(persons.removeIf(person -> person.getFirstName().equals(firstName) & person.getLastName().equals(lastName))) {
			return true;
		}
		log.debug("Failed to delete person {} {}, not found", firstName, lastName);
		//TODO Persister dans le fichier JSON.
		return false;
		
	}	
	
}
