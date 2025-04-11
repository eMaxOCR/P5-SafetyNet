package com.api.safetynet;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.api.safetynet.controller.SafetyController;
import com.api.safetynet.model.Medication;
import com.api.safetynet.model.DTO.ChildInfoDTO;
import com.api.safetynet.model.DTO.FamilyMemberDTO;
import com.api.safetynet.model.DTO.GroupOfPersonNearFireStationDTO;
import com.api.safetynet.model.DTO.GroupOfPersonServedByFireStationDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoWithMedicalRecordsDTO;
import com.api.safetynet.model.DTO.PersonNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonServedByFireStationDTO;
import com.api.safetynet.service.PersonService;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(SafetyController.class)
public class SafetyControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	PersonService ps;
		
	/*/phonealert*/
	@Test	
	public void getPersonPhoneNumberByFirestationTest() throws Exception {
		//GIVEN
		Set<String> listOffPhoneNumber = new HashSet<>(); 
		listOffPhoneNumber.add("0633669988");
		listOffPhoneNumber.add("0655441122"); 
		
		//WHEN
		when(ps.getPersonPhoneNumberByFirestation(anyInt())).thenReturn(listOffPhoneNumber); //When we use getAllFirestation() method, it should return a list of fire station.
		
		//THEN
		this.mockMvc.perform(get("/phonealert")
				.param("firestation", "1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())													//Check that response OK
			.andExpect(jsonPath("$.[0]").value("0633669988"))	
			.andExpect(jsonPath("$.[1]").value("0655441122"));
	}
	
	@Test	
	public void getPersonPhoneNumberByFirestationFailedTest() throws Exception {
		//GIVEN
		Set<String> listOffPhoneNumber = new HashSet<>(); 
		
		//WHEN
		when(ps.getPersonPhoneNumberByFirestation(anyInt())).thenReturn(listOffPhoneNumber); //When we use getAllFirestation() method, it should return a list of fire station.
		
		//THEN
		this.mockMvc.perform(get("/phonealert")
				.param("firestation", "5")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	/*/communityemail*/
	@Test	
	public void getPersonEmailByCityTest() throws Exception {
		//GIVEN
		Set<String> listOffPhoneMails = new HashSet<>(); 
		listOffPhoneMails.add("test1@google.com");
		listOffPhoneMails.add("test2@google.com");
		
		//WHEN
		when(ps.getPersonEmailByCity(anyString())).thenReturn(listOffPhoneMails);
		
		//THEN
		this.mockMvc.perform(get("/communityemail")
				.param("city", "Paris")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$.[0]").value("test1@google.com"))	
		.andExpect(jsonPath("$.[1]").value("test2@google.com"));
	}
	
	@Test	
	public void getPersonEmailByCityFailedTest() throws Exception {
		//GIVEN
		Set<String> listOffPhoneMails = new HashSet<>(); 
		
		//WHEN
		when(ps.getPersonEmailByCity(anyString())).thenReturn(listOffPhoneMails);
		
		//THEN
		this.mockMvc.perform(get("/communityemail")
				.param("city", "Paris")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	/*/personinfolastname*/
	@Test	
	public void getPersonInformationByNameTest() throws Exception {
		//GIVEN
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();
		
		//Create first info
		PersonInfoDTO personInfoDTO1 = new PersonInfoDTO();
		personInfoDTO1.setLastName("Boyd");
		personInfoDTO1.setAddress("1509 Culver St");
		personInfoDTO1.setBirthdate(new Date(03/06/1984));
		personInfoDTO1.setEmail("jaboyd@email.com");
		
		Medication medication1 = new Medication();
		medication1.setMedicationName("paracetamol ");
		medication1.setQuantityInMg("500mg");
		List<Medication> medicationList = new ArrayList<>();
		medicationList.add(medication1);
		personInfoDTO1.setMedications(medicationList);
		
		List<String> allergiesList = new ArrayList<>();
		allergiesList.add("Pollen");
		personInfoDTO1.setAllergies(allergiesList);
		
		listPersonInfoDTO.add(personInfoDTO1);
		
		//Create second info (Not good lastName on purpose to verify that only "Boyd" is check)
		PersonInfoDTO personInfoDTO2 = new PersonInfoDTO();
		personInfoDTO2.setLastName("Kleyn");
		personInfoDTO2.setAddress("1509 Culver St");
		personInfoDTO2.setBirthdate(new Date(03/06/1984));
		personInfoDTO2.setEmail("faboyd@email.com");
		
		Medication medication2 = new Medication();
		medication2.setMedicationName("bluepills ");
		medication2.setQuantityInMg("500g");
		List<Medication> medicationList2 = new ArrayList<>();
		medicationList2.add(medication2);
		personInfoDTO2.setMedications(medicationList2);
		
		List<String> allergiesList2 = new ArrayList<>();
		allergiesList2.add("fish");
		personInfoDTO2.setAllergies(allergiesList2);
		
		listPersonInfoDTO.add(personInfoDTO2);
		
		//WHEN
		when(ps.getListOfPersonInformationByLastName(anyString())).thenReturn(listPersonInfoDTO);
		
		//THEN
		this.mockMvc.perform(get("/personinfolastname")
				.param("lastName", "Boyd")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$.[0].lastName").value("Boyd"))			//Unique data
		.andExpect(jsonPath("$.[0].address").value("1509 Culver St"))	//Unique data
		.andExpect(jsonPath("$.[*].birthdate").isNotEmpty())			//Can have different data
		.andExpect(jsonPath("$.[*].email").isNotEmpty())				//Can have different data
		.andExpect(jsonPath("$.[*].medications").isNotEmpty())			//Can have different data
		.andExpect(jsonPath("$.[*].allergies").isNotEmpty());			//Can have different data
	}
	
	@Test	
	public void getPersonInformationByNameFaledTest() throws Exception {
		//GIVEN
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();
		//WHEN
		when(ps.getListOfPersonInformationByLastName(anyString())).thenReturn(listPersonInfoDTO);
		
		//THEN
		this.mockMvc.perform(get("/personinfolastname")
				.param("lastName", "Boyd")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	/*/childalert*/
	@Test	
	public void getChildInformationsByAddressTest() throws Exception {
		//GIVEN
		List<ChildInfoDTO> listChildInfoDTO = new ArrayList<>();
		List<FamilyMemberDTO> listParent = new ArrayList<>();
		
		//create parent info
		FamilyMemberDTO parent = new FamilyMemberDTO();
		parent.setFirstName("Cedric");
		parent.setLastName("Boyd");
		parent.setBirthdate(new Date(11/03/1980));
		parent.setAge(ps.calculatePersonAge(parent.getBirthdate()));
		listParent.add(parent);
		
		//create first child
		ChildInfoDTO childInfoDTO = new ChildInfoDTO();
		childInfoDTO.setFirstName("Tenley");
		childInfoDTO.setLastName("Boyd");
		childInfoDTO.setBirthdate(new Date(11-03-2020));
		childInfoDTO.setAge(ps.calculatePersonAge(childInfoDTO.getBirthdate()));
		childInfoDTO.setFamilyMember(listParent);
		listChildInfoDTO.add(childInfoDTO);
		
		//create second child
		ChildInfoDTO childInfoDTO2 = new ChildInfoDTO();
		childInfoDTO2.setFirstName("Kimberley");
		childInfoDTO2.setLastName("Boyd");
		childInfoDTO2.setBirthdate(new Date(11-03-2020));
		childInfoDTO2.setAge(ps.calculatePersonAge(childInfoDTO.getBirthdate()));
		childInfoDTO2.setFamilyMember(listParent);
		listChildInfoDTO.add(childInfoDTO2);
		
		//WHEN
		when(ps.getChildInformationsByAddress(anyString())).thenReturn(listChildInfoDTO);
		
		//THEN
		this.mockMvc.perform(get("/childalert")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$.[*].firstName").isNotEmpty())			
		.andExpect(jsonPath("$.[0].lastName").value("Boyd"))			
		.andExpect(jsonPath("$.[*].age").isNotEmpty())				
		.andExpect(jsonPath("$.[*].familyMember").isNotEmpty());		
	}
	
	@Test	
	public void getChildInformationsByAddressFailedTest() throws Exception {
		//GIVEN
		List<ChildInfoDTO> listChildInfoDTO = new ArrayList<>();
		
		//WHEN
		when(ps.getChildInformationsByAddress(anyString())).thenReturn(listChildInfoDTO);
		
		//THEN
		this.mockMvc.perform(get("/childalert")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	/*/fire*/
	@Test	
	public void getPersonsAndFireStationNumberByAddressTest() throws Exception {
		//GIVEN
		GroupOfPersonNearFireStationDTO groupeOfResident = new GroupOfPersonNearFireStationDTO();
		
		List<PersonNearFireStationDTO> listOfPersonNearStation = new ArrayList<>();
		
		//Create first person:
		PersonNearFireStationDTO person1 = new PersonNearFireStationDTO();
		person1.setLastName("Boyd");
		person1.setPhoneNumber("0655998877");
		person1.setAge(18);
		
		Medication medication1 = new Medication();
		medication1.setMedicationName("paracetamol ");
		medication1.setQuantityInMg("500mg");
		List<Medication> medicationList = new ArrayList<>();
		medicationList.add(medication1);
		person1.setMedications(medicationList);
		
		List<String> allergiesList = new ArrayList<>();
		allergiesList.add("Pollen");
		person1.setAllergies(allergiesList);
		
		listOfPersonNearStation.add(person1);
		
		//Create second person
		PersonNearFireStationDTO person2 = new PersonNearFireStationDTO();
		person2.setLastName("Boyd");
		person2.setPhoneNumber("0644551122");
		person2.setAge(23);
		
		Medication medication2 = new Medication();
		medication2.setMedicationName("paracetamol ");
		medication2.setQuantityInMg("500mg");
		List<Medication> medicationList2 = new ArrayList<>();
		medicationList2.add(medication2);
		person1.setMedications(medicationList2);
		person2.setMedications(medicationList2);
		
		List<String> allergiesList2 = new ArrayList<>();
		allergiesList2.add("Pollen");
		person2.setAllergies(allergiesList2);
		
		listOfPersonNearStation.add(person2);
		
		//Set informations to groupeOfResident
		groupeOfResident.setFiresStation(3);
		groupeOfResident.setResidents(listOfPersonNearStation);
		
		//WHEN
		when(ps.getPersonsAndFireStationNumberByAddress(anyString())).thenReturn(groupeOfResident);
		
		//THEN
		this.mockMvc.perform(get("/fire")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$.firesStation").value(3))	
		.andExpect(jsonPath("$.residents").isNotEmpty())		
		.andExpect(jsonPath("$.residents..lastName").exists())		
		.andExpect(jsonPath("$.residents..phoneNumber").isNotEmpty())
		.andExpect(jsonPath("$.residents..age").isNotEmpty())
		.andExpect(jsonPath("$.residents..medications").isNotEmpty())
		.andExpect(jsonPath("$.residents..allergies").isNotEmpty());
		
	}
	@Test	
	public void getPersonsAndFireStationNumberByAddressFailedTest() throws Exception {
		//GIVEN
		GroupOfPersonNearFireStationDTO groupeOfResident = new GroupOfPersonNearFireStationDTO();
		List<PersonNearFireStationDTO> listOfPersonNearStation = new ArrayList<>();
		groupeOfResident.setResidents(listOfPersonNearStation);
		
		//WHEN
		when(ps.getPersonsAndFireStationNumberByAddress(anyString())).thenReturn(groupeOfResident);
		
		//THEN
		this.mockMvc.perform(get("/fire")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound()); 
	}
	
	
	/*/flood/stations*/
	@Test	
	public void getAllHousesServedByFireStationNumberTest() throws Exception {
		//GIVEN	
		List<HouseNearFireStationDTO> listHouseNearFireStation = new ArrayList<>();
		
		HouseNearFireStationDTO houseNearFireStation = new HouseNearFireStationDTO();
		
			List<PersonInfoWithMedicalRecordsDTO> listOfPersons = new ArrayList<>();
			PersonInfoWithMedicalRecordsDTO person = new PersonInfoWithMedicalRecordsDTO();
			
			person.setLastName("David");
			person.setPhoneNumber("0655998877");
			person.setAge(19);
					
			//Add person to first list
			listOfPersons.add(person);
			
			houseNearFireStation.setAddress("644 Gershwin Cir");
			houseNearFireStation.setResident(listOfPersons);
			
			//Add to main list:
			listHouseNearFireStation.add(houseNearFireStation);
			
		HouseNearFireStationDTO houseNearFireStation2 = new HouseNearFireStationDTO();
				
			List<PersonInfoWithMedicalRecordsDTO> listOfPersons2 = new ArrayList<>();
			PersonInfoWithMedicalRecordsDTO person2 = new PersonInfoWithMedicalRecordsDTO();
			
			person2.setLastName("Laurent");
			person2.setPhoneNumber("0655998866");
			person2.setAge(30);
						
			//Add person to first list
			listOfPersons2.add(person2);
			
			houseNearFireStation2.setAddress("908 73rd St");
			houseNearFireStation2.setResident(listOfPersons2);
			
			//Add to main list:
			listHouseNearFireStation.add(houseNearFireStation2);
			
		//WHEN
		List<Integer> stationNumbers = new ArrayList<>();
		stationNumbers.add(1);
		stationNumbers.add(2);
		when(ps.getAllHousesServedByFireStationNumber(stationNumbers)).thenReturn(listHouseNearFireStation);
		
		//THEN
		this.mockMvc.perform(get("/flood/stations")
				.param("stations", "1, 2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].address").value("644 Gershwin Cir"))
		.andExpect(jsonPath("$[0].resident[0].lastName").value("David"))
		.andExpect(jsonPath("$[0].resident[0].phoneNumber").value("0655998877"))
		.andExpect(jsonPath("$[0].resident[0].age").value(19))
		
		.andExpect(jsonPath("$[1].address").value("908 73rd St"))	
		.andExpect(jsonPath("$[1].resident[0].lastName").value("Laurent"))
		.andExpect(jsonPath("$[1].resident[0].phoneNumber").value("0655998866"))
		.andExpect(jsonPath("$[1].resident[0].age").value(30));
		
	}
	
	@Test	
	public void getAllHousesServedByFireStationNumberFailedTest() throws Exception {
		//GIVEN	
		List<HouseNearFireStationDTO> listHouseNearFireStation = new ArrayList<>();
		
		//WHEN
		List<Integer> stationNumbers = new ArrayList<>();
		stationNumbers.add(1);
		stationNumbers.add(2);
		when(ps.getAllHousesServedByFireStationNumber(stationNumbers)).thenReturn(listHouseNearFireStation);
		
		//THEN
		this.mockMvc.perform(get("/flood/stations")
				.param("stations", "1, 2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	//faire un test avec springboottest.
	
	/*/firestation*/
	@Test	
	public void getAllPersonServedByFireStationNumberTest() throws Exception {
		//GIVEN
		GroupOfPersonServedByFireStationDTO groupfPerson = new GroupOfPersonServedByFireStationDTO();
		groupfPerson.setAdultCount(1);
		groupfPerson.setChildCount(1);
		
		List<PersonServedByFireStationDTO> listPersonServed = new ArrayList<>();
		PersonServedByFireStationDTO adult = new PersonServedByFireStationDTO();
		adult.setFirstName("Zoe");
		adult.setLastName("Mouse");
		adult.setAddress("Rue brock");
		adult.setPhoneNumber("0966558877");
		listPersonServed.add(adult);
		
		PersonServedByFireStationDTO child = new PersonServedByFireStationDTO();
		child.setFirstName("Mathlide");
		child.setLastName("Mouse");
		child.setAddress("Rue brock");
		child.setPhoneNumber("0688554477");
		listPersonServed.add(child);
		
		groupfPerson.setResidents(listPersonServed);
		
		//WHEN
		when(ps.getAllPersonServedByFireStationNumber(1)).thenReturn(groupfPerson);
		
		//THEN
		this.mockMvc.perform(get("/firestation")
				.param("stationNumber", "1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		.andExpect(jsonPath("$.residents[1].firstName").value("Mathlide"))
		.andExpect(jsonPath("$.residents[1].lastName").value("Mouse"))
		.andExpect(jsonPath("$.residents[1].address").value("Rue brock"))
		.andExpect(jsonPath("$.residents[1].phoneNumber").value("0688554477"))
		
		.andExpect(jsonPath("$.residents[0].firstName").value("Zoe"))
		.andExpect(jsonPath("$.residents[0].lastName").value("Mouse"))
		.andExpect(jsonPath("$.residents[0].address").value("Rue brock"))
		.andExpect(jsonPath("$.residents[0].phoneNumber").value("0966558877"))	
		
		.andExpect(jsonPath("$.adultCount").value(1))
		.andExpect(jsonPath("$.childCount").value(1));	
		
	}
	
	@Test	
	public void getAllPersonServedByFireStationNumberFailedTest() throws Exception {
		//GIVEN
		GroupOfPersonServedByFireStationDTO groupfPerson = new GroupOfPersonServedByFireStationDTO();		
		List<PersonServedByFireStationDTO> listPersonServed = new ArrayList<>();

		
		groupfPerson.setResidents(listPersonServed);
		
		//WHEN
		when(ps.getAllPersonServedByFireStationNumber(1)).thenReturn(groupfPerson);
		
		//THEN
		this.mockMvc.perform(get("/firestation")
				.param("stationNumber", "1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());	
		
	}
}
