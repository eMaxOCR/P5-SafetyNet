package com.api.safetynet.model;

import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
	
	//Attributes
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private int zip;
	private String phone;
	private String email;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}

	@Override
	public String toString() {
		return "Person [zip=" + zip + "]";
	} 
	
}
