package com.api.safetynet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.api.safetynet.service.PersonService;

@SpringBootTest
class SafetyNetApplicationTests {
	
	@Autowired
	private PersonService ps;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void testGetAllPerson() {
		//GIVEN
		//WHEN
		//THEN
	}

}
