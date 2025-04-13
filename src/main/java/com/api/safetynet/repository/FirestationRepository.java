package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FirestationRepository {
	
	private List<Firestation> firestations;
	
	ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
	
	private List<Firestation> parseJsonFirestation(){
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
	
	public void addFirestationIntoJson(Firestation firestation) {
		log.debug("Requesting to add : {} into  JSON", firestation);
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data.
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode firestationsNode = rootNode.get("firestations"); //Extract array "persons".
			
			ArrayNode firestationsArray = (ArrayNode) firestationsNode; 
			
			ObjectNode newFirestation = objectMapper.createObjectNode();
			newFirestation.put("address", firestation.getAddress());
			newFirestation.put("station", firestation.getStation());
			
			firestationsArray.add(newFirestation);
			
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonData, rootNode);
			
			log.debug("{} added into JSON", firestation);
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			
		}	
		
	}
	
	public List<Firestation> getAllFirestations(){
		log.debug("List of all fire stations : {} " ,firestations);
		return firestations;
	}
	
	public Firestation getOneFirestationByAddressAndNumber(final String address, final Integer station) {
		log.debug("Searching fire station by address : {}, and station number : {} ..." , address, station);
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address) & stationFinder.getStation() == station) {
				log.debug("Station found : " , stationFinder);
				return stationFinder;
			}
		}
		log.debug("No fire station found in this address : {} and station's number : {}" , address, station); 
		return null; //Return null if no person detected.
	}
	
	public int getFirestationNumberByAddress(final String address) {
		log.debug("Searching fire station by address : {} ..." , address);
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address)) {
				log.debug("Station found : {}",stationFinder.getStation());
				return stationFinder.getStation();
			}
		}
		log.debug("No fire station found with this address : {}", address);
		return 0; //Return 0 if no station detected.
	}
	
	public List<Firestation> getAllFirestationByStationNumberList(final List<Integer> listOfStationNumber) {
		List<Firestation> listOfFireStation = new ArrayList<Firestation>();
		
		log.debug("Searching fire station by station's number : {} ...", listOfStationNumber);
		for(Integer stationNumber : listOfStationNumber) {
			
			for(Firestation stationFinder : getAllFirestations()) {
				if(stationFinder.getStation() == stationNumber) {
					listOfFireStation.add(stationFinder) ;
				}
			}
		}
		log.debug("List of fire station found : {}", listOfFireStation);	
		return listOfFireStation;
	}
	
	public List<Firestation> getAllFirestationByStationNumber(final int stationNumber) {
		log.debug("Searching all fire stations by station's number : {} ...", stationNumber);
		List<Firestation> listOfFireStation = new ArrayList<Firestation>();
			for(Firestation stationFinder : getAllFirestations()) {
				if(stationFinder.getStation() == stationNumber) {
					listOfFireStation.add(stationFinder) ;
				}
		}
		log.debug("List of fire station found : {}", listOfFireStation);	
		return listOfFireStation;
	}
	
	public Firestation addFirestation(Firestation firestation) {
		log.debug("Adding {} fire station...", firestation);
		firestations.add(firestation);
		addFirestationIntoJson(firestation);
		log.debug("{} fire station added.", firestation);
		return firestation;
	}
	
	public Boolean deleteFirestation(final String address, final Integer station) {
		log.debug("Deleting fire station number {} from {} ...", station, address);
		if(firestations.removeIf(firestation -> firestation.getAddress().equals(address) & firestation.getStation() == station)) {
			log.debug("Fire station deleted !");
			return true;
		}
		log.debug("Failed to delete fire station number {} from {} ...", station, address);
		return false;
	}
		
	//Constructor
	public FirestationRepository() {
		this.firestations = parseJsonFirestation();
		log.info("\"Firestation\" repository created. {} found)", this.firestations.size()); //Log that the repository as been created with how many persons.
	
	}
	
	
	
	

}
