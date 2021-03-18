package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.servlet.ServletException;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

class DataManagerTests {

	@BeforeEach
	void setUp() {
	}

	@Test
	void testInitializeAndTerminate(){
		try {
			DataManager.initializeDAOs();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(DataManager.getAppointmentDAO() != null);
		assertTrue(DataManager.getMemberDAO() != null);
		assertTrue(DataManager.getOpeningDAO() != null);
		assertTrue(DataManager.getServiceDAO() != null);

		DataManager.terminateDAOs();
		DataManager.resetDAOs();

	}

}
