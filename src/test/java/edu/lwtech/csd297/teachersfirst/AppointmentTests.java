package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import edu.lwtech.csd297.teachersfirst.obj.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

class AppointmentTests {

	Appointment Appointment1 = new Appointment(5, 55, 1998, 3, 11, 4, 30, 22, 6, 30, true);
	Appointment Appointment2 = new Appointment(35, 66, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true);
	Appointment Appointment3 = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);
	Appointment Appointment4 = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);

	@BeforeEach
	void setUp() {}

	@Test 
	void testConstructor() {
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(-10, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: recID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, -20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: studentID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, -43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: instructorID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, 43, null, DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: startTime is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(3, 20, 43, DateHelpers.toTimestamp("2000/02/01 00:00:00"), null, true, -1);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: endTime is null"));
		
		
	}

	@Test
	void testGetRecID() {
		assertEquals(-1, Appointment4.getRecID());
	}

	@Test
	void testSetRecID() {
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Appointment4.setRecID(-6);
		});
		assertTrue(ex.getMessage().contains("setRecID: recID cannot be negative."));

		Appointment4.setRecID(42);

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Appointment4.setRecID(20);
		});
		assertTrue(ex.getMessage().contains("setRecID: Object has already been added to the database (recID != 1)."));

	}

	@Test
	void testGetStudentID() {
		assertEquals(20, Appointment4.getStudentID());
	}

	@Test
	void testGetIsMyAppointment() {
		assertTrue(Appointment1.getIsMyAppointment(5));
		assertTrue(Appointment1.getIsMyAppointment(55));
		assertFalse(Appointment1.getIsMyAppointment(9));
	}

	@Test
	void testGetInstructorID() {
		assertEquals(43, Appointment4.getInstructorID());
	}

	@Test
	void testGetStartTime() {
		assertEquals("2000-01-01 00:00:00.0", Appointment4.getStartTime().toString());
	}

	@Test
	void testGetEndTime() {
		assertEquals("2000-02-01 00:00:00.0", Appointment4.getEndTime().toString());
	}

	@Test
	void testGetName() {
		assertEquals("Appointment(-1): Student(20) > Instructor(43) @ 01/01/2000 00:00 - 00:00", Appointment4.getName());
	}

	@Test
	void testEquals() {
		//Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		assertTrue(Appointment4.equals(Appointment3));

		assertFalse(Appointment4.equals(new Appointment(2,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,19,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,44,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/15/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/25/01 00:00:00"), true, -1)));

		assertFalse(Appointment4.equals(null));
		assertTrue(Appointment4.equals(Appointment4));
		assertFalse(Appointment4.equals(new Opening(2, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/25/01 00:00:00"))));
	}

	
}
