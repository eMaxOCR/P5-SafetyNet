package com.api.safetynet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;

@Repository
public class DataParsing { 
	//TODO What to do ? @Mentor
	private List<Person> persons;
	
	private List<Firestation> firestations;
	
	private List<MedicalRecord> medicalRecords;
	
}
