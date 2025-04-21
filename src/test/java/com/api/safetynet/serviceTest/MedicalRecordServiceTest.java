package com.api.safetynet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.repository.MedicalRecordRepository;
import com.api.safetynet.service.MedicalRecordService;

@ExtendWith(MockitoExtension.class) 
public class MedicalRecordServiceTest {
	
	@InjectMocks
	MedicalRecordService ms;
	
	@Mock
	MedicalRecordRepository mr;
	
	@Test
	public void getAllMedicalRecordTest() {
		// Arrange
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("David");
        medicalRecord1.setLastName("Plamon");
        medicalRecords.add(medicalRecord1);

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Jeanne");
        medicalRecord2.setLastName("Belleville");
        medicalRecords.add(medicalRecord2);

        when(mr.getAllMedicalRecords()).thenReturn(medicalRecords);

        // Act
        Iterable<MedicalRecord> result = ms.getAllMedicalRecord();

        // Assert
        List<MedicalRecord> resultList = new ArrayList<>();
        result.forEach(resultList::add);
        assertEquals(2, resultList.size());

        assertEquals("David", resultList.get(0).getFirstName());
        assertEquals("Plamon", resultList.get(0).getLastName());

        assertEquals("Jeanne", resultList.get(1).getFirstName());
        assertEquals("Belleville", resultList.get(1).getLastName());

    }
	
	@Test
	public void getOneMedicalRecordTest() {
		// Arrange
        String firstName = "David";
        String lastName = "Plamon";
        MedicalRecord expectedMedicalRecord = new MedicalRecord();
        expectedMedicalRecord.setFirstName(firstName);
        expectedMedicalRecord.setLastName(lastName);

        when(mr.getOneMedicalRecord(firstName, lastName))
                .thenReturn(expectedMedicalRecord);

        // Act
        MedicalRecord result = ms.getOneMedicalRecord(firstName, lastName);

        // Assert
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
	}
	
	@Test
	public void addMedicalRecordTest() {
		// Arrange
        MedicalRecord recordToAdd = new MedicalRecord();
        recordToAdd.setFirstName("David");
        recordToAdd.setLastName("Jeanne");

        when(mr.addMedicalRecord(any(MedicalRecord.class))).thenReturn(recordToAdd);

        // Act
        MedicalRecord result = ms.addMedicalRecord(recordToAdd);

        // Assert
        assertNotNull(result);
        assertEquals("David", result.getFirstName());
        assertEquals("Jeanne", result.getLastName());
	}
	
	@Test
	public void updateMedicalRecordTest() {
		// Arrange
        String firstNameToUpdate = "David";
        String lastNameToUpdate = "Plamon";

        MedicalRecord existingRecord = new MedicalRecord();
        existingRecord.setFirstName(firstNameToUpdate);
        existingRecord.setLastName(lastNameToUpdate);
        existingRecord.setBirthdate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Medication> medicationList1 = new ArrayList<>();
        Medication medication1 = new Medication();
        medication1.setMedicationName("Red Pills");
        medication1.setQuantityInMg("10mg");
        medicationList1.add(medication1);
        existingRecord.setMedications(medicationList1);

        List<String> allergiesList1 = new ArrayList<>();
        allergiesList1.add("Pollen");
        existingRecord.setAllergies(allergiesList1);

        MedicalRecord updateDetails = new MedicalRecord();
        updateDetails.setFirstName(firstNameToUpdate);
        updateDetails.setLastName(lastNameToUpdate);
        updateDetails.setBirthdate(Date.from(LocalDate.of(2005, 5, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Medication> medicationList2 = new ArrayList<>();
        Medication medication2 = new Medication();
        medication2.setMedicationName("Blue Pills");
        medication2.setQuantityInMg("100mg");
        medicationList2.add(medication2);
        updateDetails.setMedications(medicationList2);

        List<String> allergiesList2 = new ArrayList<>();
        allergiesList2.add("Fish");
        updateDetails.setAllergies(allergiesList2);

        when(mr.updateMedicalRecord(firstNameToUpdate, lastNameToUpdate, updateDetails))
                .thenReturn(updateDetails);

        // Act
        MedicalRecord result = ms.updateMedicalRecord(firstNameToUpdate, lastNameToUpdate, updateDetails);

        // Assert
        assertNotNull(result);
        assertEquals(firstNameToUpdate, result.getFirstName());
        assertEquals(lastNameToUpdate, result.getLastName());
        assertEquals(updateDetails.getBirthdate(), result.getBirthdate());
        assertEquals(updateDetails.getMedications(), result.getMedications());
        assertEquals(updateDetails.getAllergies(), result.getAllergies());

	}
	
	@Test
    void deleteMedicalRecordTest() {
        // Arrange
        String firstNameToDelete = "David";
        String lastNameToDelete = "Plamon";

        when(mr.deleteMedicalRecord(firstNameToDelete, lastNameToDelete))
                .thenReturn(true);

        // Act
        Boolean result = ms.deleteMedicalRecord(firstNameToDelete, lastNameToDelete);

        // Assert
        assertTrue(result);
        verify(mr, times(1)).deleteMedicalRecord(firstNameToDelete, lastNameToDelete);
    }
	
	 @Test
	    void testGetChildMedicalRecordTest() {
	        // Arrange
	        List<MedicalRecord> childRecords = new ArrayList<>();
	        MedicalRecord child1 = new MedicalRecord();
	        child1.setFirstName("Mathilde");
	        child1.setLastName("Jackson");
	        child1.setBirthdate(new Date());
	        childRecords.add(child1);

	        MedicalRecord child2 = new MedicalRecord();
	        child2.setFirstName("Ben");
	        child2.setLastName("Boulot");
	        child2.setBirthdate(new Date()); 
	        childRecords.add(child2);

	        when(mr.getChildMedicalRecord()).thenReturn(childRecords);

	        // Act
	        List<MedicalRecord> result = ms.getChildMedicalRecord();

	        // Assert
	        assertEquals(2, result.size());
	        assertEquals("Mathilde", result.get(0).getFirstName());
	        assertEquals("Jackson", result.get(0).getLastName());
	        assertEquals("Ben", result.get(1).getFirstName());
	        assertEquals("Boulot", result.get(1).getLastName());
	    }

}
