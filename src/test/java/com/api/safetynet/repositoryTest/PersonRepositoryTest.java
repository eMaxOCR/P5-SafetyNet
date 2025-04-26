package com.api.safetynet.repositoryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PersonRepository personRepository;

    @Mock
    private DataParsing dataParsing;

    private List<Person> persons;

    @BeforeEach
    void setUp() {
        persons = new ArrayList<>();
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setAddress("123 Main St");
        p1.setCity("Some City");
        p1.setZip(12345);
        p1.setPhone("111-222-3333");
        p1.setEmail("john.doe@example.com");
        persons.add(p1);

        Person p2 = new Person();
        p2.setFirstName("Jane");
        p2.setLastName("Doe");
        p2.setAddress("123 Main St");
        p2.setCity("Some City");
        p2.setZip(12345);
        p2.setPhone("444-555-6666");
        p2.setEmail("jane.doe@example.com");
        persons.add(p2);

        Person p3 = new Person();
        p3.setFirstName("Peter");
        p3.setLastName("Pan");
        p3.setAddress("456 Oak Ave");
        p3.setCity("Another City");
        p3.setZip(67890);
        p3.setPhone("777-888-9999");
        p3.setEmail("peter.pan@example.com");
        persons.add(p3);

        when(dataParsing.parseJsonPerson()).thenReturn(persons);
        personRepository.init(); // Initialize the repository with the mocked data
    }

    @Test
    void getOnePersonTest() {
        Person result = personRepository.getOnePerson("John", "Doe");
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }
    
    @Test
    void getOnePersonFailedTest() {
        Person result = personRepository.getOnePerson("No", "Body");
        assertNull(result);
    }

    @Test
    void findAllPersonsTest() {
        List<Person> result = personRepository.findAllPersons();
        assertEquals(3, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Peter", result.get(2).getFirstName());
        verify(dataParsing, times(1)).parseJsonPerson();
    }

    @Test
    void findAllPersonsByLastNameTest() {
        List<Person> result = personRepository.findAllPersonsByLastName("Doe");
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void getAllPersonsFromSameAddressTest() {
        List<Person> result = personRepository.getAllPersonsFromSameAddress("123 Main St");
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void addPersonTest() {
        Person newPerson = new Person();
        newPerson.setFirstName("Mike");
        newPerson.setLastName("Jordan");
        newPerson.setAddress("999 Hoop Lane");
        newPerson.setCity("Chicago");
        newPerson.setZip(60600);
        newPerson.setPhone("312-555-1212");
        newPerson.setEmail("mike.jordan@example.com");
        Person result = personRepository.addPerson(newPerson);
        assertNotNull(result);
        assertEquals(4, persons.size());
        assertTrue(persons.contains(newPerson));
        verify(dataParsing, times(1)).addElementIntoJson(any(), eq("persons"));
    }

    @Test
    void updatePersonTest() {
    	Person updatedPerson = new Person();
        updatedPerson.setAddress("456 New Ave");
        updatedPerson.setCity("New City");
        updatedPerson.setZip(98765);
        updatedPerson.setPhone("111-111-1111");
        updatedPerson.setEmail("new.email@example.com");
        Person result = personRepository.updatePerson("John", "Doe", updatedPerson);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("456 New Ave", result.getAddress());
        assertEquals("New City", result.getCity());
        assertEquals(98765, result.getZip());
        assertEquals("111-111-1111", result.getPhone());
        assertEquals("new.email@example.com", result.getEmail());
        verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("persons"));
        verify(dataParsing, times(1)).addElementIntoJson(any(), eq("persons"));
    }
    
    @Test
    void updatePersonFailedTest() {
    	Person updatedPerson = new Person();
        updatedPerson.setAddress("456 New Ave");
        updatedPerson.setCity("New City");
        updatedPerson.setZip(98765);
        updatedPerson.setPhone("111-111-1111");
        updatedPerson.setEmail("new.email@example.com");
        Person result = personRepository.updatePerson("No", "Body", updatedPerson);
        assertNull(result);
        
    }
    
    @Test
    void deletePersonTest() {
        assertTrue(personRepository.deletePerson("John", "Doe"));
        assertEquals(2, persons.size());
        //assertFalse(persons.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")));
        assertFalse(persons.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")));
        verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("persons"));
    }
    
    @Test
    void deletePersonFailedTest() {
        assertFalse(personRepository.deletePerson("No", "Body"));
    }

}
