package com.api.safetynet.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.api.safetynet.model.DTO.GroupOfPersonNearFireStationDTO;
import com.api.safetynet.model.DTO.GroupOfPersonServedByFireStationDTO;
import com.api.safetynet.model.DTO.HouseNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonInfoDTO;
import com.api.safetynet.model.DTO.PersonInfoWithMedicalRecordsDTO;
import com.api.safetynet.model.DTO.PersonNearFireStationDTO;
import com.api.safetynet.model.DTO.PersonServedByFireStationDTO;
import com.api.safetynet.repository.PersonRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public Person updatePerson(String firstName, String lastName, Person person) {

		return personRepository.updatePerson(firstName, lastName, person);
	}

	public Boolean deletePerson(final String firstName, final String lastName) {
		return personRepository.deletePerson(firstName, lastName);
	}
	
	/**
	 * Calculate person's age with birth date
	 * @param Date
	 * @Return age 
	 * */
	public int calculatePersonAge(final Date birthDateVar) {
		log.debug("Calculate age with birthdate {}.", birthDateVar);
		Calendar birthDate = Calendar.getInstance();
		birthDate.setTime(birthDateVar);
		Calendar todayDate = Calendar.getInstance();
		
		int age = todayDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		log.debug("Calculated age : {} ans.", age);
		return age;
	}
	
	public List<Person> getAllPersonsFromSameAddress(String address) {
		return personRepository.getAllPersonsFromSameAddress(address);
	}
	
	/**
	 *Return a list of phone numbers of residents served by the fire station. 
	 *Used to send emergency text messages to specific households.
	 *@param station's number
	 * */
	public Set<String> getPersonPhoneNumberByFirestation(String stationNumber){
		
		//Attribute
		List<Firestation> firestationByNumber = new ArrayList<>();
		Set<String> personPhoneWithoutDouble = new HashSet<>(); //HashSet to void double reg
		
		//Only Take fire station by station's number. (We have addresses that we will compare to person's addresses)
		for(Firestation fireStation : firestationService.getAllFirestation()) {
			if(fireStation.getStation().equals(stationNumber)) {
				firestationByNumber.add(fireStation);
			}
		}
		log.debug("Taking fire station by station's number : {}", firestationByNumber);
		
		//By fire station's address, look all person's address and put it in personByAddress.
		for (Firestation firestationFounded : firestationByNumber) {
			for (Person personAddressFinder : getAllPerson()){
				if(personAddressFinder.getAddress().toString().equals(firestationFounded.getAddress().toString()))
				personPhoneWithoutDouble.add(personAddressFinder.getPhone());
			}
		}
		log.debug("List of phone numbers found:  {}", personPhoneWithoutDouble);		
		return personPhoneWithoutDouble;
	}
	
	
	/**
	 * Return the email addresses of all the residents in the city
	 * @param City
	 * @return List of email
	 * */
	public Set<String> getPersonEmailByCity(String cityToFind){
		
		//Attribute
		Set<String>personEmail = new HashSet<>();
		
		log.debug("Searching person's mail who lives in {}.", cityToFind);
		//Only take person that their city is equal to city from parameter
		for (Person personCityFinder : getAllPerson()) {
			if(personCityFinder.getCity().toString().equals(cityToFind)) {
				personEmail.add(personCityFinder.getEmail());
			}
		}
		log.debug("List of mails found:  {}", personEmail);
		return personEmail;
	}
	
	/**
	 * return the name, address, age, email address, and medical history (medications with dosage, and allergies) of each resident.
	 * @param Last name
	 * @return List of person
	 * */
	public List<PersonInfoDTO> getListOfPersonInformationByLastName(String lastNameToFind){
		
		//Attribute
		List<Person>personsByLastName = personRepository.findAllPersonsByLastName(lastNameToFind);
		List<PersonInfoDTO>personsByLastNameList = new ArrayList<>();
				
		log.debug("Searching list of {}'s informations", lastNameToFind);
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
		log.debug("List of person's informations : {}", lastNameToFind);
		return personsByLastNameList;
	}
	
	/**
	 * Return a list of children (any individual aged 18 or under) living at that address. 
	 * The list include the first and last name of each child, their age, and a list of other household members. 
	 * @param Address
	 * @return List of children
	 * */
	public List<ChildInfoDTO>getChildInformationsByAddress(String addressToFind){
		
		List<ChildInfoDTO>childsList = new ArrayList<>();
		List<Person> allPersonsFromSameAddress = getAllPersonsFromSameAddress(addressToFind);
		
		log.debug("Searching childrend living at {}", addressToFind);
		//Take all persons from address.
		for(Person person : allPersonsFromSameAddress) {
			MedicalRecord personMedicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName()); //For each person, take medical informations.
			
			int age = calculatePersonAge(personMedicalRecord.getBirthdate()); //Calculate person's age.
			
			if(age <= 18) { //If the person is minor, then add to child list.
				ChildInfoDTO childInfo = new ChildInfoDTO();
				
				//adding family members
				for(Person familyMember : allPersonsFromSameAddress) {
					
					if(!familyMember.equals(person)) {
						FamilyMemberDTO familyMemberInfo = new FamilyMemberDTO();
						
						familyMemberInfo.setFirstName(familyMember.getFirstName());
						familyMemberInfo.setLastName(familyMember.getLastName());
						
						MedicalRecord familyMemberMedicalRecord = medicalRecordService.getOneMedicalRecord(familyMemberInfo.getFirstName(),familyMemberInfo.getLastName());
						int familyMemberAge = calculatePersonAge(familyMemberMedicalRecord.getBirthdate());
						familyMemberInfo.setAge(familyMemberAge);
						
						childInfo.getFamilyMember().add(familyMemberInfo);
					}
					
				}
				
				//Mapping of DTO (May be used with MapStruct)
				childInfo.setFirstName(person.getFirstName()); //Set child's first name
				childInfo.setLastName(person.getLastName()); //Set child's last name
				childInfo.setAge(age); //Set child's age
			
				childsList.add(childInfo);

			}
					
		}
		log.debug("List of children living at {} : {}", addressToFind, childsList);
		return childsList;
		
	}

	
	/**
	 * Return the list of residents living at the given address along with the number of the fire station serving it. 
	 * The list include the name, phone number, age, and medical history (medications with dosage, and allergies) of each person.
	 * @param address
	 * @return Group of person near fire station.
	 * */
	public GroupOfPersonNearFireStationDTO getPersonsAndFireStationNumberByAddress (String address){
		
		GroupOfPersonNearFireStationDTO groupOfPerson = new GroupOfPersonNearFireStationDTO(); //Collect informations that will be return object that contain a list of Person and fire station number
		
		List<PersonNearFireStationDTO> listOfPersons = new ArrayList<>();//During process, collect one by one persons
		
		log.debug("Searching persons and firestations by address : {}.", address);
		for(Person person : getAllPersonsFromSameAddress(address)) { //Listing all person from address.
			
			PersonNearFireStationDTO personNearFireStation = new PersonNearFireStationDTO(); //Object that will be collect person informations
			
			MedicalRecord personMedicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName()); //Get birth date
			
			//Mapping of DTO (May be used with MapStruct)
			personNearFireStation.setLastName(personMedicalRecord.getLastName());					//Put lastName
			personNearFireStation.setAge(calculatePersonAge(personMedicalRecord.getBirthdate()));	//Put age
			personNearFireStation.setPhoneNumber(person.getPhone());								//Put phone number
			personNearFireStation.setMedications(personMedicalRecord.getMedications());				//Put medications[]
			personNearFireStation.setAllergies(personMedicalRecord.getAllergies());					//Put allergies[]
			
			listOfPersons.add(personNearFireStation); //Add the person to the list of persons.

		}
		
		groupOfPerson.setResidents(listOfPersons); //Set to main object the complete list of persons
		groupOfPerson.setFiresStation(firestationService.getFirestationNumberByAddress(address)); //Set fire station number
		log.debug("Liste of informations found : {}.", groupOfPerson);
		return groupOfPerson; 
		
	}
	
	/**
	 * Return a list of all households served by the fire station. 
	 * Grouped people by address it include the name, phone number, and age of the residents, and list their medical history (medications with dosage, and allergies) next to each name.
	 * @return Return a list of all homes served by the fire station
	 * @param List of station number (eg : /stations?2,4)
	 */
	public List<HouseNearFireStationDTO> getAllHousesServedByFireStationNumber (List<String> listOfStationNumber){
		
		List<HouseNearFireStationDTO> listOfHouseServedbyStation = new ArrayList<>();//Main collector
		
		log.debug("Searching houses served by fire station's number : {}.", listOfStationNumber);
		for(Firestation firestation : firestationService.getAllFirestationByStationNumberList(listOfStationNumber)){ //List of fire station.
			HouseNearFireStationDTO houseServedByStation = new HouseNearFireStationDTO(); //Collector
			List<PersonInfoWithMedicalRecordsDTO> listOfPerson = new ArrayList<>();//Temp collector
						
			for(Person personNearFirestation : getAllPersonsFromSameAddress(firestation.getAddress())){
				PersonInfoWithMedicalRecordsDTO resident = new PersonInfoWithMedicalRecordsDTO();
				MedicalRecord medicalRecord = medicalRecordService.getOneMedicalRecord(personNearFirestation.getFirstName(),personNearFirestation.getLastName());
				
				//Mapping of DTO (May be used with MapStruct)
				resident.setLastName(personNearFirestation.getLastName());
				resident.setAge(calculatePersonAge(medicalRecord.getBirthdate()));
				resident.setPhoneNumber(personNearFirestation.getPhone());
				resident.setMedications(medicalRecord.getMedications());
				resident.setAllergies(medicalRecord.getAllergies());
				
				listOfPerson.add(resident);
				
			}
			
			houseServedByStation.setAddress(firestation.getAddress());
			houseServedByStation.setResident(listOfPerson);
			listOfHouseServedbyStation.add(houseServedByStation);
						
		}
		log.debug("Liste of houses served by fire station's number : {}.", listOfHouseServedbyStation);
		return listOfHouseServedbyStation;
		
	}
	
	/**
	 * Return a list of people covered by the corresponding fire station. 
	 * So, if the station number = 1, it should return the 1  residents covered by station number 1. 
	 * The list  include the following specific information: first name, last name, address, phone number. 
	 * In addition, 2 provide a count of the number of adults and the number of children (any individual aged 18 or under) in the served area.
	 * @return Return a list of all persons served by the fire station
	 * @param Station number (eg: 1)
	 */
	public GroupOfPersonServedByFireStationDTO getAllPersonServedByFireStationNumber (final String stationNumber){
		
		GroupOfPersonServedByFireStationDTO groupOfPerson = new GroupOfPersonServedByFireStationDTO();
		List<PersonServedByFireStationDTO> personDTOList = new ArrayList<PersonServedByFireStationDTO>();
		
		log.debug("Searching all person served by fire station's number : {}.", stationNumber);
		for(Firestation addressServedByFireStation : firestationService.getAllFirestationByStationNumber(stationNumber)){ //List of fire address station.
			
			
			for(Person person : getAllPersonsFromSameAddress(addressServedByFireStation.getAddress())){
				PersonServedByFireStationDTO personDTO = new PersonServedByFireStationDTO();
				MedicalRecord medicalRecord = medicalRecordService.getOneMedicalRecord(person.getFirstName(),person.getLastName());
				
				//Mapping of DTO (May be used with MapStruct)
				personDTO.setFirstName(person.getFirstName());
				personDTO.setLastName(person.getLastName());
				personDTO.setPhoneNumber(person.getPhone());
				personDTO.setAge(calculatePersonAge(medicalRecord.getBirthdate()));
				personDTO.setAddress(person.getAddress());
				
				personDTOList.add(personDTO);
				
			}
		}
		
		//Count adults
		log.debug("Counting number of adults...");
		int adultCount = (int) personDTOList
				.stream()
				.filter(age -> age.getAge() >= 18)
				.count();
		log.debug("{} adults founds", adultCount);
		groupOfPerson.setAdultCount(adultCount); 	
		
		

		//Count children
		log.debug("Counting number of childrens...");
		int childCount = (int) personDTOList
				.stream()
				.filter(age -> age.getAge() <= 18)
				.count();
		log.debug("{} children founds", childCount);
		
		groupOfPerson.setChildCount(childCount); 
		groupOfPerson.setResidents(personDTOList);
		
		log.debug("Group of person founds : ", groupOfPerson);
		return groupOfPerson;
		
	}
	
}
