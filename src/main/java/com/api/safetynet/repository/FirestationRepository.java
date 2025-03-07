package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Firestation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class FirestationRepository {
	
	private List<Firestation> firestations;
	
	private List<Firestation> parseJsonFirestation(){
		ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<Firestation> firestationList = new ArrayList<Firestation>();
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode firestationNode = rootNode.get("firestations"); //Extract array "firestations".
			
			firestationList = objectMapper.readValue(firestationNode.toString(), new TypeReference<List<Firestation>>() {});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return firestationList;
	}
	
	public List<Firestation> getAllFirestations(){
		return firestations;
	}
	
	public Firestation getOneFirestation(final String address, final Integer station) {
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address) & stationFinder.getStation().equals(station)) {
				return stationFinder;
			}
		}
		return null; //Return null if no person detected.
	}
	
	public Firestation addFirestation(Firestation firestation) {
		firestations.add(firestation);
		return firestation;
	}
	
	public void deleteFirestation(final String address, final Integer station) {
		firestations.removeIf(firestation -> firestation.getAddress().equals(address) & firestation.getStation().equals(station));
	}
	
	//TODO : Edit method missing
	
	//Constructor
	public FirestationRepository() {
		this.firestations = parseJsonFirestation();
		System.out.println("\"Firestation\" repository created. (" + this.firestations.size() + " found)"); //Log that the repository as been created with how many persons.
	
	}
	
	
	
	

}
