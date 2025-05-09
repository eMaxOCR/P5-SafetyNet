package com.api.safetynet.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MedicalRecordRepository {
	
	@Autowired
	private DataParsing dataParsing;
	
	//Attribute
	private List<MedicalRecord> medicalRecords;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		this.medicalRecords = parseJsonMedicalRecord();//Call method at app's lunch.
		log.info("\"MedicalRecord\" repository created. {} found)", this.medicalRecords.size()); //Log that the repository as been created with how many persons.
	}
	
	/**
	* Automatically initiate list of medical record from JSON.
	 * */
	private List<MedicalRecord> parseJsonMedicalRecord(){
		return dataParsing.parseJsonMedicalRecord();
	}
	
	/**
	* Find all medical record from JSON.
	* @return List of medical records.
	 * */
	public List<MedicalRecord> getAllMedicalRecords(){
		log.debug("List of all medical records : {} ", medicalRecords);
		return medicalRecords;
	}
	
	/**
	* Find one medical record.
	* @param first name, last name (String)
	* @return One medical record.
	 * */
	public MedicalRecord getOneMedicalRecord(final String firstName, final String lastName){
		log.debug("Searching for {} {}'s medical record.");
		for(MedicalRecord medicalRecordFinder : medicalRecords) {
			if(medicalRecordFinder.getFirstName().equals(firstName) & medicalRecordFinder.getLastName().equals(lastName)) {
				log.debug("{} {}'s medical record found : {}", firstName, lastName, medicalRecordFinder);
				return medicalRecordFinder;
			}
		}
		log.debug("No medical record found");
		return null; //Return null if no medicalRecord detected.
	}
	
	/**
	* Find all child's medical record.
	* @return List of child's medical record.
	 * */
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
		log.debug("Liste of children's medical records found : {}", childMedicalRecord);
		return childMedicalRecord;
	}
	
	/**
	* Add an medical record into JAVA object and into JSON.
	* @param Medical record
	* @return Medical record that has been created.
	 * */
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		log.debug("Adding medical record : {}", medicalRecord);
		medicalRecords.add(medicalRecord);
		addMedicalRecordIntoJson(medicalRecord);
		log.debug("Medical record added");
		return medicalRecord;
	}
	
	/**
	* Add an medical record into JSON.
	* @param Medical record
	 * */
	public void addMedicalRecordIntoJson(MedicalRecord medicalRecord) {
		log.debug("Adding medical record to JSON: {}", medicalRecord);
		
		dataParsing.addMedicalRecordIntoJson(medicalRecord);
		
		log.debug("Medical record added to JSON");
	}
	
	/**
	* Update an medical record into JAVA object and into JSON.
	* First name and last name didn't change.
	* @param First name, last name, new medical record with new informations.
	* @return Fire station that has been created.
	 * */
	public MedicalRecord updateMedicalRecord (String firstName, String lastName, MedicalRecord medicalRecord) {
		MedicalRecord medicalRecordToEdit = getOneMedicalRecord(firstName, lastName); //Take MedicalRecord object that have to be updated.
		
		Date birthdate = medicalRecord.getBirthdate();
		if(birthdate != null) {
			medicalRecordToEdit.setBirthdate(birthdate);
		}
		
		List<Medication> medication = medicalRecord.getMedications();
		if(medication != null) {
			medicalRecordToEdit.setMedications(medication);
		}
		
		List<String> allergies = medicalRecord.getAllergies();
		if(allergies != null) {
			medicalRecordToEdit.setAllergies(allergies);
		}
		
		deleteMedicalRecordFromJson(medicalRecordToEdit);
		addMedicalRecordIntoJson(medicalRecord);
		
		return medicalRecordToEdit;
	}
	
	/**
	* Delete an medical record from JAVA object and from JSON.
	* @param First name, last name.
	* @return True if deleted, False if no informations found.
	 * */
	public Boolean deleteMedicalRecord(final String firstName, final String lastName) {
		log.debug("Deleting : {} {}'s medical record...", firstName, lastName);
		MedicalRecord medicalRecordToDelete = getOneMedicalRecord(firstName, lastName);
		if(medicalRecordToDelete != null) {
			deleteMedicalRecordFromJson(medicalRecordToDelete);
			if(medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) & medicalRecord.getLastName().equals(lastName))) {
				log.debug("{} {}'s medical record deleted !");
				return true;
			}
		}
		log.debug("Cannot delete {} {}'s medical record. Not found");
		return false;
	}
	
	/**
	* Delete an medical record from JSON.
	* @param medical record
	 * */
	public void deleteMedicalRecordFromJson(MedicalRecord merdicalRecord) {
		log.debug("Requesting to delete : {} from JSON", merdicalRecord);
		
		String nodeName = "medicalrecords";
		
		Map<String, String> id = new HashMap<>();
		id.put("firstName", merdicalRecord.getFirstName());
		id.put("lastName", merdicalRecord.getLastName());
		
		dataParsing.deleteElementFromJson(id, nodeName);
		
		log.debug("{} deleted from JSON", merdicalRecord);	
	}

}
