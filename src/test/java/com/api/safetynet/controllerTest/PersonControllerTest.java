package com.api.safetynet.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.api.safetynet.controller.PersonController;
import com.api.safetynet.model.Person;
import com.api.safetynet.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {
		
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	PersonService ps;
	
		
	Person person = new Person();
	Person person2 = new Person();
	
	@BeforeAll
	public void setUpPerson() {
		//Put temporary information for first person
		person.setFirstName("David");
		person.setLastName("Plamon");
		person.setAddress("125 Rue du champ");
		person.setZip(75000);
		person.setCity("Paris");
		person.setPhone("06-45-66-99-89");
		person.setEmail("d.plamon@google.com");
			
		//Put temporary information for second person
		person2.setFirstName("Laurent");
		person2.setLastName("Bistrot");
		person2.setAddress("10 Chemin des feutres");
		person2.setZip(75000);
		person2.setCity("Paris");
		person2.setPhone("06-74-13-84-64");
		person2.setEmail("l.bistrot@google.com");
	}
	
	@Test	
	public void getAllPersonTest() throws Exception {
		//Test getting all persons.
		//GIVEN
		List<Person> listOfPersons = new ArrayList<>(); //Simulate fake list with two persons.
		listOfPersons.add(person); //adding first person
		listOfPersons.add(person2); //adding second person
		when(ps.getAllPerson()).thenReturn(listOfPersons); //When we use getAllPerson() method, it should return a list of persons.
		
		//WHEN
		this.mockMvc.perform(get("/api/person")
				.accept(MediaType.APPLICATION_JSON))
		//THEN
			.andExpect(status().isOk())													//Check that response OK
			.andExpect(jsonPath("$.[*].firstName").isNotEmpty())	
			.andExpect(jsonPath("$.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].address").isNotEmpty())
			.andExpect(jsonPath("$.[*].zip").isNotEmpty())
			.andExpect(jsonPath("$.[*].city").isNotEmpty())
			.andExpect(jsonPath("$.[*].phone").isNotEmpty())
			.andExpect(jsonPath("$.[*].email").isNotEmpty());
		assertEquals(2, listOfPersons.size());
	}
	
	@Test
	public void getOnePersonTest() throws Exception {
		//GIVEN
		//person in setUpPerson()
		
		//WHEN
		when(ps.getOnePerson(person.getFirstName(), person.getLastName())).thenReturn(person);
		
		//THEN
		this.mockMvc.perform(get("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value(person.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(person.getLastName()))
		.andExpect(jsonPath("$.address").isNotEmpty())
		.andExpect(jsonPath("$.zip").isNotEmpty())
		.andExpect(jsonPath("$.city").isNotEmpty())
		.andExpect(jsonPath("$.phone").isNotEmpty())
		.andExpect(jsonPath("$.email").isNotEmpty());
		
	}
	
	@Test
	public void getOnePersonFailedTest() throws Exception {
		//GIVEN
		//person in setUpPerson()
		
		//WHEN
		when(ps.getOnePerson(anyString(), anyString())).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(get("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void addPersonTest() throws Exception {
		//GIVEN
		Person thirdPerson = new Person();
		thirdPerson.setFirstName("Bertrant");
		thirdPerson.setLastName("Daim");
		thirdPerson.setAddress("656 avenue blanche");
		thirdPerson.setZip(75000);
		thirdPerson.setCity("Paris");
		thirdPerson.setPhone("123-655-9998");
		thirdPerson.setEmail("b.daim@gmail.com");
		
		//WHEN
		when(ps.addPerson(any(Person.class))).thenReturn(thirdPerson);
		
		//THEN
		this.mockMvc.perform(post("/api/person")
				.content(objectMapper.writeValueAsString(thirdPerson)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.firstName").value(thirdPerson.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(thirdPerson.getLastName()))
		.andExpect(jsonPath("$.address").value(thirdPerson.getAddress()))
		.andExpect(jsonPath("$.zip").value(thirdPerson.getZip()))
		.andExpect(jsonPath("$.city").value(thirdPerson.getCity()))
		.andExpect(jsonPath("$.phone").value(thirdPerson.getPhone()))
		.andExpect(jsonPath("$.email").value(thirdPerson.getEmail()));
		
	}
	

	@Test
	public void updatePersonTest() throws Exception {
		//GIVEN
		Person personUpdate = new Person();
		personUpdate.setFirstName("David");
		personUpdate.setLastName("Plamon");
		personUpdate.setAddress("10 avenue du stylo");
		personUpdate.setZip(75000);
		personUpdate.setCity("Paris");
		personUpdate.setPhone("06-99-88-77-88");
		personUpdate.setEmail("d.plamon@wanadoo.com");
		
		//WHEN
		when(ps.updatePerson(person.getFirstName(), person.getLastName(), personUpdate)).thenReturn(personUpdate);
		
		//THEN
		this.mockMvc.perform(put("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName())
				.content(objectMapper.writeValueAsString(personUpdate)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value(personUpdate.getFirstName()))
		.andExpect(jsonPath("$.lastName").value(personUpdate.getLastName()))
		.andExpect(jsonPath("$.address").value(personUpdate.getAddress()))
		.andExpect(jsonPath("$.zip").value(personUpdate.getZip()))
		.andExpect(jsonPath("$.city").value(personUpdate.getCity()))
		.andExpect(jsonPath("$.phone").value(personUpdate.getPhone()))
		.andExpect(jsonPath("$.email").value(personUpdate.getEmail()));
		
	}
	
	@Test
	public void updatePersonFailedTest() throws Exception {
		//GIVEN
		Person fakePerson = new Person();
		
		//WHEN
		when(ps.updatePerson(anyString(), anyString(), any(Person.class))).thenReturn(null);
		
		//THEN
		this.mockMvc.perform(put("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName())
				.content(objectMapper.writeValueAsString(fakePerson)) //Used to convert object to json string
				.contentType(MediaType.APPLICATION_JSON) //set content as Json
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void deletePersonTest() throws Exception {
		//GIVEN
		//person in setUpPerson()
		
		//WHEN
		when(ps.deletePerson(person.getFirstName(), person.getLastName())).thenReturn(true);
		
		//THEN
		this.mockMvc.perform(delete("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deletePersonFailedTest() throws Exception {
		//GIVEN
		//person in setUpPerson()
		
		//WHEN
		when(ps.deletePerson(person.getFirstName(), person.getLastName())).thenReturn(false);
		
		//THEN
		this.mockMvc.perform(delete("/api/person/"+person.getFirstName().toString()+"/"+person.getLastName()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		
	}
	
	
}
