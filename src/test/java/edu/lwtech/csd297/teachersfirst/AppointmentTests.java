package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import java.sql.Timestamp;

class AppointmentTests {

	Appointment Alex = new Appointment(5, 55,   1998, 3, 11, 4, 30,   2000, 6, 22, 6, 30);
	Appointment Mack = new Appointment(35, 66, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));

	Appointment Eimaj = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
	Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));

	@BeforeEach
	void setUp() {}

	@Test 
	void testConstructor(){
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(-10, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: recID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, -20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: studentID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, -43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: instructorID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, 43, null, DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: startTime is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, 43, DateHelpers.toTimestamp("2000/02/01 00:00:00"), null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: endTime is null"));
		
		
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

		Jamie.setRecID(42);

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Jamie.setRecID(20);
		});
		assertTrue(ex.getMessage().contains("setRecID: Object has already been added to the database (recID != 1)."));

	}

	@Test
	void testGetStudentID(){
		assertEquals(20, Jamie.getStudentID());
	}

	@Test
	void testGetIsMyAppointment(){
		assertTrue(Alex.getIsMyAppointment(5));
		assertTrue(Alex.getIsMyAppointment(55));
		assertFalse(Alex.getIsMyAppointment(9));
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

	@Test
	void testEquals(){
		//Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		assertTrue(Jamie.equals(Eimaj));

		assertFalse(Jamie.equals(new Appointment(2,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"))));
		assertFalse(Jamie.equals(new Appointment(-1,19,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"))));
		assertFalse(Jamie.equals(new Appointment(-1,20,44,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"))));
		assertFalse(Jamie.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/15/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"))));
		assertFalse(Jamie.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/25/01 00:00:00"))));

		assertFalse(Jamie.equals(null));
		assertTrue(Jamie.equals(Jamie));
		assertFalse(Jamie.equals(new Opening(2, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/25/01 00:00:00"))));
	}

	
}
