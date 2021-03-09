package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import jdk.jfr.Timestamp;

class OpeningTests {

	Opening Jordan = new Opening(12,  2000,3,11,4,30,  2000,6,14,6,30);
	Opening Riley = new Opening(14,  DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));
	Opening Alex = new Opening(5, 5, DateHelpers.toTimestamp("2000/06/05 00:00:00"),  DateHelpers.toTimestamp("2000/08/15 00:00:00"));

	@BeforeEach
	void setUp() {}

	@Test
	void testConstructor() {}

	@Test
	void testGetRecID(){
		Exception ex = null;

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

}
