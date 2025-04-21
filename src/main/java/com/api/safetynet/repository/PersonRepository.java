package com.api.safetynet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PersonRepository {
	
	@Autowired
	private DataParsing dataParsing;
	
	//Attribute
	private List<Person> persons = new ArrayList<>(); //Initiate list
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@PostConstruct
	public void init() {
       this.persons = parseJsonPerson();//Initialize person list after Spring has injected dependencies.
       log.info("\"Person\" repository initialized. {} found)", this.persons.size());
    }
	
	//Functions
	private List<Person> parseJsonPerson(){ //Parse JSON 		
		return dataParsing.parseJsonPerson();
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
		return null; 
	}
	
	public List<Person> findAllPersons(){
		log.debug("List of all person : {} ", persons);
		return persons;	
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
	
	
	public void addPersonIntoJson(Person person) {
		log.debug("Requesting to add : {} into  JSON", person);
		
		String nodeName = "persons";
		
		ObjectNode personNode = objectMapper.createObjectNode();
		personNode.put("firstName", person.getFirstName());
		personNode.put("lastName", person.getLastName());
		personNode.put("address", person.getAddress());
		personNode.put("city", person.getCity());
		personNode.put("phone", person.getPhone());
		personNode.put("email", person.getEmail());
		
		dataParsing.addElementIntoJson(personNode, nodeName);
		
		log.debug("{} added into  JSON", person);		
	}
	
	public void deletePersonFromJson(Person person) {
		log.debug("Requesting to delete : {} from JSON", person);
		
		String nodeName = "persons";
		
		Map<String, String> id = new HashMap<>();
		id.put("firstName", person.getFirstName());
		id.put("lastName", person.getLastName());
		
		dataParsing.deleteElementFromJson(id, nodeName);
		
		log.debug("{} deleted from JSON", person);	
	}
	
	public Person addPerson(final Person person) {
		log.debug("Adding person : {} ...", person);
		persons.add(person);
		addPersonIntoJson(person);
		log.debug("Person added");

		return person;
	}
	
	public Person updatePerson(String firstName, String lastName, Person person) {
		Person personToEdit = getOnePerson(firstName, lastName);//Take Person object that have to be updated.
		
		if(personToEdit != null) {
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
			
			//update database
			deletePersonFromJson(personToEdit);
			addPersonIntoJson(person);
			
			log.debug("Person {} {} updated.", personToEdit.getFirstName(), personToEdit.getLastName());
			return personToEdit;
		}
		return null;
	}
	
	public Boolean deletePerson(final String firstName, final String lastName) {
		log.debug("Deleting person : {} {}");
		Person personToDelete = getOnePerson(firstName, lastName);
		if(personToDelete != null) {
			deletePersonFromJson(personToDelete);
			if(persons.removeIf(person -> person.getFirstName().equals(firstName) & person.getLastName().equals(lastName))) {
				log.debug("person deleted !");
				return true;
			}
		}
		log.debug("Failed to delete person {} {}, not found", firstName, lastName);
		return false;
		
	}	
	
}
