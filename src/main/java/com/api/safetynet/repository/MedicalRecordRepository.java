package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.MedicalRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MedicalRecordRepository {
	
	//Attribute
	private List<MedicalRecord> medicalRecords;
	
	//Constructor
	public MedicalRecordRepository() {
		this.medicalRecords = parseJsonMedicalRecord();//Call method at app's lunch.
		log.info("\"MedicalRecord\" repository created. {} found)", this.medicalRecords.size()); //Log that the repository as been created with how many persons.
	}
	
	//Functions
	private List<MedicalRecord> parseJsonMedicalRecord(){
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
	
	public List<MedicalRecord> getAllMedicalRecords(){
		log.debug("List of all medical records : {} ");
		return medicalRecords;
	}
	
	public MedicalRecord getOneMedicalRecord(final String firstName, final String lastName){
		log.debug("Searching for {} {}'s medical record.");
		for(MedicalRecord medicalRecordFinder : medicalRecords) {
			if(medicalRecordFinder.getFirstName().equals(firstName) & medicalRecordFinder.getLastName().equals(lastName)) {
				log.debug("{} {}'s medical record found : {}", medicalRecordFinder);
				return medicalRecordFinder;
			}
		}
		log.debug("No medical record found");
		return null; //Return null if no medicalRecord detected.
	}
	
	public List<MedicalRecord> getChildMedicalRecord() {
		log.debug("Searching for children's medical records.");
		Calendar calendar = Calendar.getInstance(); //Create calendar
		List<MedicalRecord> childMedicalRecord = new ArrayList<>();
		
		Date todayDate = calendar.getTime(); //Set today's time
		calendar.add(Calendar.YEAR, -18); //Take off 18 years from calendar. 
		Date todayMinus18years = calendar.getTime(); //Set time minus 18 years 
		
		for(MedicalRecord medicalRecord : medicalRecords) {
			if(medicalRecord.getBirthdate().after(todayMinus18years) && medicalRecord.getBirthdate().before(todayDate)) {
				childMedicalRecord.add(medicalRecord);
			}
		}
		log.debug("Liste of children's medical records found : ", childMedicalRecord);
		return childMedicalRecord;
	}
		
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		log.debug("Adding medical record : {}", medicalRecord);
		medicalRecords.add(medicalRecord);
		log.debug("Medical record added");
		return medicalRecord;
	}
	
	public Boolean deleteMedicalRecord(final String firstName, final String lastName) {
		log.debug("Deleting : {} {}'s medical record...", firstName, lastName);
		if(medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) & medicalRecord.getLastName().equals(lastName))) {
			log.debug("{} {}'s medical record deleted !");
			return true;
		}
		log.debug("Cannot delete {} {}'s medical record. Not found");
		return false;
	}

}
