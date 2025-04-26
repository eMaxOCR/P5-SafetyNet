package com.api.safetynet.repositoryTest;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Medication;
import com.api.safetynet.model.Person;
import com.api.safetynet.repository.DataParsing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataParsingTest {

	@InjectMocks
    public DataParsing dataParsing;
    
    @Mock
    protected ObjectMapper objectMapper;
    
    private File tempFile;
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        //Create temp file for T.U
        tempFile = tempDir.resolve(Paths.get("data2.json")).toFile();
        tempFile.deleteOnExit();

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
        
    }
    

    @Test
    void parseJsonPersonTest() {
        List<Person> persons = dataParsing.parseJsonPerson();
        assertNotNull(persons);
    }

    @Test
    void parseJsonFirestationTest() {
        List<Firestation> firestations = dataParsing.parseJsonFirestation();
        assertNotNull(firestations);
    }

    @Test
    void parseJsonMedicalRecordTest() throws ParseException {
        List<MedicalRecord> medicalRecords = dataParsing.parseJsonMedicalRecord();
        assertNotNull(medicalRecords);
    }

    @Test
    void addElementIntoJsonTest() throws IOException {
    	// Arrange
        String nodeName = "persons";
        when(objectMapper.createObjectNode()).thenReturn(mock(ObjectNode.class));

        ObjectNode newPersonNode = objectMapper.createObjectNode();
        newPersonNode.put("firstName", "David"); 

        ObjectNode rootNodeMock = mock(ObjectNode.class);
        ArrayNode elementNodeMock = mock(ArrayNode.class);
        ObjectWriter objectWriterMock = mock(ObjectWriter.class);

        when(objectMapper.readTree(any(File.class))).thenReturn(rootNodeMock);
        when(rootNodeMock.get(nodeName)).thenReturn(elementNodeMock);
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriterMock);

        // Act
        dataParsing.addElementIntoJson(newPersonNode, nodeName);

        // Assert
        verify(objectMapper, times(1)).readTree(any(File.class));
        verify(rootNodeMock, times(1)).get(nodeName);
        verify(elementNodeMock, times(1)).add(any(ObjectNode.class)); 
        verify(objectWriterMock, times(1)).writeValue(any(File.class), eq(rootNodeMock));
        verify(objectMapper, times(1)).createObjectNode(); 
    }

    @Test
    void deleteElementFromJsonTest() throws IOException {
    	// Arrange
        String nodeName = "persons";
        Map<String, String> personToDelete = new HashMap<>();
        personToDelete.put("firstName", "John");
        personToDelete.put("lastName", "Boyd");

        ObjectNode rootNodeMock = mock(ObjectNode.class);
        ArrayNode elementNodeMock = mock(ArrayNode.class);
        ObjectNode elementToFindMock = mock(ObjectNode.class);

        when(objectMapper.readTree(any(File.class))).thenReturn(rootNodeMock);
        when(rootNodeMock.get(nodeName)).thenReturn(elementNodeMock);

        List<JsonNode> elements = new ArrayList<>();
        elements.add(elementToFindMock);
        when(elementNodeMock.elements()).thenReturn(elements.iterator());

        when(elementToFindMock.get("firstName")).thenReturn(mock(JsonNode.class));
        when(elementToFindMock.get("firstName").asText()).thenReturn("John");
        when(elementToFindMock.get("lastName")).thenReturn(mock(JsonNode.class));
        when(elementToFindMock.get("lastName").asText()).thenReturn("Boyd");

        ObjectWriter objectWriterMock = mock(ObjectWriter.class);
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriterMock);

        // Act
        dataParsing.deleteElementFromJson(personToDelete, nodeName);

        // Assert
        verify(objectMapper, times(1)).readTree(any(File.class));
        verify(rootNodeMock, times(1)).get(nodeName);
        verify(elementToFindMock.get("firstName"), times(1)).asText();
        verify(elementToFindMock.get("lastName"), times(1)).asText();
        // Indirectly verify removal by checking writeValue is called
        verify(objectWriterMock, times(1)).writeValue(any(File.class), eq(rootNodeMock));
    }

    @Test
    void addMedicalRecordIntoJsonTest() throws IOException, ParseException {
    	// Arrange
        MedicalRecord medicalRecordToAdd = new MedicalRecord();
        medicalRecordToAdd.setFirstName("David");
        medicalRecordToAdd.setLastName("Plamon");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        medicalRecordToAdd.setBirthdate(dateFormat.parse("01/01/2000"));
        List<Medication> medications = new ArrayList<>();
        Medication medication = new Medication();
        medication.setMedicationName("aznol");
        medication.setMedicationName("350mg");
        medications.add(medication);
        medicalRecordToAdd.setMedications(medications);
        List<String> allergies = new ArrayList<>();
        allergies.add("nillacilan");
        medicalRecordToAdd.setAllergies(allergies);

        ObjectNode rootNodeMock = mock(ObjectNode.class);
        ArrayNode medicalRecordsNodeMock = mock(ArrayNode.class);
        ObjectNode medicalRecordNodeMock = mock(ObjectNode.class);

        Mockito.when(objectMapper.readTree(Mockito.any(File.class))).thenReturn(rootNodeMock);
        Mockito.when(rootNodeMock.get("medicalrecords")).thenReturn(medicalRecordsNodeMock);
        Mockito.when(objectMapper.createObjectNode()).thenReturn(medicalRecordNodeMock);
        Mockito.when(objectMapper.createArrayNode()).thenReturn(mock(ArrayNode.class));

        ObjectWriter objectWriterMock = mock(ObjectWriter.class);
        Mockito.when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriterMock);

        // Act
        dataParsing.addMedicalRecordIntoJson(medicalRecordToAdd);

        // Assert
        Mockito.verify(objectMapper, Mockito.times(1)).readTree(Mockito.any(File.class));
        Mockito.verify(rootNodeMock, Mockito.times(1)).get("medicalrecords");
        Mockito.verify(objectMapper, Mockito.times(1)).createObjectNode();
        Mockito.verify(objectMapper, Mockito.times(2)).createArrayNode(); 

        Mockito.verify(medicalRecordNodeMock, Mockito.times(1)).put("firstName", "David");
        Mockito.verify(medicalRecordNodeMock, Mockito.times(1)).put("lastName", "Plamon");
        Mockito.verify(medicalRecordNodeMock, Mockito.times(1)).put("birthdate", "01/01/2000");
        Mockito.verify(medicalRecordNodeMock, Mockito.times(1)).set(Mockito.eq("medications"), Mockito.any(ArrayNode.class));
        Mockito.verify(medicalRecordNodeMock, Mockito.times(1)).set(Mockito.eq("allergies"), Mockito.any(ArrayNode.class));

        Mockito.verify(medicalRecordsNodeMock, Mockito.times(1)).add(medicalRecordNodeMock);
        Mockito.verify(objectWriterMock, Mockito.times(1)).writeValue(Mockito.any(File.class), Mockito.eq(rootNodeMock));
    }
}