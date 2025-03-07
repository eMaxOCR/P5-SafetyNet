package com.api.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.safetynet.model.MedicalRecord;
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
	
	public void deleteMedicalRecord(final String firstName, final String lastName) {
		medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
	}
}
