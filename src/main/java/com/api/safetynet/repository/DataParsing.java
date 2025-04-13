package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DataParsing { 
	
	//Attributes
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalRecords;
	
	ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
	
	//Functions
	public List<Person> parseJsonPerson(){ //Parse JSON 
		log.debug("Loading JSON Person.");
		//ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
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
	
	public void addPersonIntoJson(Person person) {
		log.debug("Requesting to add : {} into  JSON", person);
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data.
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode personsNode = rootNode.get("persons"); //Extract array "persons".
			
			ArrayNode personArray = (ArrayNode) personsNode; 
			
			ObjectNode newPerson = objectMapper.createObjectNode();
			newPerson.put("firstName", person.getFirstName());
			newPerson.put("lastName", person.getLastName());
			newPerson.put("address", person.getAddress());
			newPerson.put("city", person.getCity());
			newPerson.put("phone", person.getPhone());
			newPerson.put("email", person.getEmail());
			
			personArray.add(newPerson);
			
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);
			
			log.debug("{} added into JSON", person);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		} 		
		
	}
	
	public void deletePersonFromJson(Person person) {
		log.debug("Requesting to delete : {} from JSON", person);
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data.
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode personsNode = rootNode.get("persons"); //Extract array "persons".
			
			ArrayNode personArray = (ArrayNode) personsNode; 
			
			Iterator<JsonNode> personFinder = personArray.elements(); //Used to check all elements one by one.
			
			while(personFinder.hasNext()) {
				JsonNode personToFind = personFinder.next();
                JsonNode firstNameNode = personToFind.get("firstName");
                JsonNode lastNameNode = personToFind.get("lastName");
                
                // Verify if its the good person to delete.
                if (firstNameNode != null && firstNameNode.asText().equals(person.getFirstName()) &&
                    lastNameNode != null && lastNameNode.asText().equals(person.getLastName())) {
                	personFinder.remove(); 
                	break;
                }
			}
						
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);
			
			log.debug("{} deleted from JSON", person);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		} 		
		
	}
	
	
	
	
}
