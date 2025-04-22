package com.api.safetynet.testIT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.PersonService;
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
public class PersonTestIT {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	PersonService ps;
	
	Person person;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * Clean Database after tests.
	 * */
	@AfterEach
    void tearDown() throws Exception {
        try {
            mockMvc.perform(delete("/api/person/LEROY/MERLIN")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (AssertionError e) {
        }
    }
	
	/*
	 * Testing to get all person from database.
	 * */
	@Test
	void GetAllPersonTestIT() throws Exception {
		
		this.mockMvc.perform(get("/api/person"))
			.andExpect(status().isOk());		
	}
	
	/*
	 * Testing to add person.
	 * */
	@Test
	void AddPersonTestIT() throws Exception {

	    Map<String, Object> person = new HashMap<>();
	    person.put("firstName", "LEROY"); 
	    person.put("lastName", "MERLIN"); 
	    person.put("address", "rue du pont");
	    person.put("city", "Lyon");
	    person.put("zip", 69000);
	    person.put("phone", "04-12-34-56-78");
	    person.put("email", "jean.dupont@test.fr");

	    String personJson = objectMapper.writeValueAsString(person); 

	    this.mockMvc.perform(post("/api/person")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(personJson))
	            .andExpect(status().isCreated());

	    this.mockMvc.perform(get("/api/person/LEROY/MERLIN"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.firstName", is("LEROY")))
	            .andExpect(jsonPath("$.lastName", is("MERLIN")))
	            .andExpect(jsonPath("$.address", is("rue du pont")))
	            .andExpect(jsonPath("$.city", is("Lyon")))
	            .andExpect(jsonPath("$.zip", is(69000)))
	            .andExpect(jsonPath("$.phone", is("04-12-34-56-78")))
	            .andExpect(jsonPath("$.email", is("jean.dupont@test.fr")));
	}
	
	/*
	 * Testing to update person.
	 */
	@Test
	void updatePersonTestIT() throws Exception {

	    Map<String, Object> personToAdd = new HashMap<>();
	    personToAdd.put("firstName", "LEROY");
	    personToAdd.put("lastName", "MERLIN");
	    personToAdd.put("address", "rue du bricolage");
	    personToAdd.put("city", "Paris");
	    personToAdd.put("zip", 75000);
	    personToAdd.put("phone", "04-56-98-96-63");
	    personToAdd.put("email", "paris@leroymerlin.fr");

	    String personToAddJson = objectMapper.writeValueAsString(personToAdd);

	    this.mockMvc.perform(post("/api/person")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(personToAddJson))
	            .andExpect(status().isCreated());

	    Map<String, Object> personToUpdate = new HashMap<>();
	    personToUpdate.put("firstName", "LEROY");
	    personToUpdate.put("lastName", "MERLIN");
	    personToUpdate.put("address", "rue du test");
	    personToUpdate.put("city", "Lyon"); 
	    personToUpdate.put("zip", 69000); 
	    personToUpdate.put("phone", "01-23-45-67-89");
	    personToUpdate.put("email", "lyon@leroymerlin.fr"); 

	    String personUpdateJson = objectMapper.writeValueAsString(personToUpdate);

	    // Request to update
	    this.mockMvc.perform(put("/api/person/LEROY/MERLIN")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(personUpdateJson))
	            .andExpect(status().isOk());

	    // Check if updated
	    this.mockMvc.perform(get("/api/person/LEROY/MERLIN"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.firstName", is("LEROY")))
	            .andExpect(jsonPath("$.lastName", is("MERLIN")))
	            .andExpect(jsonPath("$.address", is("rue du test")))
	            .andExpect(jsonPath("$.city", is("Lyon")))
	            .andExpect(jsonPath("$.zip", is(69000)))
	            .andExpect(jsonPath("$.phone", is("01-23-45-67-89")))
	            .andExpect(jsonPath("$.email", is("lyon@leroymerlin.fr")));
	}
	
	    /*
	     * Testing to delete person.
	     */
	    @Test
	    void deletePersonTestIT() throws Exception {

	        // add person to delete.
	        Map<String, Object> personToAdd = new HashMap<>();
	        personToAdd.put("firstName", "LEROY");
	        personToAdd.put("lastName", "MERLIN");
	        personToAdd.put("address", "rue du bricolage");
	        personToAdd.put("city", "Paris");
	        personToAdd.put("zip", 75000);
	        personToAdd.put("phone", "04-56-98-96-63");
	        personToAdd.put("email", "paris@leroymerlin.fr");

	        String personToAddJson = objectMapper.writeValueAsString(personToAdd);

	        this.mockMvc.perform(post("/api/person")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(personToAddJson))
	                .andExpect(status().isCreated());

	        // Delete person
	        this.mockMvc.perform(delete("/api/person/LEROY/MERLIN")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isNoContent());

	        // Check if deleted
	        this.mockMvc.perform(get("/api/person/LEROY/MERLIN"))
	                .andExpect(status().isNotFound());
	    }
	
}
