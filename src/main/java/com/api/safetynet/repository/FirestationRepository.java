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
		
	/**
	* Automatically initiate list of fire station from JSON.
	 * */
	private List<Firestation> parseJsonFirestation(){ //Parse JSON 		
		return dataParsing.parseJsonFirestation();
	}
	
	/**
	* Add an fire station into JSON.
	* @param fire station
	 * */
	public void addFirestationIntoJson(Firestation firestation) {
		log.debug("Requesting to add : {} into  JSON", firestation);
		
		String nodeName = "firestations";
		
		ObjectNode firestationNode = objectMapper.createObjectNode();
		firestationNode.put("address", firestation.getAddress());
		firestationNode.put("station", firestation.getStation());
		
		dataParsing.addElementIntoJson(firestationNode, nodeName);
		
		
		log.debug("{} added into  JSON", firestation);		
	}
	
	/**
	* Delete an fire station from JSON.
	* @param firestation
	 * */
	public void deleteFirestationFromJson(Firestation firestation) {
		log.debug("Requesting to delete : {} from JSON", firestation);
		
		String nodeName = "firestations";
		
		Map<String, String> id = new HashMap<>();
		id.put("address", firestation.getAddress());
		id.put("station", firestation.getStation());
		dataParsing.deleteElementFromJson(id, nodeName);
		
		log.debug("{} deleted from JSON", firestation);		
	}
	
	/**
	* Get all fire stations.
	* @return List of fire stations.
	 * */
	public List<Firestation> getAllFirestations(){
		log.debug("List of all fire stations : {} " ,firestations);
		return firestations;
	}
	
	/**
	* Find an fire station with an address and station's number.
	* @param address, station
	* @return fire station
	 * */
	public Firestation getOneFirestationByAddressAndNumber(final String address, final String station) {
		log.debug("Searching fire station by address : {}, and station number : {} ..." , address, station);
		for(Firestation stationFinder : firestations) {
			if(stationFinder.getAddress().equals(address) & stationFinder.getStation().equals(station)) {
				log.debug("Station found : {}" , stationFinder);
				return stationFinder;
			}
		}
		log.debug("No fire station found in this address : {} and station's number : {}" , address, station); 
		return null; //Return null if no person detected.
	}
	
	/**
	* Find  fire station's number with address.
	* @param address
	* @return firestation's number
	 * */
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
	
	/**
	* Find all fire station's number that match from list of station's number.
	* @param List of station's number
	* @return List firestations
	 * */
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
	
	/**
	* Find all fire station that match one station's number.
	* @param Station's number
	* @return List firestations
	 * */
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
	
	/**
	* Add an fire station into JAVA object and into JSON.
	* @param Fire station
	* @return Fire station that has been created.
	 * */
	public Firestation addFirestation(Firestation firestation) {
		log.debug("Adding {} fire station...", firestation);
		firestations.add(firestation);
		addFirestationIntoJson(firestation);
		log.debug("{} fire station added.", firestation);
		return firestation;
	}
	
	/**
	* Update an fire station into JAVA object and into JSON.
	* First name and last name didn't change.
	* @param Address, station's number, new fire station with new informations.
	* @return Fire station that has been created.
	 * */
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
	
	/**
	* Delete an fire station from JAVA object and from JSON.
	* @param Address, station's number.
	* @return True if deleted, False if no informations found.
	 * */
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
