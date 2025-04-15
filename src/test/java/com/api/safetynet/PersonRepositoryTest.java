package com.api.safetynet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.api.safetynet.model.Person;
import com.api.safetynet.repository.DataParsing;
import com.api.safetynet.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {
	
	@InjectMocks
	PersonRepository pr;
	
	@Mock
	private DataParsing dataParsing;
	
	public List<Person> defaultPersons = new ArrayList<>();
	Person person1 = new Person();
	Person person2 = new Person();
	
 	@BeforeEach
    void setUp() throws IOException {
 				
 		person1.setFirstName("David");
        person1.setLastName("Plamon");
        person1.setAddress("Rue du crayon");
        person1.setCity("PARIS");
        person1.setZip(75000);
        person1.setPhone("0699887744");
        person1.setEmail("d.plamon@google.com");
        
        person2.setFirstName("Mathilde");
        person2.setLastName("Delacourt");
        person2.setAddress("36 avenue stylot");
        person2.setCity("PARIS");
        person2.setZip(75000);
        person2.setPhone("0688778899");
        person2.setEmail("m.delacourt@google.com");
        
        defaultPersons.add(person1);
        defaultPersons.add(person2);

        when(dataParsing.parseJsonPerson()).thenReturn(defaultPersons);
        // Initialize the repository after setting up mock
        pr.init(); 
    }
	
	@Test
	public void getOnePersonTest() {
		//GIVEN
		String personFirstName = "David";
		String personLastName = "Plamon";
		
		//WHEN
		Person personFounded = pr.getOnePerson(personFirstName, personLastName);
		
		//THEN
		assertEquals(personFounded.getFirstName(),personFirstName);
		assertEquals(personFounded.getLastName(),personLastName);
	}
	
	@Test
	public void getOnePersonFailedTest() {
		//GIVEN
		String personFirstName = "Hector";
		String personLastName = "Stylot";
		
		//WHEN
		Person personFounded = pr.getOnePerson(personFirstName, personLastName);
		
		//THEN
		assertNull(personFounded);
	}
	
	@Test
	public void findAllPersons() {
		//GIVEN
		List<Person> findAllPersons = pr.findAllPersons();
		//THEN
		assertThat(findAllPersons).isNotNull();
		assertThat(findAllPersons).hasSize(2);
		assertThat(findAllPersons).containsAll(defaultPersons);
	}
	
	@Test
	public void findAllPersonsByLastNameTest() {
		//GIVEN
		String lastNameToFind = "Plamon";
		List<Person> expectedPersons = new ArrayList<>();
		expectedPersons.add(person1);

		//WHEN
		List<Person> foundPersons = pr.findAllPersonsByLastName(lastNameToFind);
		//THEN
		assertThat(foundPersons).isNotNull();
		assertThat(foundPersons).isEqualTo(expectedPersons);
	}
	
	@Test
	public void getAllPersonsFromSameAddressTest() {
		//GIVEN
		List<Person> personAdresse = pr.getAllPersonsFromSameAddress("Rue du crayon");
		
		//THEN
        assertEquals(1, personAdresse.size());
        assertTrue(personAdresse.stream().allMatch(person -> person.getAddress().equals("Rue du crayon")));

	}
	
	@Test
	public void addPersonIntoJsonTest() {
		// GIVEN
        Person newPerson = new Person();
        newPerson.setFirstName("Eric");
        newPerson.setLastName("Cartman");
        newPerson.setAddress("123 Main St");
        newPerson.setCity("South Park");
        newPerson.setZip(80000);
        newPerson.setPhone("111-222-3333");
        newPerson.setEmail("eric@gmail.com");

        // WHEN
        Person addedPerson = pr.addPerson(newPerson);

        // THEN
        assertThat(addedPerson).isNotNull();
        assertEquals(newPerson, addedPerson);
        assertThat(pr.findAllPersons()).contains(newPerson);
        verify(dataParsing, times(1)).addPersonIntoJson(newPerson);

	}
	
	@Test
	public void updatePersonTest() {
		// GIVEN
        Person updatedInfo = new Person();
        updatedInfo.setFirstName("David");
        updatedInfo.setLastName("Plamon");
        updatedInfo.setAddress("456 New Address");
        updatedInfo.setCity("New City");
        updatedInfo.setZip(12345);
        updatedInfo.setPhone("999-888-7777");
        updatedInfo.setEmail("john.new@gmail.com");

        // WHEN
        Person updatedPerson = pr.updatePerson("David", "Plamon", updatedInfo);

        // THEN
        assertThat(updatedPerson).isNotNull();
        assertEquals("David", updatedPerson.getFirstName());
        assertEquals("Plamon", updatedPerson.getLastName());
        assertEquals("456 New Address", updatedPerson.getAddress());
        assertEquals("New City", updatedPerson.getCity());
        assertEquals(12345, updatedPerson.getZip());
        assertEquals("999-888-7777", updatedPerson.getPhone());
        assertEquals("john.new@gmail.com", updatedPerson.getEmail());

        verify(dataParsing, times(1)).deletePersonFromJson(argThat(person ->
                person.getFirstName().equals("David") && person.getLastName().equals("Plamon") && person.getAddress().equals("456 New Address")));
        verify(dataParsing, times(1)).addPersonIntoJson(updatedInfo);
	}
	
	@Test
	public void updatePersonFailedTest() {
		// GIVEN
        Person updatedInfo = new Person();
        updatedInfo.setFirstName("Fake");
        updatedInfo.setLastName("Person");
        updatedInfo.setAddress("Address");
        updatedInfo.setCity("City");
        updatedInfo.setZip(12345);
        updatedInfo.setPhone("999-888-7777");
        updatedInfo.setEmail("fake@gmail.com");

        // WHEN
        Person updatedPerson = pr.updatePerson("Fake", "Person", updatedInfo);

        // THEN
        assertNull(updatedPerson);
        verify(dataParsing, never()).deletePersonFromJson(any());
        verify(dataParsing, never()).addPersonIntoJson(any());
    }
	
	@Test
	public void deletePersonTest() {
		// GIVEN
        String firstNameToDelete = "David";
        String lastNameToDelete = "Plamon";

        // WHEN
        boolean deleted = pr.deletePerson(firstNameToDelete, lastNameToDelete);

        // THEN
        assertTrue(deleted);
        assertNull(pr.getOnePerson(firstNameToDelete, lastNameToDelete));
        verify(dataParsing, times(1)).deletePersonFromJson(argThat(person ->
                person.getFirstName().equals(firstNameToDelete) && person.getLastName().equals(lastNameToDelete)));
    
	}
	
	@Test
	public void deletePersonFailedTest() {
		// GIVEN
        String firstNameToDelete = "Pierre";
        String lastNameToDelete = "Feuille";

        // WHEN
        boolean deleted = pr.deletePerson(firstNameToDelete, lastNameToDelete);

        // THEN
        assertFalse(deleted);
        assertNull(pr.getOnePerson(firstNameToDelete, lastNameToDelete)); 
    
	}

}
