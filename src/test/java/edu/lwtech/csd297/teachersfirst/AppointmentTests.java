package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import jdk.jfr.Timestamp;

class AppointmentTests {

	Appointment Eimaj = new Appointment(10, 50,  2000, 5, 5, 3, 30,  2000, 10, 5, 6, 30);
	Appointment Noah = new Appointment(21, 44, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
	Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));

	@BeforeEach
	void setUp() {
		
	}

	@Test 
	void testConstructor(){

	}

	@Test
	void testGetRecID(){
		assertEquals(-1, Jamie.getRecID());
	}

	@Test
	void testSetRecID(){
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Jamie.setRecID(-6);
		});
		assertTrue(ex.getMessage().contains("setRecID: recID cannot be negative."));

		Jamie.setRecID(2);

		assertEquals(2, Jamie.getRecID());
	}

	@Test
	void testGetStudentID(){
		assertEquals(20, Jamie.getStudentID());
	}

	@Test
	void testGetInstructorID(){
		assertEquals(43, Jamie.getInstructorID());
	}

	@Test
	void testGetStartTime(){
		assertEquals("2000-01-01 00:00:00.0", Jamie.getStartTime().toString());
	}

	@Test
	void testGetEndTime(){
		assertEquals("2000-02-01 00:00:00.0", Jamie.getEndTime().toString());
	}

	@Test
	void testGetName(){
		assertEquals("Appointment/20>43@2000-01-01 00:00:00.0-2000-02-01 00:00:00.0", Jamie.getName());
	}

	
}
