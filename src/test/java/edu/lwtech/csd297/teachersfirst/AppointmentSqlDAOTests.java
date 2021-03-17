package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import jdk.jfr.Timestamp;

class AppointmentSqlDAOTests {

	Appointment Eimaj = new Appointment(10, 50,  2000, 5, 5, 3, 30,  2000, 10, 5, 6, 30);
	Appointment Noah = new Appointment(21, 44, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));
	Appointment Jamie = new Appointment(-1, 20, 43, DateHelpers.toTimestamp("2000/01/01 00:00:00"), DateHelpers.toTimestamp("2000/02/01 00:00:00"));

	@BeforeEach
	void setUp() {
	}

	
}
