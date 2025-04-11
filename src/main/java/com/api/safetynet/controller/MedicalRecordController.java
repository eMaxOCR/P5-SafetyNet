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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("All medical records found: {}", result);
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
			log.error("{} {}'s medical record not found.", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
		log.info("{} {}'s medical record : {}", firstName, lastName, result);
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
		log.info("{} {}'s medical record created.", result);
		return ResponseEntity.created(location).body(result);
		
	}
	
	/**
	 * Can update information for a medical record by searching firstname and lastname.
	 * Editable attributes : "birthdate", "medications", "allergies".
	 * @param /api/medicalrecord/{firstName}/{lastName}
	 * @return Update information.
	 */
	
	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("firstName") final String firstName, @PathVariable("lastName") final String lastName, @RequestBody MedicalRecord medicalRecord) {
		MedicalRecord medicalRecordToEdit = medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord); //Take MedicalRecord object that have to be updated.
		
		if(medicalRecordToEdit == null) {
			log.error("Can't update {} {}'s medical record. Not found.", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
				
		log.info("{} {}'s medical record updated.", firstName, lastName);
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
			log.error("Can't delete {} {}'s medical record. Not found.", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
		log.info("{} {}'s medical record has been deleted.", firstName, lastName);
		return ResponseEntity.noContent().build();
		
	}
	
}
