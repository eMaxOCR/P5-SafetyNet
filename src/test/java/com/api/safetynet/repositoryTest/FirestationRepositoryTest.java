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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.repository.DataParsing;
import com.api.safetynet.repository.FirestationRepository;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {

	@InjectMocks
	private FirestationRepository firestationRepository;
	
	@Mock
	private DataParsing dataParsing;
	
	private List<Firestation> firestations;
	
	@BeforeEach
	void setUp() {
		firestations = new ArrayList<>();
	    Firestation fs1 = new Firestation();
	    fs1.setAddress("123 Main St");
	    fs1.setStation("2");
	    firestations.add(fs1);
	
	    Firestation fs2 = new Firestation();
	    fs2.setAddress("456 Oak Ave");
	    fs2.setStation("3");
	    firestations.add(fs2);
	
	    when(dataParsing.parseJsonFirestation()).thenReturn(firestations);
	    firestationRepository.init(); // Initialize the repository with the mocked data
	}
	
	@Test
	void getAllFirestationsTest() {
	    List<Firestation> result = firestationRepository.getAllFirestations();
	    assertEquals(2, result.size());
	    assertEquals("123 Main St", result.get(0).getAddress());
	    assertEquals("2", result.get(0).getStation());
	    assertEquals("456 Oak Ave", result.get(1).getAddress());
	    assertEquals("3", result.get(1).getStation());
	    verify(dataParsing, times(1)).parseJsonFirestation();
	}
	
	@Test
	void getOneFirestationByAddressAndNumberTest() {
	    Firestation result = firestationRepository.getOneFirestationByAddressAndNumber("123 Main St", "2");
	    assertNotNull(result);
	    assertEquals("123 Main St", result.getAddress());
	    assertEquals("2", result.getStation());
	}
	
	@Test
	void getOneFirestationByAddressAndNumberFailedTest() {
	    Firestation result = firestationRepository.getOneFirestationByAddressAndNumber("Nothing", "0");
	    assertNull(result);
	}
	
	@Test
	void getFirestationNumberByAddressTest() {
	    String result = firestationRepository.getFirestationNumberByAddress("123 Main St");
	    assertEquals("2", result);
	}
	
	@Test
	void getFirestationNumberByAddressFailedTest() {
	    String result = firestationRepository.getFirestationNumberByAddress("Nothing");
	    assertEquals("", result);
	}

	@Test
	void getAllFirestationByStationNumberListTest() {
	    Firestation fs3 = new Firestation();
	    fs3.setAddress("999 Willow Dr");
	    fs3.setStation("2");
	    firestations.add(fs3);
	    List<String> stationNumbers = List.of("2");
	    List<Firestation> result = firestationRepository.getAllFirestationByStationNumberList(stationNumbers);
	    assertEquals(2, result.size());
	    assertEquals("123 Main St", result.get(0).getAddress());
	    assertEquals("2", result.get(0).getStation());
	    assertEquals("999 Willow Dr", result.get(1).getAddress());
	    assertEquals("2", result.get(1).getStation());
	}
	
	@Test
	void getAllFirestationByStationNumberTest() {
	    Firestation fs3 = new Firestation();
	    fs3.setAddress("999 Willow Dr");
	    fs3.setStation("2");
	    firestations.add(fs3);
	    List<Firestation> result = firestationRepository.getAllFirestationByStationNumber("2");
	    assertEquals(2, result.size());
	    assertEquals("123 Main St", result.get(0).getAddress());
	    assertEquals("2", result.get(0).getStation());
	    assertEquals("999 Willow Dr", result.get(1).getAddress());
	    assertEquals("2", result.get(1).getStation());
	}
	
	@Test
	void addFirestationTest() {
	    Firestation newFirestation = new Firestation();
	    newFirestation.setAddress("789 Pine Ln");
	    newFirestation.setStation("4");
	    firestationRepository.addFirestation(newFirestation);
	    assertEquals(3, firestations.size());
	    assertTrue(firestations.contains(newFirestation));
	    verify(dataParsing, times(1)).addElementIntoJson(any(), eq("firestations"));
	}
	
	@Test
	void updateFirestationTest() {
	    Firestation updatedFirestation = new Firestation();
	    updatedFirestation.setAddress("123 Main St");
	    updatedFirestation.setStation("5");
	    Firestation result = firestationRepository.updateFirestation("123 Main St", "2", updatedFirestation);
	    assertNotNull(result);
	    assertEquals("123 Main St", result.getAddress());
	    assertEquals("5", result.getStation());
	    verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("firestations"));
	    verify(dataParsing, times(1)).addElementIntoJson(any(), eq("firestations"));
	}
	
	@Test
	void updateFirestationFailedTest() {
	    Firestation updatedFirestation = new Firestation();
	    updatedFirestation.setAddress("123 Main St");
	    updatedFirestation.setStation("5");
	    Firestation result = firestationRepository.updateFirestation("Nothing", "0", updatedFirestation);
	    assertNull(result);
	}
	
	@Test
	void deleteFirestationTest() {
	    assertTrue(firestationRepository.deleteFirestation("123 Main St", "2"));
	    assertEquals(1, firestations.size());
	    assertFalse(firestations.stream().anyMatch(fs -> fs.getAddress().equals("123 Main St") && fs.getStation().equals("2")));
	    verify(dataParsing, times(1)).deleteElementFromJson(anyMap(), eq("firestations"));
	}
	
	@Test
	void deleteFirestationFailedTest() {
	    assertFalse(firestationRepository.deleteFirestation("Nothing", "0"));
	}
	
}

