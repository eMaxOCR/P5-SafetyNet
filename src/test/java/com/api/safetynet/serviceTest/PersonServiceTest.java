package com.api.safetynet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.ParseException;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.model.Person;
import com.api.safetynet.model.DTO.GroupOfPersonNearFireStationDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoWithMedicalRecordsDTO;
import com.api.safetynet.model.DTO.PersonNearFireStationDTO;
import com.api.safetynet.repository.FirestationRepository;
import com.api.safetynet.repository.PersonRepository;
import com.api.safetynet.service.FirestationService;
import com.api.safetynet.service.MedicalRecordService;
import com.api.safetynet.service.PersonService;

@ExtendWith(MockitoExtension.class) 
public class PersonServiceTest {
	
	@InjectMocks
	PersonService ps;
	
	@Mock
	FirestationService fs;
	
	@Mock
	MedicalRecordService ms;
	
	@Mock
	PersonRepository pr;
	
	static Person person1 = new Person();
	static Person person2 = new Person();
	
	@BeforeAll
	static public void setUpBeforeAll() {

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

	}
	
	@Test
    public void getAllPersonTest() {
        // Arrange
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        when(pr.findAllPersons()).thenReturn(persons);

        // Act
        Iterable<Person> result = ps.getAllPerson();

        // Assert
        List<Person> resultList = new ArrayList<>();
        result.forEach(resultList::add);
        assertEquals(2, resultList.size());

        assertEquals("David", resultList.get(0).getFirstName());
        assertEquals("Plamon", resultList.get(0).getLastName());
        assertEquals("Rue du crayon", resultList.get(0).getAddress());
        assertEquals("PARIS", resultList.get(0).getCity());
        assertEquals(75000, resultList.get(0).getZip());
        assertEquals("0699887744", resultList.get(0).getPhone());
        assertEquals("d.plamon@google.com", resultList.get(0).getEmail());

        assertEquals("Mathilde", resultList.get(1).getFirstName());
        assertEquals("Delacourt", resultList.get(1).getLastName());
        assertEquals("36 avenue stylot", resultList.get(1).getAddress());
        assertEquals("PARIS", resultList.get(1).getCity());
        assertEquals(75000, resultList.get(1).getZip());
        assertEquals("0688778899", resultList.get(1).getPhone());
        assertEquals("m.delacourt@google.com", resultList.get(1).getEmail());
    }
	
	@Test
    void getOnePersonTest() {
        // Arrange
        String firstName = "David";
        String lastName = "Plamon";
        
        Person expectedPerson = new Person();
        expectedPerson.setFirstName("David");
        expectedPerson.setLastName("Plamon");
        expectedPerson.setAddress("Rue du crayon");
        expectedPerson.setCity("PARIS");
        expectedPerson.setZip(75000);
        expectedPerson.setPhone("0699887744");
        expectedPerson.setEmail("d.plamon@google.com");

        when(pr.getOnePerson(firstName, lastName)).thenReturn(person1);

        // Act
        Person result = ps.getOnePerson(firstName, lastName);

        // Assert
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals("Rue du crayon", result.getAddress());
        assertEquals("PARIS", result.getCity());
        assertEquals(75000, result.getZip());
        assertEquals("0699887744", result.getPhone());
        assertEquals("d.plamon@google.com", result.getEmail());
    }
	
	//TODO Add
	//TODO Update
	
	@Test
    void deletePersonTest() {
        // Arrange
        String firstNameToDelete = "David";
        String lastNameToDelete = "Plamon";

        when(pr.deletePerson(firstNameToDelete, lastNameToDelete))
                .thenReturn(true);

        // Act
        Boolean result = ps.deletePerson(firstNameToDelete, lastNameToDelete);

        // Assert
        assertTrue(result);
        verify(pr, times(1)).deletePerson(firstNameToDelete, lastNameToDelete);
    }
	
	@Test
    void calculatePersonAgeTest() throws ParseException, java.text.ParseException {
        // Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("1997-03-11"); // Born less than a year ago

        // Act
        int age = ps.calculatePersonAge(birthDate);

        // Assert
        assertEquals(28, age);
    }
	
	@Test
    void getAllPersonsFromSameAddressTest() {
        // Arrange
        String address = "test1";
        List<Person> expectedPersons = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("David");
        person1.setLastName("Plamon");
        person1.setAddress(address);
        expectedPersons.add(person1);

        Person person2 = new Person();
        person2.setFirstName("Jeanne");
        person2.setLastName("Dark");
        person2.setAddress(address);
        expectedPersons.add(person2);

        when(pr.getAllPersonsFromSameAddress(address)).thenReturn(expectedPersons);

        // Act
        List<Person> result = ps.getAllPersonsFromSameAddress(address);

        // Assert
        assertEquals(2, result.size());
        assertEquals(address, result.get(0).getAddress());
        assertEquals("David", result.get(0).getFirstName());
        assertEquals(address, result.get(1).getAddress());
        assertEquals("Jeanne", result.get(1).getFirstName());
    }
	
	 @Test
     void getPersonPhoneNumberByFirestationTest() {
	 // Arrange
        Person person3 = new Person();
        person3.setFirstName("David");
        person3.setLastName("Plamon");
        person3.setAddress("36 avenue stylot");
        person3.setCity("PARIS");
        person3.setZip(75000);
        person3.setPhone("0699887744");
        person3.setEmail("d.plamon@google.com");

        Person person4 = new Person();
        person4.setFirstName("Mathilde");
        person4.setLastName("Delacourt");
        person4.setAddress("36 avenue stylot");
        person4.setCity("Paris");
        person4.setZip(75000);
        person4.setPhone("0688778899");
        person4.setEmail("m.delacourt@google.com");

        List<Firestation> firestationsList = new ArrayList<>();
        Firestation firestation1 = new Firestation();
        firestation1.setStation(1);
        firestation1.setAddress("36 avenue stylot");
        firestationsList.add(firestation1); 

        List<Person> persons = new ArrayList<>();
        persons.add(person3);
        persons.add(person4);

        Set<String> expectedPhones = new HashSet<>();
        expectedPhones.add("0699887744");
        expectedPhones.add("0688778899");

        when(fs.getAllFirestation()).thenReturn(firestationsList);
        when(pr.findAllPersons()).thenReturn(persons);

        // Act
        Set<String> result = ps.getPersonPhoneNumberByFirestation(1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedPhones));
    }
	 
	 @Test
     void getPersonEmailByCityTest() {
        // Arrange
        String cityToFind = "PARIS";
        
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);


        Set<String> expectedEmails = new HashSet<>();
        expectedEmails.add("d.plamon@google.com");
        expectedEmails.add("m.delacourt@google.com");

        when(pr.findAllPersons()).thenReturn(persons);

        // Act
        Set<String> result = ps.getPersonEmailByCity(cityToFind);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedEmails));
    }
	 
	@Test
    void getListOfPersonInformationByLastNameTest() {
        // Arrange
        String lastNameToFind = "Plamon";
        List<Person> persons = new ArrayList<>();
        persons.add(person1);

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("David");
        medicalRecord1.setLastName(lastNameToFind);
        
        List<Medication> medicationList = new ArrayList<>();
        Medication medication = new Medication();
        medication.setMedicationName("aspirin");
        medication.setQuantityInMg("10mg");
        medicationList.add(medication);
        medicalRecord1.setMedications(medicationList);
        
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("peanuts");
        medicalRecord1.setAllergies(allergiesList);

        when(pr.findAllPersonsByLastName(lastNameToFind)).thenReturn(persons);
        when(ms.getOneMedicalRecord("David", lastNameToFind)).thenReturn(medicalRecord1);

        // Act
        List<PersonInfoDTO> result = ps.getListOfPersonInformationByLastName(lastNameToFind);

        // Assert
        assertEquals(1, result.size());
        PersonInfoDTO personInfo = result.get(0);
        assertEquals(lastNameToFind, personInfo.getLastName());
        assertEquals(person1.getEmail(), personInfo.getEmail());
        assertEquals(person1.getAddress(), personInfo.getAddress());
        assertEquals(medicalRecord1.getBirthdate(), personInfo.getBirthdate());
        assertTrue(personInfo.getMedications().stream().anyMatch(med -> med.getMedicationName().equals("aspirin")));
        assertTrue(personInfo.getAllergies().contains("peanuts"));
    }
	 
	//TODO getChildInformationsByAddressTest()
	
	@Test
    void getPersonsAndFireStationNumberByAddressTest() throws Exception {
        // Arrange
        String address = "456 Elm St";
        List<Person> persons = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress(address);
        person1.setPhone("111-222-3333");

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress(address);
        person2.setPhone("444-555-6666");

        persons.add(person1);
        persons.add(person2);

        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate(new Date());
        
        List<Medication> medicationList = new ArrayList<>();
        Medication medication = new Medication();
        medication.setMedicationName("aspirin");
        medication.setQuantityInMg("10mg");
        medicationList.add(medication);
        mr1.setMedications(medicationList);
        
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("peanuts");
        mr1.setAllergies(allergiesList);

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jane");
        mr2.setLastName("Doe");
        mr2.setBirthdate(new Date()); 
        
        List<Medication> medicationList2 = new ArrayList<>();
        Medication medication2 = new Medication();
        medication2.setMedicationName("paracetamol");
        medication2.setQuantityInMg("10mg");
        medicationList2.add(medication2);
        mr2.setMedications(medicationList2);
        
        List<String> allergiesList2 = new ArrayList<>();
        allergiesList2.add("cats");
        mr2.setAllergies(allergiesList2);

        when(pr.getAllPersonsFromSameAddress(address)).thenReturn(persons);
        when(ms.getOneMedicalRecord("John", "Doe")).thenReturn(mr1);
        when(ms.getOneMedicalRecord("Jane", "Doe")).thenReturn(mr2);

        when(fs.getFirestationNumberByAddress(address)).thenReturn(2);

        // Act
        GroupOfPersonNearFireStationDTO result = ps.getPersonsAndFireStationNumberByAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getResidents().size());
        assertEquals(2, result.getFiresStation());

        PersonNearFireStationDTO resident1 = result.getResidents().get(0);
        assertEquals("Doe", resident1.getLastName());
        assertEquals("111-222-3333", resident1.getPhoneNumber());
        assertEquals(List.of("aspirin"), resident1.getMedications().stream().map(Medication::getMedicationName).collect(Collectors.toList()));
        assertEquals(List.of("peanuts"), resident1.getAllergies());

        PersonNearFireStationDTO resident2 = result.getResidents().get(1);
        assertEquals("Doe", resident2.getLastName());
        assertEquals("444-555-6666", resident2.getPhoneNumber());
        assertEquals(List.of("paracetamol"), resident2.getMedications().stream().map(Medication::getMedicationName).collect(Collectors.toList()));
        assertEquals(List.of("cats"), resident2.getAllergies());
    }
	
	@Test
    void getAllHousesServedByFireStationNumberTest() throws Exception { //TODO Verify
		// Arrange
        List<Integer> stationNumbers = List.of(1, 2);

        // Firestations serving 123 Main St
        Firestation fs1 = new Firestation();
        fs1.setStation(1);
        fs1.setAddress("123 Main St");

        Firestation fs2 = new Firestation();
        fs2.setStation(2);
        fs2.setAddress("123 Main St");

        when(fs.getAllFirestationByStationNumberList(stationNumbers)).thenReturn(List.of(fs1, fs2));

        // Persons at 123 Main St
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setAddress("123 Main St");
        p1.setPhone("111-222-3333");

        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate(new Date());

        List<Medication> medicationList = new ArrayList<>();
        Medication medication = new Medication();
        medication.setMedicationName("paracetamol");
        medication.setQuantityInMg("10mg");
        medicationList.add(medication);
        mr1.setMedications(medicationList);
        mr1.setAllergies(List.of("peanuts"));

        when(pr.getAllPersonsFromSameAddress("123 Main St")).thenReturn(List.of(p1));
        when(ms.getOneMedicalRecord("John", "Doe")).thenReturn(mr1);

        // Act
        List<HouseNearFireStationDTO> result = ps.getAllHousesServedByFireStationNumber(stationNumbers);

        // Assert
        assertNotNull(result);

        HouseNearFireStationDTO house1 = result.get(0);
        assertEquals("123 Main St", house1.getAddress());
        assertEquals(1, house1.getResident().size());
        PersonInfoWithMedicalRecordsDTO resident1 = house1.getResident().get(0);
        assertEquals("Doe", resident1.getLastName());
        assertEquals("111-222-3333", resident1.getPhoneNumber());
        assertEquals(List.of("paracetamol"), resident1.getMedications().stream().map(Medication::getMedicationName).collect(Collectors.toList()));
        assertEquals(List.of("peanuts"), resident1.getAllergies());
	}
//	@Test
//	void getAllPersonServedByFireStationNumberTest() throws Exception { //TODO Verify
//        // Arrange
//        int stationNumber = 1;
//
//        // Firestations serving addresses
//        Firestation fs1 = new Firestation();
//        fs1.setStation(1);
//        fs1.setAddress("123 Main St");
//
//        Firestation fs2 = new Firestation();
//        fs2.setStation(1);
//        fs2.setAddress("456 Oak Ave");
//
//        when(fs.getAllFirestationByStationNumber(stationNumber)).thenReturn(List.of(fs1, fs2));
//
//        // Persons at 123 Main St
//        Person p1 = new Person();
//        p1.setFirstName("John");
//        p1.setLastName("Doe");
//        p1.setAddress("123 Main St");
//        p1.setPhone("111-222-3333");
//
//        MedicalRecord mr1 = new MedicalRecord();
//        mr1.setFirstName("John");
//        mr1.setLastName("Doe");
//        mr1.setBirthdate(new Date());
//       // mr1.setMedications(List.of(new Medication("paracetamol", "10mg")));
//        mr1.setAllergies(List.of("peanuts"));
//
//        // Persons at 456 Oak Ave
//        Person p2 = new Person();
//        p2.setFirstName("Jane");
//        p2.setLastName("Smith");
//        p2.setAddress("456 Oak Ave");
//        p2.setPhone("444-555-6666");
//
//        MedicalRecord mr2 = new MedicalRecord();
//        mr2.setFirstName("Jane");
//        mr2.setLastName("Smith");
//        mr2.setBirthdate(new Date());
//       // mr2.setMedications(List.of(new Medication("aspirin", "500mg")));
//        mr2.setAllergies(List.of("none"));
//
//        when(pr.getAllPersonsFromSameAddress("123 Main St")).thenReturn(List.of(p1));
//        when(pr.getAllPersonsFromSameAddress("456 Oak Ave")).thenReturn(List.of(p2));
//        when(ms.getOneMedicalRecord("John", "Doe")).thenReturn(mr1);
//        when(ms.getOneMedicalRecord("Jane", "Smith")).thenReturn(mr2);
//
//        // Act
//        List<PersonInfoWithMedicalRecordsDTO> result = ps.getAllPersonServedByFireStationNumber(stationNumber);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//        PersonInfoWithMedicalRecordsDTO person1 = result.stream()
//                .filter(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe"))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(person1);
//        assertEquals("Doe", person1.getLastName());
//        assertEquals("111-222-3333", person1.getPhoneNumber());
//        assertEquals(List.of("paracetamol"), person1.getMedications().stream().map(Medication::getMedicationName).collect(Collectors.toList()));
//        assertEquals(List.of("peanuts"), person1.getAllergies());
//
//        PersonInfoWithMedicalRecordsDTO person2 = result.stream()
//                .filter(p -> p.getFirstName().equals("Jane") && p.getLastName().equals("Smith"))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(person2);
//        assertEquals("Smith", person2.getLastName());
//        assertEquals("444-555-6666", person2.getPhoneNumber());
//        assertEquals(List.of("aspirin"), person2.getMedications().stream().map(Medication::getMedicationName).collect(Collectors.toList()));
//        assertEquals(List.of("none"), person2.getAllergies());
//	}

}
