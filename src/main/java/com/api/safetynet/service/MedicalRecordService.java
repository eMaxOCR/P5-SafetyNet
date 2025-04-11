package com.api.safetynet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.repository.MedicalRecordRepository;

import lombok.Data;

@Data
@Service
public class MedicalRecordService {
	
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	
	public Iterable<MedicalRecord> getAllMedicalRecord(){
		return medicalRecordRepository.getAllMedicalRecords();
	}
	
	public MedicalRecord getOneMedicalRecord(final String firstName, final String lastName) {
		return medicalRecordRepository.getOneMedicalRecord(firstName, lastName);
	}
	
	public MedicalRecord addMedicalRecord (MedicalRecord medicalRecord) {
		MedicalRecord addedMedicalRecord = medicalRecordRepository.addMedicalRecord(medicalRecord);
		return addedMedicalRecord;
	}
	
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
		
		return medicalRecordToEdit;
	}
	
	public Boolean deleteMedicalRecord(final String firstName, final String lastName) {
		return medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
	}
	
	public List<MedicalRecord> getChildMedicalRecord() {
		return medicalRecordRepository.getChildMedicalRecord();
	}
	
}
