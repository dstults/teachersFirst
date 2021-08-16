package org.funteachers.teachersfirst;

import org.junit.jupiter.api.*;

class DataManagerTests {

	@BeforeEach
	void setUp() { }

	@Test
	void testInitializeAndTerminate() {
		// This test should not exist unless we make a test db.
		
		/*
		try {
			DataManager.initializeDAOs();
		} catch (ServletException e) {
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
