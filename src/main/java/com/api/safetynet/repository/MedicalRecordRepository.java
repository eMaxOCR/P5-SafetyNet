package com.api.safetynet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class MedicalRecordRepository {
	
	//Attribute
	private List<MedicalRecord> medicalRecords;
	
	//Constructor
	public MedicalRecordRepository() {
		this.medicalRecords = parseJsonMedicalRecord();//Call method at app's lunch.
		System.out.println("\"MedicalRecord\" repository created. (" + this.medicalRecords.size() + " found)"); //Log that the repository as been created with how many persons.
	}
	
	//Functions
	private List<MedicalRecord> parseJsonMedicalRecord(){
		ObjectMapper objectMapper = new ObjectMapper();//Create Jackon's object mapper
		
		List<MedicalRecord> medicalRecordList = new ArrayList<MedicalRecord>();//Create list of person and put information into Java object.
		
		try {
			File jsonData = new File("src/main/resources/data.json"); //Indicate where is the data to parse
			JsonNode rootNode = objectMapper.readTree(jsonData); //Read all content of json data.
			JsonNode medicalRecordsNode = rootNode.get("medicalrecords"); //Extract array "persons".
			
			medicalRecordList = objectMapper.readValue(medicalRecordsNode.toString(), new TypeReference<List<MedicalRecord>>() {});
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return medicalRecordList;
	}
	
	public List<MedicalRecord> getAllMedicalRecords(){
		return medicalRecords;
	}
	
	public MedicalRecord getOneMedicalRecord(final String firstName, final String lastName){
		for(MedicalRecord medicalRecordFinder : medicalRecords) {
			if(medicalRecordFinder.getFirstName().equals(firstName) & medicalRecordFinder.getLastName().equals(lastName)) {
				return medicalRecordFinder;
			}
		}
		return null; //Return null if no medicalRecord detected.
	}
	
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecords.add(medicalRecord);
		return medicalRecord;
	}
	
	public void deleteMedicalRecord(final String firstName, final String lastName) {
		medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) & medicalRecord.getLastName().equals(lastName));
	}

}
