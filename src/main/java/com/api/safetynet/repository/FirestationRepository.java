package com.api.safetynet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.Firestation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FirestationRepository {
	
	@Autowired
	private DataParsing dataParsing;
	
	//Attribute
	private List<Firestation> firestations;
	
	ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
	
	@PostConstruct
	public void init() {
       this.firestations = parseJsonFirestation();//Initialize person list after Spring has injected dependencies.
       log.info("\"Person\" repository initialized. {} found)", this.firestations.size());
    }
		
	//Functions
	private List<Firestation> parseJsonFirestation(){ //Parse JSON 		
		return dataParsing.parseJsonFirestation();
	}
	
	public void addFirestationIntoJson(Firestation firestation) {
		log.debug("Requesting to add : {} into  JSON", firestation);
		
		String nodeName = "firestations";
		
		ObjectNode firestationNode = objectMapper.createObjectNode();
		firestationNode.put("address", firestation.getAddress());
		firestationNode.put("station", firestation.getStation());
		
		dataParsing.addElementIntoJson(firestationNode, nodeName);
		
		
		log.debug("{} added into  JSON", firestation);		
	}
	
	public void deleteFirestationFromJson(Firestation firestation) {
		log.debug("Requesting to delete : {} from JSON", firestation);
		
		String nodeName = "firestations";
		
		Map<String, String> id = new HashMap<>();
		id.put("address", firestation.getAddress());
		id.put("station", firestation.getStation());
		dataParsing.deleteElementFromJson(id, nodeName);
		
		log.debug("{} deleted from JSON", firestation);		
	}
	
	public List<Firestation> getAllFirestations(){
		log.debug("List of all fire stations : {} " ,firestations);
		return firestations;
	}
	
	public Firestation getOneFirestationByAddressAndNumber(final String address, final String station) {
		log.debug("Searching fire station by address : {}, and station number : {} ..." , address, station);
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address) & stationFinder.getStation().equals(station)) {
				log.debug("Station found : " , stationFinder);
				return stationFinder;
			}
		}
		log.debug("No fire station found in this address : {} and station's number : {}" , address, station); 
		return null; //Return null if no person detected.
	}
	
	public String getFirestationNumberByAddress(final String address) {
		log.debug("Searching fire station by address : {} ..." , address);
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address)) {
				log.debug("Station found : {}",stationFinder.getStation());
				return stationFinder.getStation();
			}
		}
		log.debug("No fire station found with this address : {}", address);
		return ""; //Return 0 if no station detected.
	}
	
	public List<Firestation> getAllFirestationByStationNumberList(final List<String> listOfStationNumber) {
		List<Firestation> listOfFireStation = new ArrayList<Firestation>();
		
		log.debug("Searching fire station by station's number : {} ...", listOfStationNumber);
		for(String stationNumber : listOfStationNumber) {
			
			for(Firestation stationFinder : getAllFirestations()) {
				if(stationFinder.getStation().equals(stationNumber)) {
					listOfFireStation.add(stationFinder) ;
				}
			}
		}
		log.debug("List of fire station found : {}", listOfFireStation);	
		return listOfFireStation;
	}
	
	public List<Firestation> getAllFirestationByStationNumber(final String stationNumber) {
		log.debug("Searching all fire stations by station's number : {} ...", stationNumber);
		List<Firestation> listOfFireStation = new ArrayList<Firestation>();
			for(Firestation stationFinder : getAllFirestations()) {
				if(stationFinder.getStation().equals(stationNumber)) {
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
	
	public Firestation updateFirestation (String address, String station, Firestation firestation) {
		Firestation firestationToEdit = getOneFirestationByAddressAndNumber(address, station);
		Firestation firestationToDelete = firestationToEdit;
		
		log.debug("Requesting update {} by {}", firestationToEdit, firestation);
				
		if(firestationToEdit != null) {
			
			String stationVar = firestation.getStation();
			if(stationVar != null) {
				deleteFirestationFromJson(firestationToDelete);
				firestationToEdit.setStation(stationVar);
			}
			
			//Update database
			addFirestationIntoJson(firestation);
			
			log.debug("Firestation {} updated ", firestation);
					
			return firestationToEdit;
			
		}
		log.error("No data found to update firestation");
		return null;
	}
	
	public Boolean deleteFirestation(final String address, final String station) {
		log.debug("Deleting fire station number {} from {} ...", address, station);
		Firestation firestationToDelete = getOneFirestationByAddressAndNumber(address, station);
		if(firestationToDelete != null) {
			deleteFirestationFromJson(firestationToDelete);
			if(firestations.removeIf(firestation -> firestation.getAddress().equals(address) & firestation.getStation().equals(station))) {
				log.debug("Fire station deleted !");
				return true;
			}
		}
			
		log.debug("Failed to delete fire station number {} from {} ...", address, station);
		return false;
	}
	
}
