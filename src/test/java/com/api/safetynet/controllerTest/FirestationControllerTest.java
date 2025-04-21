package com.api.safetynet.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
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
import com.api.safetynet.controller.FirestationController;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.service.FirestationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {
		
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	FirestationService fs;
	
		
	Firestation firestation1 = new Firestation();
	Firestation firestation2 = new Firestation();
	
	@BeforeAll
	public void setUpFireStation() {
		//Put temporary information for first fire station
		firestation1.setAddress("123 rue table");
		firestation1.setStation("1");
			
		//Put temporary information for first fire station
		firestation2.setAddress("456 rue chaise");
		firestation2.setStation("2");
	}
	
	@Test	
	public void getAllFirestationTest() throws Exception {
		//Test getting all fire station.
		//GIVEN
		List<Firestation> listOfFireStations = new ArrayList<>(); //Simulate fake list with two fire station.
		listOfFireStations.add(firestation1); //adding first fire station
		listOfFireStations.add(firestation2); //adding second fire station
		
		//WHEN
		when(fs.getAllFirestation()).thenReturn(listOfFireStations); //When we use getAllFirestation() method, it should return a list of fire station.
		
		//THEN
		this.mockMvc.perform(get("/api/firestation")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())													//Check that response OK
			.andExpect(jsonPath("$.[*].address").isNotEmpty())	
			.andExpect(jsonPath("$.[*].station").isNotEmpty());
	}
	
	@Test
	public void getOneFirestationTest() throws Exception {
		//GIVEN
		//FireStation in setUpFireStation()
		
		//WHEN
		when(fs.getOneFirestationByAddressAndStationNumber(firestation1.getAddress(), firestation1.getStation())).thenReturn(firestation1);
		
		//THEN
		this.mockMvc.perform(get("/api/firestation/"+firestation1.getAddress()+"/"+ firestation1.getStation())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.address").value(firestation1.getAddress()))
		.andExpect(jsonPath("$.station").value(firestation1.getStation()));
		
	}
	
	@Test
	public void getOneFirestationFailedTest() throws Exception {
		//GIVEN
		//FireStation in setUpFireStation()
		
		//WHEN
		when(fs.getOneFirestationByAddressAndStationNumber(firestation1.getAddress(), firestation1.getStation())).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(get("/api/firestation/"+firestation1.getAddress()+"/"+ firestation1.getStation()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound()); 
		
	}
	
	@Test
	public void addFirestationTest() throws Exception {
		//GIVEN
		Firestation thirdFireStation = new Firestation();
		thirdFireStation.setAddress("188 Rue National");
		thirdFireStation.setStation("3");
		
		//WHEN
		when(fs.addFirestation(any(Firestation.class))).thenReturn(thirdFireStation);
		
		//THEN
		this.mockMvc.perform(post("/api/firestation")
				.content(objectMapper.writeValueAsString(thirdFireStation)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.address").value(thirdFireStation.getAddress()))
		.andExpect(jsonPath("$.station").value(thirdFireStation.getStation()));
		
	}


	@Test
	public void updateFirestationTest() throws Exception {
		//GIVEN
		Firestation newFirestationInformation = new Firestation();
		newFirestationInformation.setAddress("123 rue table");
		newFirestationInformation.setStation("4");
		
		//WHEN
		when(fs.updateFirestation(firestation1.getAddress(), firestation1.getStation(), newFirestationInformation)).thenReturn(newFirestationInformation);
		
		//THEN
		this.mockMvc.perform(put("/api/firestation/"+firestation1.getAddress()+"/"+firestation1.getStation())
				.content(objectMapper.writeValueAsString(newFirestationInformation)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.address").value(newFirestationInformation.getAddress()))
		.andExpect(jsonPath("$.station").value(newFirestationInformation.getStation()));
		
	}
	
	@Test
	public void updateFirestationFailedTest() throws Exception {
		//GIVEN
		Firestation fakeFirestation = new Firestation();
		
		//WHEN
		when(fs.updateFirestation(firestation1.getAddress(), firestation1.getStation(), firestation1)).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(put("/api/person/"+firestation1.getAddress()+"/"+firestation1.getStation())
				.content(objectMapper.writeValueAsString(fakeFirestation)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void deleteFirestationTest() throws Exception {
		//GIVEN
		//FireStation in setUpFireStation()
		
		//WHEN
		when(fs.deleteFirestation(firestation1.getAddress(), firestation1.getStation())).thenReturn(true);
		
		//THEN
		this.mockMvc.perform(delete("/api/firestation/"+firestation1.getAddress()+"/"+ firestation1.getStation()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteFirestationFailedTest() throws Exception {
		//GIVEN
		//FireStation in setUpFireStation()
		
		//WHEN
		when(fs.deleteFirestation(firestation1.getAddress(), firestation1.getStation())).thenReturn(false);
		
		//THEN
		this.mockMvc.perform(delete("/api/firestation/"+firestation1.getAddress()+"/"+ firestation1.getStation()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	
}
