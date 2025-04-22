package com.api.safetynet.testIT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.service.FirestationService;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationTestIT {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	FirestationService fs;
	
	Firestation firestation;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * Clean Database after tests.
	 * */
	@AfterEach
    void tearDown() throws Exception {
        try {
            mockMvc.perform(delete("/api/firestation/999 Avenue du chapeau/6666")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (AssertionError e) {
        }
        try {
            mockMvc.perform(delete("/api/firestation/999 Avenue du chapeau/666")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (AssertionError e) {
        }
    }
	
	/*
	 * Testing to get all fire station from database.
	 * */
	@Test
	void GetAllFirestationTestIT() throws Exception {
		
		this.mockMvc.perform(get("/api/firestation"))
			.andExpect(status().isOk());		
	}
	
	/*
	 * Testing to add fire station.
	 * */
	@Test
	void AddFirestationTestIT() throws Exception {

	    Map<String, Object> firestation = new HashMap<>();
	    firestation.put("address", "999 Avenue du chapeau"); 
	    firestation.put("station", "6666"); 

	    String firestationJSON = objectMapper.writeValueAsString(firestation); 

	    this.mockMvc.perform(post("/api/firestation")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(firestationJSON))
	            .andExpect(status().isCreated());

	    this.mockMvc.perform(get("/api/firestation/999 Avenue du chapeau/6666"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.address", is("999 Avenue du chapeau")))
	            .andExpect(jsonPath("$.station", is("6666")));
	}
	
	/*
	 * Testing to update firestation station number.
	 */
	@Test
	void updateFirestationTestIT() throws Exception {
		
		Map<String, Object> firestationToAdd = new HashMap<>();
		firestationToAdd.put("address", "999 Avenue du chapeau"); 
		firestationToAdd.put("station", "6666");


	    String firestationToAdd2 = objectMapper.writeValueAsString(firestationToAdd);

	    this.mockMvc.perform(post("/api/firestation")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(firestationToAdd2))
	            .andExpect(status().isCreated());

	    Map<String, Object> firestationToUpdate = new HashMap<>();
	    firestationToUpdate.put("address", "999 Avenue du chapeau"); 
	    firestationToUpdate.put("station", "666"); 

	    String firestationToUpdateJson = objectMapper.writeValueAsString(firestationToUpdate);

	    // Request to update
	    this.mockMvc.perform(put("/api/firestation/999 Avenue du chapeau/6666")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(firestationToUpdateJson))
	            .andExpect(status().isOk());

	    // Check if updated
	    this.mockMvc.perform(get("/api/firestation/999 Avenue du chapeau/666"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.address", is("999 Avenue du chapeau")))
	            .andExpect(jsonPath("$.station", is("666")));
	}
	
    /*
     * Testing to delete fire station.
     */
    @Test
    void deleteFirestationTestIT() throws Exception {

        // add fire station to delete.
    	Map<String, Object> firestationToDelete = new HashMap<>();
		firestationToDelete.put("address", "999 Avenue du chapeau"); 
		firestationToDelete.put("station", "666");

        String firestationToAdd = objectMapper.writeValueAsString(firestationToDelete);

        this.mockMvc.perform(post("/api/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationToAdd))
                .andExpect(status().isCreated());

        // Delete fire station
        this.mockMvc.perform(delete("/api/firestation/999 Avenue du chapeau/666")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Check if deleted
        this.mockMvc.perform(get("/api/firestation/999 Avenue du chapeau/666"))
                .andExpect(status().isNotFound());
    }
	
}
