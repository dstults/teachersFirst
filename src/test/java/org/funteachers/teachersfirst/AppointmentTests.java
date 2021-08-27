package org.funteachers.teachersfirst;

import org.funteachers.teachersfirst.managers.DateHelpers;
import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTests {

	Appointment Appointment1 = new Appointment(5, 55, 1998, 3, 11, 4, 30, 22, 6, 30, true);
	Appointment Appointment2 = new Appointment(35, 66, DateHelpers.toTimestamp("2020/02/10 09:00:00"), DateHelpers.toTimestamp("2020/02/10 12:00:00"), true);
	Appointment Appointment3 = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2021/04/14 12:00:00"), DateHelpers.toTimestamp("2021/04/14 13:30:00"), true, -1);
	Appointment Appointment4 = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2019/07/21 16:00:00"), DateHelpers.toTimestamp("2019/07/21 17:00:00"), true, -1);
	Appointment Appointment4b = new Appointment(20, 43, 2019, 7, 21, 16, 0, 21, 17, 0, true);

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
		assertEquals("2019-07-21 16:00:00.0", Appointment4.getStartTime().toString());
	}

	@Test
	void testGetEndTime() {
		assertEquals("2019-07-21 17:00:00.0", Appointment4.getEndTime().toString());
	}

	@Test
	void testGetName() {
		assertEquals("Appointment(-1): Student(35) > Instructor(66) @ 02/10/2020 09:00 - 12:00", Appointment2.getName());
		assertEquals("Appointment(-1): Student(20) > Instructor(43) @ 07/21/2019 16:00 - 17:00", Appointment4.getName());
		assertEquals("Appointment(-1): Student(20) > Instructor(43) @ 07/21/2019 16:00 - 17:00", Appointment4b.getName());
	}

	@Test
	void testEquals() {
		//Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
		assertTrue(Appointment4.equals(Appointment4b));

		assertFalse(Appointment4.equals(new Appointment(2,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,19,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,44,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/15/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"), true, -1)));
		assertFalse(Appointment4.equals(new Appointment(-1,20,43,DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/25/01 00:00:00"), true, -1)));

		assertFalse(Appointment4.equals(null));
		assertTrue(Appointment4.equals(Appointment4));
		assertFalse(Appointment4.equals(Appointment3));
	}

	
}
