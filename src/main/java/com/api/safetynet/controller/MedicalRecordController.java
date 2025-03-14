package com.api.safetynet.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.service.MedicalRecordService;

@RequestMapping("/api/medicalrecord")
@RestController
public class MedicalRecordController {

	@Autowired
	private MedicalRecordService medicalRecordService;
	
	/**
	 * Get all medical records
	 * @return All medical records from json's data
	 */
	@GetMapping()
	public Iterable<MedicalRecord> getAllMedicalRecord(){
		return medicalRecordService.getAllMedicalRecord();
	}
	
	/**
	 * Get one medical record
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 * @return One medical record from json's data
	 */
	@GetMapping("/{firstName}/{lastName}")
	public MedicalRecord getOneMedicalRecord(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName) {
		return medicalRecordService.getOneMedicalRecord(firstName, lastName);
	}
	
	/**
	 * Create/add a medical record
	 * @param An object MedicalRecord. ("firstName", "lastName", "birthdate", "medications":["medicationName : weightInMg"], "allergies":["allergyName"])
	 * @return The "MedicalRecord" object saved.
	 */
	@PostMapping()
	public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalrecord) {
		return medicalRecordService.addMedicalRecord(medicalrecord);
	}
	
	/**
	 * Can update information for a medical record by searching firstname and lastname.
	 * Editable attributes : "birthdate", "medications", "allergies".
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 * @return Update information.
	 */
	
	@PutMapping("/{firstName}/{lastName}")
	public MedicalRecord updatePerson(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName, @RequestBody MedicalRecord medicalRecord) {
		MedicalRecord medicalRecordToEdit = medicalRecordService.getOneMedicalRecord(firstName, lastName); //Take MedicalRecord object that have to be updated.
		
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
	
	/**
	 * Delete a medical record
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 */
	@DeleteMapping("/{firstName}/{lastName}")
	public void deleteMedicalRecord(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName) {
		medicalRecordService.deleteMedicalRecord(firstName, lastName);
	}
	
}
