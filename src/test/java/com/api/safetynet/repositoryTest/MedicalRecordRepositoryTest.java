package com.api.safetynet.repositoryTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.repository.DataParsing;
import com.api.safetynet.repository.MedicalRecordRepository;

@ExtendWith(MockitoExtension.class)
class MedicalRecordRepositoryTest {

	@Mock
    private DataParsing dataParsing;

    @InjectMocks
    private MedicalRecordRepository medicalRecordRepository;

    private List<MedicalRecord> medicalRecords;
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;
    private MedicalRecord childMedicalRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        medicalRecords = new ArrayList<>();

        medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("John");
        medicalRecord1.setLastName("Doe");
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2000, Calendar.JANUARY, 1);
        medicalRecord1.setBirthdate(cal1.getTime());
        Medication medication1_1 = new Medication();
        medication1_1.setMedicationName("aspirin");
        medication1_1.setQuantityInMg("500mg");
        medicalRecord1.setMedications(List.of(medication1_1));
        medicalRecord1.setAllergies(List.of("peanuts"));
        medicalRecords.add(medicalRecord1);

        medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Jane");
        medicalRecord2.setLastName("Smith");
        Calendar cal2 = Calendar.getInstance();
        cal2.set(1995, Calendar.JUNE, 15);
        medicalRecord2.setBirthdate(cal2.getTime());
        Medication medication2_1 = new Medication();
        medication2_1.setMedicationName("paracetamol");
        medication2_1.setQuantityInMg("1g");
        medicalRecord2.setMedications(List.of(medication2_1));
        medicalRecord2.setAllergies(List.of("pollen"));
        medicalRecords.add(medicalRecord2);

        childMedicalRecord = new MedicalRecord();
        childMedicalRecord.setFirstName("Peter");
        childMedicalRecord.setLastName("Pan");
        Calendar calChild = Calendar.getInstance();
        calChild.add(Calendar.YEAR, -10); 
        childMedicalRecord.setBirthdate(calChild.getTime());
        childMedicalRecord.setMedications(new ArrayList<>());
        childMedicalRecord.setAllergies(new ArrayList<>());
        medicalRecords.add(childMedicalRecord);

        when(dataParsing.parseJsonMedicalRecord()).thenReturn(medicalRecords);

        //Initiale repository to use mocked data.
        medicalRecordRepository.init();
    }

    @Test
    void getAllMedicalRecordsTest() {
        List<MedicalRecord> allRecords = medicalRecordRepository.getAllMedicalRecords();
        assertEquals(3, allRecords.size());
        assertTrue(allRecords.contains(medicalRecord1));
        assertTrue(allRecords.contains(medicalRecord2));
        assertTrue(allRecords.contains(childMedicalRecord));
    }

    @Test
    void getOneMedicalRecordTest() {
        MedicalRecord foundRecord = medicalRecordRepository.getOneMedicalRecord("John", "Doe");
        assertEquals(medicalRecord1, foundRecord);
    }
    
    @Test
    void getOneMedicalRecordFailedTest() {
        MedicalRecord foundRecord = medicalRecordRepository.getOneMedicalRecord("No", "Body");
        assertNull(foundRecord);
    }

    @Test
    void getChildMedicalRecordTest() {
        List<MedicalRecord> childrenRecords = medicalRecordRepository.getChildMedicalRecord();
        assertEquals(1, childrenRecords.size());
        assertTrue(childrenRecords.contains(childMedicalRecord));
        assertFalse(childrenRecords.contains(medicalRecord1));
        assertFalse(childrenRecords.contains(medicalRecord2));
    }
    
    @Test
    void getChildMedicalRecordFailedTest() {
        List<MedicalRecord> childrenRecords = null;
        assertNull(childrenRecords);
    }

    @Test
    void addMedicalRecordTest() {
        MedicalRecord newRecord = new MedicalRecord();
        newRecord.setFirstName("Alice");
        newRecord.setLastName("Wonderland");
        Calendar calNew = Calendar.getInstance();
        calNew.set(2010, Calendar.MARCH, 20);
        newRecord.setBirthdate(calNew.getTime());
        newRecord.setMedications(new ArrayList<>());
        newRecord.setAllergies(List.of("cats"));

        MedicalRecord addedRecord = medicalRecordRepository.addMedicalRecord(newRecord);

        assertEquals(4, medicalRecordRepository.getAllMedicalRecords().size());
        assertTrue(medicalRecordRepository.getAllMedicalRecords().contains(newRecord));
        assertEquals(newRecord, addedRecord);
        verify(dataParsing, times(1)).addMedicalRecordIntoJson(newRecord);
    }

    @Test
    void updateMedicalRecordTest() {
        MedicalRecord updatedRecord = new MedicalRecord();
        Calendar calUpdate = Calendar.getInstance();
        calUpdate.set(2001, Calendar.FEBRUARY, 10);
        updatedRecord.setBirthdate(calUpdate.getTime());
        Medication newMedication = new Medication();
        newMedication.setMedicationName("newDrug");
        newMedication.setQuantityInMg("10mg");
        updatedRecord.setMedications(List.of(newMedication));
        updatedRecord.setAllergies(List.of("dust"));

        MedicalRecord result = medicalRecordRepository.updateMedicalRecord("John", "Doe", updatedRecord);

        assertEquals(medicalRecord1.getFirstName(), result.getFirstName());
        assertEquals(medicalRecord1.getLastName(), result.getLastName());
        assertEquals(updatedRecord.getBirthdate(), result.getBirthdate());
        assertEquals(updatedRecord.getMedications().get(0).getMedicationName(), "newDrug");
        assertEquals(updatedRecord.getMedications().get(0).getQuantityInMg(), "10mg");
        assertEquals(updatedRecord.getAllergies(), result.getAllergies());
        verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("medicalrecords"));
        verify(dataParsing, times(1)).addMedicalRecordIntoJson(updatedRecord);
    }

    @Test
    void deleteMedicalRecordTest() {
        boolean deleted = medicalRecordRepository.deleteMedicalRecord("Jane", "Smith");
        assertTrue(deleted);
        assertEquals(2, medicalRecordRepository.getAllMedicalRecords().size());
        assertFalse(medicalRecordRepository.getAllMedicalRecords().contains(medicalRecord2));
        verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("medicalrecords"));
    }
    
    @Test
    void deleteMedicalRecordFailedTest() {
        boolean deleted = medicalRecordRepository.deleteMedicalRecord("No", "Body");
        assertFalse(deleted);
    }

}