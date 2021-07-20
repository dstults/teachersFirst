package org.funteachers.teachersfirst;

import java.util.*;

import javax.servlet.ServletException;
import javax.validation.constraints.AssertTrue;

import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DataManagerTests {

	@BeforeEach
	void setUp() { }

	@Test
	void testInitializeAndTerminate() {
		// This entire test should not exist, period.
		
		/*
		try {
			DataManager.initializeDAOs();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		/* Disabled while moving servers:
		assertTrue(DataManager.getAppointmentDAO() != null);
		assertTrue(DataManager.getMemberDAO() != null);
		assertTrue(DataManager.getOpeningDAO() != null);
		assertTrue(DataManager.getServiceDAO() != null);

		DataManager.terminateDAOs();
		DataManager.resetDAOs();
		*/
	}

}
