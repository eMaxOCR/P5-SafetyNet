package com.api.safetynet.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.api.safetynet.controller.MedicalRecordController;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {
		
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	MedicalRecordService ms;
	
		
	MedicalRecord medicalRecord1 = new MedicalRecord();
	MedicalRecord medicalRecord2 = new MedicalRecord();
	
	@BeforeAll
	public void setUpMedicalRecord() {
		//Put temporary information for first medical record
		medicalRecord1.setFirstName("David");
		medicalRecord1.setLastName("Plamon");
		medicalRecord1.setBirthdate(new Date(11/03/1997));
		
		Medication medication1 = new Medication();
		medication1.setMedicationName("paracetamol ");
		medication1.setQuantityInMg("500mg");
		List<Medication> medicationList = new ArrayList<>();
		medicationList.add(medication1);
		medicalRecord1.setMedications(medicationList);
		
		List<String> allergiesList = new ArrayList<>();
		allergiesList.add("Pollen");
		medicalRecord1.setAllergies(allergiesList);
			
		//Put temporary information for second medical record
		medicalRecord2.setFirstName("Christophe");
		medicalRecord2.setLastName("Frauth");
		medicalRecord2.setBirthdate(new Date(11/03/1998));
		medicalRecord2.setMedications(null);
		medicalRecord2.setAllergies(null);
		
		Medication medication2 = new Medication();
		medication2.setMedicationName("paracetamol ");
		medication2.setQuantityInMg("500mg");
		List<Medication> medicationList2 = new ArrayList<>();
		medicationList2.add(medication2);
		medicalRecord1.setMedications(medicationList2);
		
		List<String> allergiesList2 = new ArrayList<>();
		allergiesList2.add("Pollen");
		medicalRecord1.setAllergies(allergiesList2);
	}
	
	@Test	
	public void getAllMedicalRecordTest() throws Exception {
		//Test getting all medical records.
		//GIVEN
		List<MedicalRecord> listOfMedicalRecords = new ArrayList<>(); //Simulate fake list with two fake medical record.
		listOfMedicalRecords.add(medicalRecord1); //adding first medicalrecord
		listOfMedicalRecords.add(medicalRecord2); //adding second medicalrecord
		
		//WHEN
		when(ms.getAllMedicalRecord()).thenReturn(listOfMedicalRecords); //When we use getAllPerson() method, it should return a list of persons.
		
		//THEN
		this.mockMvc.perform(get("/api/medicalrecord")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())													//Check that response OK
			.andExpect(jsonPath("$.[*].firstName").isNotEmpty())	
			.andExpect(jsonPath("$.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].birthdate").isNotEmpty())
			.andExpect(jsonPath("$.[*].medications").isNotEmpty())
			.andExpect(jsonPath("$.[*].allergies").isNotEmpty());
	}
	
	@Test
	public void getOneMedicalRecordTest() throws Exception {
		//GIVEN
		//person in setUpMedicalRecord()
		
		//WHEN
		when(ms.getOneMedicalRecord(medicalRecord1.getFirstName(), medicalRecord1.getLastName())).thenReturn(medicalRecord1);
		
		//THEN
		this.mockMvc.perform(get("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value(medicalRecord1.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(medicalRecord1.getLastName()))
		.andExpect(jsonPath("$.birthdate").isNotEmpty())
		.andExpect(jsonPath("$.medications").isNotEmpty())
		.andExpect(jsonPath("$.allergies").isNotEmpty());
		
	}
	
	@Test
	public void getOneMedicalRecordFailedTest() throws Exception {
		//GIVEN
		//person in setUpMedicalRecord()
		
		//WHEN
		when(ms.getOneMedicalRecord(medicalRecord1.getFirstName(), medicalRecord1.getLastName())).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(get("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void addMedicalRecordTest() throws Exception {
		//GIVEN
		MedicalRecord thirdMedicalRecord = new MedicalRecord();
		
		thirdMedicalRecord.setFirstName("Florian");
		thirdMedicalRecord.setLastName("Paulard");
		thirdMedicalRecord.setBirthdate(new Date(11/03/200));
		
		Medication medication1 = new Medication();
		medication1.setMedicationName("paracetamol ");
		medication1.setQuantityInMg("1000mg");
		List<Medication> medicationList = new ArrayList<>();
		medicationList.add(medication1);
		thirdMedicalRecord.setMedications(null);
		
		List<String> allergiesList = new ArrayList<>();
		allergiesList.add("Cats");
		thirdMedicalRecord.setAllergies(allergiesList);
		
		//WHEN
		when(ms.addMedicalRecord(any(MedicalRecord.class))).thenReturn(thirdMedicalRecord);
		
		//THEN
		this.mockMvc.perform(post("/api/medicalrecord")
				.content(objectMapper.writeValueAsString(thirdMedicalRecord)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.firstName").value(thirdMedicalRecord.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(thirdMedicalRecord.getLastName()))
		.andExpect(jsonPath("$.birthdate").isNotEmpty())
		.andExpect(jsonPath("$.medications").value(thirdMedicalRecord.getMedications()))
		.andExpect(jsonPath("$.allergies").value(thirdMedicalRecord.getAllergies()));
		
	}
	

	@Test
	public void updateMedicalRecordTest() throws Exception {
		//GIVEN
		MedicalRecord fourthMedicalRecord = new MedicalRecord();
				
		fourthMedicalRecord.setFirstName("David");
		fourthMedicalRecord.setLastName("Plamon");
		fourthMedicalRecord.setBirthdate(new Date(11/03/2000));
				
		Medication medication4 = new Medication();
		medication4.setMedicationName("pills");
		medication4.setQuantityInMg("1kg");
		List<Medication> medicationList = new ArrayList<>();
		medicationList.add(medication4);
		fourthMedicalRecord.setMedications(null);
				
		List<String> allergiesList3 = new ArrayList<>();
		allergiesList3.add("salts");
		fourthMedicalRecord.setAllergies(allergiesList3);
		
		//WHEN
		when(ms.updateMedicalRecord(medicalRecord1.getFirstName(), medicalRecord1.getLastName(),fourthMedicalRecord )).thenReturn(fourthMedicalRecord);//TODO check
		
		//THEN
		this.mockMvc.perform(put("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName())
				.content(objectMapper.writeValueAsString(fourthMedicalRecord)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value(fourthMedicalRecord.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(fourthMedicalRecord.getLastName()))
		.andExpect(jsonPath("$.birthdate").isNotEmpty())
		.andExpect(jsonPath("$.medications").value(fourthMedicalRecord.getMedications()))
		.andExpect(jsonPath("$.allergies").value(fourthMedicalRecord.getAllergies()));
		
	}
	
	@Test
	public void updateMedicalRecordFailedTest() throws Exception {
		//GIVEN
		MedicalRecord fakeMedicalRecord = new MedicalRecord();
		
		//WHEN
		when(ms.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class))).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(put("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName())
				.content(objectMapper.writeValueAsString(fakeMedicalRecord)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void deleteMedicalRecord() throws Exception {
		//GIVEN
		//person in setUpMedicalRecord()
		
		//WHEN
		when(ms.deleteMedicalRecord(medicalRecord1.getFirstName(), medicalRecord1.getLastName())).thenReturn(true);
		
		//THEN
		this.mockMvc.perform(delete("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteMedicalRecordFailedTest() throws Exception {
		//GIVEN
		//person in setUpMedicalRecord()
		
		//WHEN
		when(ms.deleteMedicalRecord(medicalRecord1.getFirstName(), medicalRecord1.getLastName())).thenReturn(false);
		
		//THEN
		this.mockMvc.perform(delete("/api/medicalrecord/"+medicalRecord1.getFirstName()+"/"+medicalRecord1.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	
}
