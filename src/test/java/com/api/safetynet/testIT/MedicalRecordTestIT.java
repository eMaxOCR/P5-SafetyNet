package com.api.safetynet.testIT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.MedicalRecordService;
import com.api.safetynet.service.PersonService;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordTestIT {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	MedicalRecordService ms;
	
	Person person;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * Clean Database after tests.
	 * */
	@AfterEach
    void tearDown() throws Exception {
        try {
            mockMvc.perform(delete("/api/medicalrecord/LEROY/MERLIN")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (AssertionError e) {
        }
    }
	
	/*
	 * Testing to get all medical records from database.
	 * */
	@Test
	void GetAllMedicalRecordsTestIT() throws Exception {
		
		this.mockMvc.perform(get("/api/medicalrecord"))
			.andExpect(status().isOk());		
	}
	
	/*
	 * Testing to add medical record.
	 * */
	@Test
	void AddMedicalRecordTestIT() throws Exception {

	    Map<String, Object> medicalRecord = new HashMap<>();
	    medicalRecord.put("firstName", "LEROY"); 
	    medicalRecord.put("lastName", "MERLIN"); 
	    medicalRecord.put("birthdate", "11/03/1997");
	    
	    List<Map<String, String>> medicationsList = new ArrayList<>();
	    Map<String, String> medication1 = new HashMap<>();
	    medication1.put("medicationName", "Bluepill");
	    medication1.put("quantityInMg", "10mg");
	    medicationsList.add(medication1);

	    medicalRecord.put("medications", medicationsList);
	    medicalRecord.put("allergies", Arrays.asList("cat"));

	    String medicalRecordToAdd = objectMapper.writeValueAsString(medicalRecord); 

	    this.mockMvc.perform(post("/api/medicalrecord")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(medicalRecordToAdd))
	            .andExpect(status().isCreated());

	    this.mockMvc.perform(get("/api/medicalrecord/LEROY/MERLIN"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.firstName", is("LEROY")))
	            .andExpect(jsonPath("$.lastName", is("MERLIN")))
	            .andExpect(jsonPath("$.birthdate", is("11/03/1997")))
	            
	            .andExpect(jsonPath("$.medications.[0].medicationName", is("Bluepill")))
	            .andExpect(jsonPath("$.medications.[0].quantityInMg", is("10mg")))
	            
	            .andExpect(jsonPath("$.allergies.[0]", is("cat")));
	}
	
	/*
	 * Testing to update medical Record.
	 */
	@Test
	void updateMedicalRecordTestIT() throws Exception {
	    
	    Map<String, Object> medicalRecord = new HashMap<>();
	    medicalRecord.put("firstName", "LEROY"); 
	    medicalRecord.put("lastName", "MERLIN"); 
	    medicalRecord.put("birthdate", "11/03/1997");
	    
	    List<Map<String, String>> medicationsList = new ArrayList<>();
	    Map<String, String> medication1 = new HashMap<>();
	    medication1.put("medicationName", "Bluepill");
	    medication1.put("quantityInMg", "10mg");
	    medicationsList.add(medication1);

	    medicalRecord.put("medications", medicationsList);
	    medicalRecord.put("allergies", Arrays.asList("cat"));

	    String medicalRecordToAddJson = objectMapper.writeValueAsString(medicalRecord);

	    this.mockMvc.perform(post("/api/medicalrecord")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(medicalRecordToAddJson))
	            .andExpect(status().isCreated());

	    Map<String, Object> medicalRecordUpdateInfo = new HashMap<>();
	    medicalRecordUpdateInfo.put("firstName", "LEROY"); 
	    medicalRecordUpdateInfo.put("lastName", "MERLIN"); 
	    medicalRecordUpdateInfo.put("birthdate", "11/03/1997");
	    
	    List<Map<String, String>> medicationsList2 = new ArrayList<>();
	    Map<String, String> medication2 = new HashMap<>();
	    medication2.put("medicationName", "RedPills");
	    medication2.put("quantityInMg", "100mg");
	    medicationsList2.add(medication2);

	    medicalRecordUpdateInfo.put("medications", medicationsList2);
	    medicalRecordUpdateInfo.put("allergies", Arrays.asList("dog")); 

	    String personUpdateJson = objectMapper.writeValueAsString(medicalRecordUpdateInfo);

	    // Request to update
	    this.mockMvc.perform(put("/api/medicalrecord/LEROY/MERLIN")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(personUpdateJson))
	            .andExpect(status().isOk());

	    // Check if updated
	    this.mockMvc.perform(get("/api/medicalrecord/LEROY/MERLIN"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.firstName", is("LEROY")))
		        .andExpect(jsonPath("$.lastName", is("MERLIN")))
		        .andExpect(jsonPath("$.birthdate", is("11/03/1997")))
		        
		        .andExpect(jsonPath("$.medications.[0].medicationName", is("RedPills")))
		        .andExpect(jsonPath("$.medications.[0].quantityInMg", is("100mg")))
		        
		        .andExpect(jsonPath("$.allergies.[0]", is("dog")));
	}
	
    /*
     * Testing to delete medical record.
     */
    @Test
    void deleteMedicalRecordTestIT() throws Exception {

        // add medical record to delete.
    	Map<String, Object> medicalRecord = new HashMap<>();
	    medicalRecord.put("firstName", "LEROY"); 
	    medicalRecord.put("lastName", "MERLIN"); 
	    medicalRecord.put("birthdate", "11/03/1997");

        String personToAddJson = objectMapper.writeValueAsString(medicalRecord);

        this.mockMvc.perform(post("/api/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personToAddJson))
                .andExpect(status().isCreated());

        // Delete medical record
        this.mockMvc.perform(delete("/api/medicalrecord/LEROY/MERLIN")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Check if deleted
        this.mockMvc.perform(get("/api/medicalrecord/LEROY/MERLIN"))
                .andExpect(status().isNotFound());
    }
	
}
