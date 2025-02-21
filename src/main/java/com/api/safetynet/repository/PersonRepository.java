package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class PersonRepository {
	
	private List<Person> persons; //Initiate list

	private List<Person> parseJsonPerson(){ //Parse JSON 
		ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<Person> personList = new ArrayList<Person>();//Create list of person and put information into Java object.
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			
			JsonNode personsNode = rootNode.get("persons"); //Extract array "persons".
			
			personList = objectMapper.readValue(personsNode.toString(), new TypeReference<List<Person>>() {});
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return personList;
	}
	
	public List<Person> findAllPersons(){
		return persons;	
	}
	
	public Person getOnePerson(final String firstName, final String lastName){
		
		for(Person personFinder : persons) {
			if(personFinder.getFirstName().equals(firstName) & personFinder.getLastName().equals(lastName)) {
				return personFinder;
			}
		}
		return null; //Return null if no person detected.
	}
	
	public Person addPerson(Person person) {
		persons.add(person);
		return person;
	}
	
	public void deletePerson(final String firstName, final String lastName) {
		persons.removeIf(person -> person.getFirstName().equals(firstName) & person.getLastName().equals(lastName));
	}
	
	
	//Constructor
	public PersonRepository() {
		this.persons = parseJsonPerson();//Call method at app's lunch.
		System.out.println("Création du répository : \" Person \" " + this.persons.size()); //Log that the repository as been created with how many persons.
		
	}


}
