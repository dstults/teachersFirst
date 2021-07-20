package org.funteachers.teachersfirst;

import java.util.*;

import javax.validation.constraints.AssertTrue;

import org.funteachers.teachersfirst.DateHelpers;
import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import junit.framework.Assert;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.format.*;

class PlannedAppointmentTests {

	@BeforeEach
	void setUp() {}

	@Test 
	void testConstructorBasicGets() {

		PlannedAppointment Sample1 = new PlannedAppointment(13, 2, 2021, 4, 29, 6, 30, 29, 7, 30);
		PlannedAppointment Sample2 = new PlannedAppointment(166, 35, DateHelpers.toTimestamp("2023/03/12 11:30:00"), DateHelpers.toTimestamp("2023/03/12 12:30:00"));
		Assert.assertEquals(13, Sample1.getStudentID());
		Assert.assertEquals(2, Sample1.getInstructorID());
		Assert.assertEquals(DateHelpers.toTimestamp("2021/04/29 06:30:00"), Sample1.getStartTime());
		Assert.assertEquals(35, Sample2.getInstructorID());
		Assert.assertEquals("UNTESTED", Sample2.getResult());
		Assert.assertEquals(DateHelpers.toTimestamp("2023/03/12 12:30:00"), Sample2.getEndTime());
		// No crash is good
	}

	@Test 
	void testMakeListConstructor() {
		List<DayOfWeek> daysOfWeek = new ArrayList<>();
		daysOfWeek.add(DayOfWeek.SUNDAY);
		daysOfWeek.add(DayOfWeek.TUESDAY);
		daysOfWeek.add(DayOfWeek.THURSDAY);

		List<PlannedAppointment> planList = PlannedAppointment.MakeList(13, 2, daysOfWeek,
			2021, 5, 2, 8, 0,
			2021, 5, 15, 19, 30);
		
		Assert.assertNotNull(planList);
		Assert.assertEquals("2021-05-04 08:00:00.0", planList.get(1).getStartTime().toString());
		Assert.assertEquals("2021-05-11 19:30:00.0", planList.get(4).getEndTime().toString());
	}

	
}
