package com.api.safetynet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.api.safetynet.model.DTO.ChildInfoDTO;
import com.api.safetynet.model.DTO.FamilyMemberDTO;
import com.api.safetynet.model.DTO.GroupOfPersonNearFireStationDTO;
import com.api.safetynet.model.DTO.GroupOfPersonServedByFireStationDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoWithMedicalRecordsDTO;
import com.api.safetynet.model.DTO.PersonNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonServedByFireStationDTO;
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
	
	@Test
    void addPersonTest() {
		Person personToAdd = new Person();
        personToAdd.setFirstName("Joe");
        personToAdd.setLastName("Screen");
        personToAdd.setAddress("123 Soleil");
        personToAdd.setCity("Bordeau");
        personToAdd.setZip(12345);
        personToAdd.setPhone("666-999-888");
        personToAdd.setEmail("j.screen@gmail.com");
        
        when(pr.addPerson(any(Person.class))).thenReturn(personToAdd);

        Person addedPerson = ps.addPerson(personToAdd);

        assertNotNull(addedPerson);
        assertEquals(personToAdd.getFirstName(), addedPerson.getFirstName());
        assertEquals(personToAdd.getLastName(), addedPerson.getLastName());
        assertEquals(personToAdd.getAddress(), addedPerson.getAddress());
        assertEquals(personToAdd.getCity(), addedPerson.getCity());
        assertEquals(personToAdd.getZip(), addedPerson.getZip());
        assertEquals(personToAdd.getPhone(), addedPerson.getPhone());
        assertEquals(personToAdd.getEmail(), addedPerson.getEmail());
        verify(pr, times(1)).addPerson(personToAdd);
    }

	@Test
    void updatePerson_Test() {
		Person updatedPersonInfo = new Person();
		updatedPersonInfo.setFirstName("Joe");
		updatedPersonInfo.setLastName("Screen");
		updatedPersonInfo.setAddress("123 Soleil");
		updatedPersonInfo.setCity("Bordeau");
		updatedPersonInfo.setZip(12345);
		updatedPersonInfo.setPhone("666-999-888");
		updatedPersonInfo.setEmail("j.screen@gmail.com");
		
		Person personToUpdate = new Person();
		personToUpdate.setFirstName("Joe");
		personToUpdate.setLastName("Screen");
		personToUpdate.setAddress("123 Lune");
		personToUpdate.setCity("Paris");
		personToUpdate.setZip(75000);
		personToUpdate.setPhone("11223344556677");
		personToUpdate.setEmail("j.screen@yahoo.com");
		
	    String firstNameToUpdate = "Joe";
	    String lastNameToUpdate = "Screen";
	
	    when(pr.updatePerson(firstNameToUpdate, lastNameToUpdate, personToUpdate))
	            .thenReturn(personToUpdate);
	
	    Person updatedPerson = ps.updatePerson(firstNameToUpdate, lastNameToUpdate, personToUpdate);
	
	    assertNotNull(updatedPerson);
	    assertEquals(personToUpdate.getFirstName(), updatedPerson.getFirstName());
	    assertEquals(personToUpdate.getLastName(), updatedPerson.getLastName());
	    assertEquals(personToUpdate.getAddress(), updatedPerson.getAddress());
	    assertEquals(personToUpdate.getCity(), updatedPerson.getCity());
	    assertEquals(personToUpdate.getZip(), updatedPerson.getZip());
	    assertEquals(personToUpdate.getPhone(), updatedPerson.getPhone());
	    assertEquals(personToUpdate.getEmail(), updatedPerson.getEmail());
	    verify(pr, times(1))
            .updatePerson(firstNameToUpdate, lastNameToUpdate, personToUpdate);
    }
	
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
        Date birthDate = sdf.parse("1997-03-11"); 

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
        firestation1.setStation("1");
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
        Set<String> result = ps.getPersonPhoneNumberByFirestation("1");

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
	 
	@Test
    void getChildInformationsByAddressTest_singleChildWithAdultFamily() {
		// Arrange
	    String address = "123 Soleil";

	    // Persons at the address
	    Person child = new Person();
	    child.setFirstName("Billy");
	    child.setLastName("Kid");
	    child.setAddress(address);
	    child.setCity("Some City");
	    child.setZip(12345);
	    child.setPhone("111-222-3333");
	    child.setEmail("billy.kid@example.com");

	    Person adult1 = new Person();
	    adult1.setFirstName("John");
	    adult1.setLastName("Snow");
	    adult1.setAddress(address);
	    adult1.setCity("Some City");
	    adult1.setZip(12345);
	    adult1.setPhone("444-555-6666");
	    adult1.setEmail("john.doe@example.com");

	    Person adult2 = new Person();
	    adult2.setFirstName("Jane");
	    adult2.setLastName("Dark");
	    adult2.setAddress(address);
	    adult2.setCity("Some City");
	    adult2.setZip(12345);
	    adult2.setPhone("777-888-9999");
	    adult2.setEmail("jane.doe@example.com");

	    List<Person> persons = List.of(child, adult1, adult2);

	    // Medical Records
	    MedicalRecord childRecord = new MedicalRecord();
	    childRecord.setFirstName("Billy");
	    childRecord.setLastName("Kid");
	    childRecord.setBirthdate(Date.from(LocalDate.of(2010, 5, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    childRecord.setMedications(new ArrayList<>());
	    childRecord.setAllergies(new ArrayList<>());

	    MedicalRecord adult1Record = new MedicalRecord();
	    adult1Record.setFirstName("John");
	    adult1Record.setLastName("Snow");
	    adult1Record.setBirthdate(Date.from(LocalDate.of(1980, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    adult1Record.setMedications(new ArrayList<>());
	    adult1Record.setAllergies(new ArrayList<>());

	    MedicalRecord adult2Record = new MedicalRecord();
	    adult2Record.setFirstName("Jane");
	    adult2Record.setLastName("Dark");
	    adult2Record.setBirthdate(Date.from(LocalDate.of(1985, 8, 22).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    adult2Record.setMedications(new ArrayList<>());
	    adult2Record.setAllergies(new ArrayList<>());

	    when(pr.getAllPersonsFromSameAddress(address)).thenReturn(persons);
	    when(ms.getOneMedicalRecord("Billy", "Kid")).thenReturn(childRecord);
	    when(ms.getOneMedicalRecord("John", "Snow")).thenReturn(adult1Record);
	    when(ms.getOneMedicalRecord("Jane", "Dark")).thenReturn(adult2Record);

	    // Act
	    List<ChildInfoDTO> result = ps.getChildInformationsByAddress(address);

	    // Assert
	    assertEquals(1, result.size());
	    ChildInfoDTO childInfo = result.get(0);
	    assertEquals("Billy", childInfo.getFirstName());
	    assertEquals("Kid", childInfo.getLastName());
	    assertEquals(15, childInfo.getAge()); 
	    assertEquals(2, childInfo.getFamilyMember().size());

	    boolean johnFound = false;
	    boolean janeFound = false;
	    for (FamilyMemberDTO member : childInfo.getFamilyMember()) {
	        if (member.getFirstName().equals("John") && member.getLastName().equals("Snow") && member.getAge() == 45) { 
	            johnFound = true;
	        } else if (member.getFirstName().equals("Jane") && member.getLastName().equals("Dark") && member.getAge() == 40) { 
	            janeFound = true;
	        }
	    }
	    assertTrue(johnFound);
	    assertTrue(janeFound);
    }
	
	@Test
    void getPersonsAndFireStationNumberByAddressTest() throws Exception {
		// Arrange
        String address = "456 Elm St";
        List<Person> personsAtAddress = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress(address);
        person1.setPhone("111-222-3333");
        personsAtAddress.add(person1);

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress(address);
        person2.setPhone("444-555-6666");
        personsAtAddress.add(person2);

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("John");
        medicalRecord1.setLastName("Doe");
        medicalRecord1.setBirthdate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));


        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Jane");
        medicalRecord2.setLastName("Doe");
        medicalRecord2.setBirthdate(Date.from(LocalDate.of(2010, 6, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()));


        when(pr.getAllPersonsFromSameAddress(address)).thenReturn(personsAtAddress);
        when(ms.getOneMedicalRecord("John", "Doe")).thenReturn(medicalRecord1);
        when(ms.getOneMedicalRecord("Jane", "Doe")).thenReturn(medicalRecord2);
        when(fs.getFirestationNumberByAddress(address)).thenReturn("3");

        // Act
        GroupOfPersonNearFireStationDTO result = ps.getPersonsAndFireStationNumberByAddress(address);

        // Assert
        assertEquals("3", result.getFiresStation());
        assertEquals(2, result.getResidents().size());

        PersonNearFireStationDTO resident1 = result.getResidents().get(0);
        assertEquals("Doe", resident1.getLastName());
        assertEquals(25, resident1.getAge());
        assertEquals("111-222-3333", resident1.getPhoneNumber());

        PersonNearFireStationDTO resident2 = result.getResidents().get(1);
        assertEquals("Doe", resident2.getLastName());
        assertEquals(15, resident2.getAge());
        assertEquals("444-555-6666", resident2.getPhoneNumber());
	}
	
	@Test
    void getAllHousesServedByFireStationNumberTest() throws Exception { 
		// Arrange
        List<String> stationNumbers = List.of("1", "2");

        // Firestations serving 123 Main St
        Firestation fs1 = new Firestation();
        fs1.setStation("1");
        fs1.setAddress("123 Main St");

        Firestation fs2 = new Firestation();
        fs2.setStation("2");
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
	@Test
	void getAllPersonServedByFireStationNumberTest() throws Exception { 
		// Arrange
        String stationNumber = "2";

        // Firestations serving station number "2"
        List<Firestation> firestations = new ArrayList<>();
        Firestation fs1 = new Firestation();
        fs1.setStation("2");
        fs1.setAddress("742 Evergreen Terrace");
        firestations.add(fs1);
        Firestation fs2 = new Firestation();
        fs2.setStation("2");
        fs2.setAddress("123 Fake Street");
        firestations.add(fs2);

        // Persons living at the addresses served by station "2"
        List<Person> persons1 = new ArrayList<>();
        Person adult1 = new Person();
        adult1.setFirstName("Homer");
        adult1.setLastName("Simpson");
        adult1.setAddress("742 Evergreen Terrace");
        adult1.setPhone("555-1212");
        persons1.add(adult1);
        Person child1 = new Person();
        child1.setFirstName("Bart");
        child1.setLastName("Simpson");
        child1.setAddress("742 Evergreen Terrace");
        child1.setPhone("555-1212");
        persons1.add(child1);

        List<Person> persons2 = new ArrayList<>();
        Person adult2 = new Person();
        adult2.setFirstName("Ned");
        adult2.setLastName("Flanders");
        adult2.setAddress("123 Fake Street");
        adult2.setPhone("555-1111");
        persons2.add(adult2);

        // Medical Records
        MedicalRecord mrAdult1 = new MedicalRecord();
        mrAdult1.setFirstName("Homer");
        mrAdult1.setLastName("Simpson");
        mrAdult1.setBirthdate(Date.from(LocalDate.of(1970, 5, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        MedicalRecord mrChild1 = new MedicalRecord();
        mrChild1.setFirstName("Bart");
        mrChild1.setLastName("Simpson");
        mrChild1.setBirthdate(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        MedicalRecord mrAdult2 = new MedicalRecord();
        mrAdult2.setFirstName("Ned");
        mrAdult2.setLastName("Flanders");
        mrAdult2.setBirthdate(Date.from(LocalDate.of(1965, 9, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(fs.getAllFirestationByStationNumber(stationNumber)).thenReturn(firestations);
        when(pr.getAllPersonsFromSameAddress("742 Evergreen Terrace")).thenReturn(persons1);
        when(pr.getAllPersonsFromSameAddress("123 Fake Street")).thenReturn(persons2);
        when(ms.getOneMedicalRecord("Homer", "Simpson")).thenReturn(mrAdult1);
        when(ms.getOneMedicalRecord("Bart", "Simpson")).thenReturn(mrChild1);
        when(ms.getOneMedicalRecord("Ned", "Flanders")).thenReturn(mrAdult2);

        // Act
        GroupOfPersonServedByFireStationDTO result = ps.getAllPersonServedByFireStationNumber(stationNumber);

        // Assert
        assertEquals(2, result.getAdultCount());
        assertEquals(1, result.getChildCount());
        assertEquals(3, result.getResidents().size());

        List<PersonServedByFireStationDTO> residents = result.getResidents();
        boolean homerFound = false;
        boolean bartFound = false;
        boolean nedFound = false;

        for (PersonServedByFireStationDTO resident : residents) {
            if (resident.getFirstName().equals("Homer")) {
                assertEquals("Simpson", resident.getLastName());
                assertEquals("555-1212", resident.getPhoneNumber());
                assertEquals(55, resident.getAge()); 
                assertEquals("742 Evergreen Terrace", resident.getAddress());
                homerFound = true;
            } else if (resident.getFirstName().equals("Bart")) {
                assertEquals("Simpson", resident.getLastName());
                assertEquals("555-1212", resident.getPhoneNumber());
                assertEquals(15, resident.getAge());
                assertEquals("742 Evergreen Terrace", resident.getAddress());
                bartFound = true;
            } else if (resident.getFirstName().equals("Ned")) {
                assertEquals("Flanders", resident.getLastName());
                assertEquals("555-1111", resident.getPhoneNumber());
                assertEquals(60, resident.getAge());
                assertEquals("123 Fake Street", resident.getAddress());
                nedFound = true;
            }
        }

        assertTrue(homerFound);
        assertTrue(bartFound);
        assertTrue(nedFound);
    }

}
