package com.api.safetynet.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
	public ResponseEntity<Iterable<MedicalRecord>> getAllMedicalRecord(){
		Iterable<MedicalRecord> result = medicalRecordService.getAllMedicalRecord();
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Get one medical record
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 * @return One medical record from json's data
	 */
	@GetMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> getOneMedicalRecord(@PathVariable("firstName") final String firstName,@PathVariable("lastName") final String lastName) {
		MedicalRecord result = medicalRecordService.getOneMedicalRecord(firstName, lastName);
		if(result == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Create/add a medical record
	 * @param An object MedicalRecord. ("firstName", "lastName", "birthdate", "medications":["medicationName : weightInMg"], "allergies":["allergyName"])
	 * @return The "MedicalRecord" object saved.
	 */
	@PostMapping()
	public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalrecord) {
		MedicalRecord result = medicalRecordService.addMedicalRecord(medicalrecord);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{firstName}/{lastName}")
                .buildAndExpand(result.getFirstName(),result.getLastName())
                .toUri(); //Sent URI to header.
		
		return ResponseEntity.created(location).body(result);
		
	}
	
	/**
	 * Can update information for a medical record by searching firstname and lastname.
	 * Editable attributes : "birthdate", "medications", "allergies".
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 * @return Update information.
	 */
	
	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> updatePerson(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName, @RequestBody MedicalRecord medicalRecord) {
		MedicalRecord medicalRecordToEdit = medicalRecordService.getOneMedicalRecord(firstName, lastName); //Take MedicalRecord object that have to be updated.
		
		if(medicalRecordToEdit == null) {
			return ResponseEntity.notFound().build();
		}
		
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
		
		return ResponseEntity.ok(medicalRecordToEdit);
	}
	
	/**
	 * Delete a medical record
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 */
	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName) {
		Boolean hasBeenDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if(!hasBeenDeleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
		
	}
	
}
