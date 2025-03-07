package com.api.safetynet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.repository.PersonRepository;
import lombok.Data;

@Data
@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private FirestationService firestationService;
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	
	public Iterable<Person> getAllPerson() {
		return personRepository.findAllPersons();
	}
	
	public Person getOnePerson(final String firstName, final String lastName) {
		return personRepository.getOnePerson(firstName, lastName);
	}
	
	public Person addPerson(Person person) {
		Person addedPerson = personRepository.addPerson(person);
		return addedPerson;
	}

	public void deletePerson(final String firstName, final String lastName) {
		personRepository.deletePerson(firstName, lastName);
	}
	
	public Set<String> getPersonPhoneNumberByFirestation(int stationNumber){
		
		//Attribute
		List<Firestation> firestationByNumber = new ArrayList<>();
		Set<String> personPhoneWithoutDouble = new HashSet<>(); //HashSet to void double reg
		
		//Only Take fire station by station's number. (We have addresses that we will compare to person's addresses)
		for(Firestation fireStation : firestationService.getAllFirestation()) {
			if(fireStation.getStation().equals(stationNumber)) {
				firestationByNumber.add(fireStation);
			}
		}
		
		//By fire station's address, look all person's address and put it in personByAddress.
		for (Firestation firestationFounded : firestationByNumber) {
			for (Person personAddressFinder : getAllPerson()){
				if(personAddressFinder.getAddress().toString().equals(firestationFounded.getAddress().toString()))
				personPhoneWithoutDouble.add(personAddressFinder.getPhone());
			}
		}
						
		return personPhoneWithoutDouble;
	}
	
	public Set<String> getPersonEmailByCity(@RequestParam("city") String cityToFind){
		
		//Attribute
		Set<String>personEmail = new HashSet<>();

		//Only take person that their city is equal to city from parameter
		for (Person personCityFinder : getAllPerson()) {
			if(personCityFinder.getCity().toString().equals(cityToFind)) {
				personEmail.add(personCityFinder.getEmail());
			}
		}
		
		return personEmail;
	}
	
	public List<PersonInfoDTO> getPersonInformationByName(@RequestParam("lastName") String lastNameToFind){
		
		//Attribute
		List<Person>personByLastName = new ArrayList<>();
		List<MedicalRecord>medicalRecordByLastName = new ArrayList<>();
		List<PersonInfoDTO>personsByLastNameList = new ArrayList<>();
		
		for (Person personLastNameFinder : getAllPerson()) {
			if(personLastNameFinder.getLastName().toString().equals(lastNameToFind)) {
				personByLastName.add(personLastNameFinder);
			}
		}
		
		//Take all medical records by last name
		for (MedicalRecord personInformation : medicalRecordService.getAllMedicalRecord()) { 
			if(personInformation.getLastName().equals(lastNameToFind)) {
				medicalRecordByLastName.add(personInformation);
			}
		}
		
		for (int i = 0; i < medicalRecordByLastName.size() ; i++) {
			
			//Create DTO object
			PersonInfoDTO personInfo = new PersonInfoDTO();
			
			//Set informations to Object by using index from person and medical record lists.
			personInfo.setLastName(personByLastName.get(i).getLastName());
			personInfo.setBirthdate(medicalRecordByLastName.get(i).getBirthdate());
			personInfo.setEmail(personByLastName.get(i).getEmail());
			personInfo.setAddress(personByLastName.get(i).getAddress());
			personInfo.setMedications(medicalRecordByLastName.get(i).getMedications());
			personInfo.setAllergies(medicalRecordByLastName.get(i).getAllergies());
			
			personsByLastNameList.add(personInfo);
		}
			
		return personsByLastNameList;
	}
	
	
}
