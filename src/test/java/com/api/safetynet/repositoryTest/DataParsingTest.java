package com.api.safetynet.repositoryTest;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.model.Person;
import com.api.safetynet.repository.DataParsing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DataParsingTest {

    private DataParsing dataParsing;
    private File tempFile;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        //Create temp file for T.U
        tempFile = tempDir.resolve(Paths.get("data.json")).toFile();

        String initialJsonContent = """
                {
                  "persons": [
                    { "firstName": "John", "lastName": "Boyd", "address": "1509 Culver St", "city": "Culver", "zip": "978", "phone": "841-874-6512", "email": "jaboyd@email.com" },
                    { "firstName": "Jacob", "lastName": "Boyd", "address": "1509 Culver St", "city": "Culver", "zip": "978", "phone": "841-874-6513", "email": "drk@email.com" }
                  ],
                  "firestations": [
                    { "address": "1509 Culver St", "station": "3" },
                    { "address": "29 15th St", "station": "2" }
                  ],
                  "medicalrecords": [
                    { "firstName": "John", "lastName": "Boyd", "birthdate": "03/06/1984", "medications": ["aznol:350mg", "hydrapermazol:100mg"], "allergies": ["nillacilan"] },
                    { "firstName": "Jacob", "lastName": "Boyd", "birthdate": "03/06/1989", "medications": ["pharmacol:5000mg", "terazine:10mg"], "allergies": ["shellfish"] }
                  ]
                }
                """;
        Files.writeString(tempFile.toPath(), initialJsonContent);

        // Initialize DataParsing by using temp file
        dataParsing = new DataParsing() {
            @Override
            protected File getJsonDataFile() {
                return tempFile;
            }
        };
    }

    @Test
    void parseJsonPerson_Test() {
        List<Person> persons = dataParsing.parseJsonPerson();
        assertNotNull(persons);
    }

    @Test
    void parseJsonFirestation_Test() {
        List<Firestation> firestations = dataParsing.parseJsonFirestation();
        assertNotNull(firestations);
    }

    @Test
    void parseJsonMedicalRecord_Test() throws ParseException {
        List<MedicalRecord> medicalRecords = dataParsing.parseJsonMedicalRecord();
        assertNotNull(medicalRecords);
    }

    @Test
    void addElementIntoJson_Test() throws IOException {
        ObjectNode newPersonNode = objectMapper.createObjectNode();
        newPersonNode.put("firstName", "David");
        newPersonNode.put("lastName", "Plamon");
        newPersonNode.put("address", "123 soleil");
        newPersonNode.put("city", "Paris");
        newPersonNode.put("zip", "75000");
        newPersonNode.put("phone", "111-222-3333");
        newPersonNode.put("email", "d.plamon@gmail.com");

        dataParsing.addElementIntoJson(newPersonNode, "persons");

        List<Person> persons = dataParsing.parseJsonPerson();
        assertTrue(persons.stream().anyMatch(p -> p.getFirstName().equals("David") && p.getLastName().equals("Plamon")));
    }

    @Test
    void deleteElementFromJson_Test() throws IOException {
        Map<String, String> personToDelete = new HashMap<>();
        personToDelete.put("firstName", "John");
        personToDelete.put("lastName", "Boyd");

        dataParsing.deleteElementFromJson(personToDelete, "persons");

        List<Person> persons = dataParsing.parseJsonPerson();
        assertFalse(persons.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Boyd")));
    }

    @Test
    void addMedicalRecordIntoJson_Test() throws IOException, ParseException {
    	MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setFirstName("David");
        newMedicalRecord.setLastName("Plamon");
        newMedicalRecord.setBirthdate(dateFormat.parse("01/01/2000"));

        List<Medication> medicationsList = new ArrayList<>();
        Medication medication = new Medication();
        medication.setMedicationName("testDrug");
        medication.setQuantityInMg("10mg");
        medicationsList.add(medication);
        newMedicalRecord.setMedications(medicationsList);

        newMedicalRecord.setAllergies(List.of("testAllergy"));

        dataParsing.addMedicalRecordIntoJson(newMedicalRecord);

        List<MedicalRecord> medicalRecords = dataParsing.parseJsonMedicalRecord();
        assertTrue(medicalRecords.stream().anyMatch(mr ->
                mr.getFirstName().equals("David") &&
                mr.getLastName().equals("Plamon") &&
                dateFormat.format(mr.getBirthdate()).equals("01/01/2000") &&
                mr.getMedications().stream().anyMatch(m -> m.getMedicationName().equals("testDrug") && m.getQuantityInMg().equals("10mg")) &&
                mr.getAllergies().contains("testAllergy")
        ));
    }
}