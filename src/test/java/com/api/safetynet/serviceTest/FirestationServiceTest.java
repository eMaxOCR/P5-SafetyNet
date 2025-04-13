package com.api.safetynet.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.repository.FirestationRepository;
import com.api.safetynet.service.FirestationService;

@ExtendWith(MockitoExtension.class) 
public class FirestationServiceTest {
	
	@InjectMocks
	FirestationService fs;
	
	@Mock
	FirestationRepository fr;
	
	@Test
	public void getAllFirestationTest() {
		// Arrange
        List<Firestation> firestations = new ArrayList<>();
        Firestation firestation1 = new Firestation();
        firestation1.setAddress("test1");
        firestation1.setStation(1);
        firestations.add(firestation1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("test2");
        firestation2.setStation(2);
        firestations.add(firestation2);

        when(fr.getAllFirestations()).thenReturn(firestations);

        // Act
        Iterable<Firestation> result = fs.getAllFirestation();

        // Assert
        List<Firestation> resultList = new ArrayList<>();
        result.forEach(resultList::add);
        assertEquals(2, resultList.size());
        assertEquals("test1", resultList.get(0).getAddress());
        assertEquals(1, resultList.get(0).getStation());
        assertEquals("test2", resultList.get(1).getAddress());
        assertEquals(2, resultList.get(1).getStation());
    }
	
	@Test
	public void getOneFirestationByAddressAndStationNumberTest() {
		// Arrange
        String address = "test1";
        Integer stationNumber = 1;
        Firestation expectedFirestation = new Firestation();
        expectedFirestation.setAddress(address);
        expectedFirestation.setStation(stationNumber);

        when(fr.getOneFirestationByAddressAndNumber(address, stationNumber))
                .thenReturn(expectedFirestation);

        // Act
        Firestation result = fs.getOneFirestationByAddressAndStationNumber(address, stationNumber);

        // Assert
        assertEquals(address, result.getAddress());
        assertEquals(stationNumber, result.getStation());
    }
	
	@Test
    void testGetFirestationNumberByAddressTest() {
        // Arrange
        String address = "test1";
        int expectedStationNumber = 1;

        when(fr.getFirestationNumberByAddress(address))
                .thenReturn(expectedStationNumber);

        // Act
        int result = fs.getFirestationNumberByAddress(address);

        // Assert
        assertEquals(expectedStationNumber, result);
    }
	
	@Test
    void getAllFirestationByStationNumberListTest() {
		// Arrange
        List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(1);
        stationNumbers.add(2);
        List<Firestation> expectedFirestations = new ArrayList<>();

        Firestation firestation1 = new Firestation();
        firestation1.setAddress("test1");
        firestation1.setStation(1);
        expectedFirestations.add(firestation1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("test2");
        firestation2.setStation(1);
        expectedFirestations.add(firestation2);

        when(fr.getAllFirestationByStationNumberList(stationNumbers))
                .thenReturn(expectedFirestations);

        // Act
        List<Firestation> result = fs.getAllFirestationByStationNumberList(stationNumbers);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getStation());
        assertEquals("test1", result.get(0).getAddress());
        assertEquals(1, result.get(1).getStation());
        assertEquals("test2", result.get(1).getAddress());
    }
	
	@Test
    void getAllFirestationByStationNumberTest() {
		// Arrange
        int stationNumber = 1;
        List<Firestation> expectedFirestations = new ArrayList<>();

        Firestation firestation1 = new Firestation();
        firestation1.setAddress("test1");
        firestation1.setStation(stationNumber);
        expectedFirestations.add(firestation1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("test2");
        firestation2.setStation(stationNumber);
        expectedFirestations.add(firestation2);

        when(fr.getAllFirestationByStationNumber(stationNumber))
                .thenReturn(expectedFirestations);

        // Act
        List<Firestation> result = fs.getAllFirestationByStationNumber(stationNumber);

        // Assert
        assertEquals(2, result.size());
        assertEquals(stationNumber, result.get(0).getStation());
        assertEquals("test1", result.get(0).getAddress());
        assertEquals(stationNumber, result.get(1).getStation());
        assertEquals("test2", result.get(1).getAddress());
	}
	
	@Test
    void addFirestationTest() { 
		// Arrange
        Firestation firestationToAdd = new Firestation();
        firestationToAdd.setAddress("test1");
        firestationToAdd.setStation(4);

        when(fr.addFirestation(any(Firestation.class))).thenReturn(firestationToAdd);

        // Act
        Firestation result = fs.addFirestation(firestationToAdd);

        // Assert
        assertNotNull(result);
        assertEquals("test1", result.getAddress());
        assertEquals(4, result.getStation());
	}
	
	@Test
    void updateFirestationTest() { //TODO To check
		// Arrange
        String addressToUpdate = "test1";
        Integer stationToUpdate = 1;
        Firestation existingFirestation = new Firestation();
        existingFirestation.setAddress(addressToUpdate);
        existingFirestation.setStation(stationToUpdate);

        Firestation updateDetails = new Firestation();
        updateDetails.setStation(5);

        Firestation updatedFirestation = new Firestation();
        updatedFirestation.setAddress(addressToUpdate);
        updatedFirestation.setStation(5);

        when(fr.getOneFirestationByAddressAndNumber(addressToUpdate, stationToUpdate))
                .thenReturn(existingFirestation);

        // Act
        Firestation result = fs.updateFirestation(addressToUpdate, stationToUpdate, updateDetails);

        // Assert
        assertNotNull(result);
        assertEquals(addressToUpdate, result.getAddress());
        assertEquals(5, result.getStation());
        verify(fr, times(1)).getOneFirestationByAddressAndNumber(addressToUpdate, stationToUpdate);
	}
	
	@Test
	void deleteFirestationTest() {
		 // Arrange
        String addressToDelete = "test1";
        Integer stationToDelete = 1;

        when(fr.deleteFirestation(addressToDelete, stationToDelete))
                .thenReturn(true);

        // Act
        Boolean result = fs.deleteFirestation(addressToDelete, stationToDelete);

        // Assert
        assertTrue(result);
        verify(fr, times(1)).deleteFirestation(addressToDelete, stationToDelete);
	}
}
