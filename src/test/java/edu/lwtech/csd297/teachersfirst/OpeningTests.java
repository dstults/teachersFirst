package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import java.sql.Timestamp;

class OpeningTests {

	Opening Jordan = new Opening(-1,  2000,3,11,4,30,  2000,6,14,6,30);
	Opening Riley = new Opening(14,  DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));
	Opening Alex = new Opening(5, 5, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));

	@BeforeEach
	void setUp() {}

	@Test
	void testConstructor() {
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, ()->{
			new Opening(-2, 100, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: recID < -1"));
	
		ex = assertThrows(IllegalArgumentException.class, ()->{
			new Opening(3, -100, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: instructorID < -1"));
	
		ex = assertThrows(IllegalArgumentException.class, ()->{
			new Opening(3, 100, null,  DateHelpers.toTimestamp("2000/08/15 00:00:00"));
		});
		assertTrue(ex.getMessage().contains("Invalid argument: startTime is null"));
	
		ex = assertThrows(IllegalArgumentException.class, ()->{
			new Opening(3, 100, DateHelpers.toTimestamp("2000/08/15 00:00:00"), null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: endTime is null"));
	}

	@Test
	void testGetRecID(){

		assertEquals(5, Alex.getRecID());
		assertEquals(-1, Riley.getRecID());
	}

	@Test
	void testSetRecID(){
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Riley.setRecID(-6);
		});
		assertTrue(ex.getMessage().contains("setRecID: recID cannot be negative."));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			Alex.setRecID(66);
		});
		assertTrue(ex.getMessage().contains("setRecID: Object has already been added to the database (recID != 1)."));

		Jordan.setRecID(2);

		assertEquals(2, Jordan.getRecID());
	}

	@Test
	void testGetStartTime(){
		assertEquals("2000-06-05 00:00:00.0", Alex.getStartTime().toString());
	}

	@Test
	void testGetEndTime(){
		assertEquals("2000-06-14 06:30:00.0", Jordan.getEndTime().toString());
	}

	@Test
	void testInstructorID(){
		assertEquals(5,Alex.getInstructorID());
	}

	@Test
	void testGetName(){
		assertEquals("Opening/14@2000-06-05 00:00:00.0-2000-08-15 00:00:00.0",Riley.getName());
	}

	@Test
	void testEquals(){
		assertFalse(Alex.equals(null));
		assertTrue(Alex.equals(Alex));
		assertFalse(Alex.equals(10));

		assertFalse(Alex.equals(new Opening(25, 5, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"))));
		assertFalse(Alex.equals(new Opening(5, 55, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"))));
		assertFalse(Alex.equals(new Opening(5, 5, DateHelpers.toTimestamp("1980/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"))));
		assertFalse(Alex.equals(new Opening(5, 5, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2040/08/15 00:00:00"))));
		
		assertTrue(Alex.equals(new Opening(5, 5, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"))));
	}

}
