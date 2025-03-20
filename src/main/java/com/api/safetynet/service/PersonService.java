package com.api.safetynet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.safetynet.model.Firestation;
import com.api.safetynet.model.MedicalRecord;
import com.api.safetynet.model.Person;
import com.api.safetynet.model.DTO.ChildInfoDTO;
import com.api.safetynet.model.DTO.FamilyMemberDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoWithMedicalRecordsDTO;
import com.api.safetynet.model.DTO.PersonNearFireStationDTO;
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
	
	public int calculatePersonAge(Date birthDate) {

		return personRepository.calculatePersonAge(birthDate);
	}
	
	public List<Person> getAllPersonsFromSameAddress(String address) {
		return personRepository.getAllPersonsFromSameAddress(address);
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
	
	public Set<String> getPersonEmailByCity(String cityToFind){
		
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
	
	public List<PersonInfoDTO> getListOfPersonInformationByLastName(String lastNameToFind){
		
		//Attribute
		List<Person>personsByLastName = personRepository.findAllPersonsByLastName(lastNameToFind);
		List<PersonInfoDTO>personsByLastNameList = new ArrayList<>();
					
		for(Person person : personsByLastName) {

			MedicalRecord medicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName());
			PersonInfoDTO personInfo = new PersonInfoDTO();
			
			//Mapping of DTO (May be used with MapStruct)
			personInfo.setLastName(person.getLastName());
			personInfo.setEmail(person.getEmail());
			personInfo.setAddress(person.getAddress());
			
			personInfo.setBirthdate(medicalRecord.getBirthdate());
			personInfo.setMedications(medicalRecord.getMedications());
			personInfo.setAllergies(medicalRecord.getAllergies());
			
			personsByLastNameList.add(personInfo);
		}
	
		return personsByLastNameList;
	}
	
	public List<ChildInfoDTO>getChildInformationsByAddress(String addressToFind){
		
		List<ChildInfoDTO>childsList = new ArrayList<>();
		List<Person> allPersonsFromSameAddress = getAllPersonsFromSameAddress(addressToFind);
		
		//Take all persons from address.
		for(Person person : allPersonsFromSameAddress) {
			MedicalRecord personMedicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName()); //For each person, take medical informations.
						
			int age = calculatePersonAge(personMedicalRecord.getBirthdate()); //Calculate person's age.
			
			if(age <= 18) { //If the person is minor, then add to child list.
				ChildInfoDTO childInfo = new ChildInfoDTO();
				
				
				//Mapping of DTO (May be used with MapStruct)
				childInfo.setFirstName(person.getFirstName()); //Set child's first name
				childInfo.setLastName(person.getLastName()); //Set child's last name
				childInfo.setAge(age); //Set child's age
				
				List<FamilyMemberDTO>memberFamilyList = new ArrayList<>();
					//adding family members
					for(Person familyMember : allPersonsFromSameAddress) {
						
						if(familyMember.getFirstName() != childInfo.getFirstName()  && familyMember.getLastName() != childInfo.getLastName()) {
							FamilyMemberDTO familyMemberInfo = new FamilyMemberDTO();
							
							familyMemberInfo.setFirstName(familyMember.getFirstName());
							familyMemberInfo.setLastName(familyMember.getLastName());
							
							MedicalRecord familyMemberMedicalRecord = medicalRecordService.getOneMedicalRecord(familyMemberInfo.getFirstName(),familyMemberInfo.getLastName());
							int familyMemberAge = calculatePersonAge(familyMemberMedicalRecord.getBirthdate());
							familyMemberInfo.setAge(familyMemberAge);
							
							memberFamilyList.add(familyMemberInfo);
						}
						
						childInfo.setFamilyMember(memberFamilyList);
					}
				
				childsList.add(childInfo);

			}
					
		}
		
		return childsList;
		
	}
	
	public List<PersonNearFireStationDTO> getPersonsAndFireStationNumberByAddress (String address){
		
		List<PersonNearFireStationDTO> listOfPersonNearStation = new ArrayList<>();
		
		for(Person person : getAllPersonsFromSameAddress(address)) { //Listing all person from address.
			
			PersonNearFireStationDTO personNearFireStation = new PersonNearFireStationDTO();
			
			MedicalRecord personMedicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName());
			
			//Mapping of DTO (May be used with MapStruct)
			personNearFireStation.setLastName(personMedicalRecord.getLastName());
			personNearFireStation.setAge(calculatePersonAge(personMedicalRecord.getBirthdate()));
			personNearFireStation.setPhoneNumber(person.getPhone());
			personNearFireStation.setMedications(personMedicalRecord.getMedications());
			personNearFireStation.setAllergies(personMedicalRecord.getAllergies());
			personNearFireStation.setFireStationNumber(firestationService.getFirestationNumberByAddress(address));;
			
			listOfPersonNearStation.add(personNearFireStation);

		}
		
		return listOfPersonNearStation;
		
	}
	
	public List<HouseNearFireStationDTO> getAllHousesServedByFireStationNumber (List<Integer> listOfStationNumber){
		
		List<HouseNearFireStationDTO> listOfHouseServedbyStation = new ArrayList<HouseNearFireStationDTO>();
		
		for(Firestation firestation : firestationService.getAllFirestationByStationNumber(listOfStationNumber)){ //List of fire station.
			HouseNearFireStationDTO houseServedByStation = new HouseNearFireStationDTO();
			List<PersonInfoWithMedicalRecordsDTO> listOfPerson = new ArrayList<PersonInfoWithMedicalRecordsDTO>();
			
			for(PersonNearFireStationDTO personNearFirestation : getPersonsAndFireStationNumberByAddress(firestation.getAddress())){
				PersonInfoWithMedicalRecordsDTO personWhoLives = new PersonInfoWithMedicalRecordsDTO();
				
				personWhoLives.setLastName(personNearFirestation.getLastName());
				personWhoLives.setAge(personNearFirestation.getAge());
				personWhoLives.setPhoneNumber(personNearFirestation.getPhoneNumber());
				personWhoLives.setMedications(personNearFirestation.getMedications());
				personWhoLives.setAllergies(personNearFirestation.getAllergies());
				
				listOfPerson.add(personWhoLives);
				
			}
			
			houseServedByStation.setAddress(firestation.getAddress());
			houseServedByStation.setResident(listOfPerson);
			listOfHouseServedbyStation.add(houseServedByStation);
						
		}
		
		return listOfHouseServedbyStation;
		
	}
	
}
