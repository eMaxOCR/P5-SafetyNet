package com.api.safetynet.repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
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
	
	ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
	
	protected File getJsonDataFile() {
        return new File("src/main/resources/data.json");
    }
	
	//Functions
	public List<Person> parseJsonPerson(){ //Parse JSON 
		log.debug("Loading JSON Person.");
		//ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<Person> personList = new ArrayList<Person>();//Create list of person and put information into Java object.
		
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
	
	public List<Firestation> parseJsonFirestation(){
		log.debug("Loading JSON fire station.");
		
		List<Firestation> firestationList = new ArrayList<Firestation>();
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode firestationNode = rootNode.get("firestations"); //Extract array "firestations".
			
			firestationList = objectMapper.readValue(firestationNode.toString(), new TypeReference<List<Firestation>>() {});
			
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
		return firestationList;
	}
	
	public List<MedicalRecord> parseJsonMedicalRecord(){
		ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<MedicalRecord> medicalRecordList = new ArrayList<MedicalRecord>();//Create list of person and put information into Java object.
		log.debug("Loading JSON medical records.");
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode medicalRecordsNode = rootNode.get("medicalrecords"); //Extract array "persons".
			
			medicalRecordList = objectMapper.readValue(medicalRecordsNode.toString(), new TypeReference<List<MedicalRecord>>() {});
			
		} catch (Exception e) {
			log.debug(e.getMessage());
		} 
		
		return medicalRecordList;
	}
	
	/**
	* This function is used to add and element (person, firestation or medicalrecords) into JSON file.
	 * @param node (Content all data), nodeName (the node that have to be focus eg: persons, firestations...)
	 * */
	public void addElementIntoJson(ObjectNode node, String nodeName) {
		log.debug("Requesting to add : {} into  JSON", nodeName);
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data.
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode elementNode = rootNode.get(nodeName); //Extract array "persons".
			
			ArrayNode elementArray = (ArrayNode) elementNode; 
					
			elementArray.add(node);
			
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);
			
			log.debug("{} added into JSON", nodeName);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		} 		
		
	}
	
	/**
	* This function is used to delete and element (person, firestation or medicalrecords) from JSON file.
	 * @param ids (Content Map with key value eg: firstName, "David"), nodeName (the node that have to be focus eg: persons, firestations...)
	 * */
	public void deleteElementFromJson(Map<String, String> ids, String nodeName ) {
		log.debug("Requesting to delete : {} from JSON", nodeName);
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data.
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode elementNode = rootNode.get(nodeName); //Extract array "persons".
			
			ArrayNode elementArray = (ArrayNode) elementNode; 
			
			Iterator<JsonNode> elementFinder = elementArray.elements(); //Used to check all elements one by one.
			
			while(elementFinder.hasNext()) {
				JsonNode elementToFind = elementFinder.next();
				log.info(elementToFind.toString());
				Boolean isElementMatching = true;
				
				for (Map.Entry<String, String> id : ids.entrySet()) {
				    String key = id.getKey();
				    String value = id.getValue();
				    if(!elementToFind.get(key).asText().equals(value)) {
				    	isElementMatching = false;
				    	break;
				    }
				}
				if(isElementMatching) {
					log.info("removing");
					elementFinder.remove(); 
					break;
				}
				
			}
						
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);
			
			log.debug("{} deleted from JSON", nodeName);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		} 		
		
	}
	public void addMedicalRecordIntoJson(MedicalRecord medicalRecord) {
		log.debug("Requesting to add : {} into  JSON", medicalRecord);
		
		try {
			File jsonData = new File("src/main/resources/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode medicalRecordsNode = rootNode.get("medicalrecords");

            ArrayNode medicalRecordsArray = (ArrayNode) medicalRecordsNode;

            // Create a new ObjectNode for the MedicalRecord
            ObjectNode medicalRecordNode = objectMapper.createObjectNode();
            medicalRecordNode.put("firstName", medicalRecord.getFirstName());
            medicalRecordNode.put("lastName", medicalRecord.getLastName());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            medicalRecordNode.put("birthdate", sdf.format(medicalRecord.getBirthdate()));

            // Create an ArrayNode for medications (as "name:dosage" strings)
            ArrayNode medicationsArray = objectMapper.createArrayNode();
            if (medicalRecord.getMedications() != null) {
                for (Medication medication : medicalRecord.getMedications()) {
                    String medicationString = medication.getMedicationName() + ":" + medication.getQuantityInMg();
                    medicationsArray.add(medicationString);
                }
            }
            medicalRecordNode.set("medications", medicationsArray);

            // Create an ArrayNode for allergies
            ArrayNode allergiesArray = objectMapper.createArrayNode();
            if (medicalRecord.getAllergies() != null) {
                for (String allergy : medicalRecord.getAllergies()) {
                    allergiesArray.add(allergy);
                }
            }
            medicalRecordNode.set("allergies", allergiesArray);

            // Add the new MedicalRecord node to the array
            medicalRecordsArray.add(medicalRecordNode);

            // Write the updated JSON back to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);

            log.debug("{} added into JSON", medicalRecord);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		} 		
		
	}
	
}
