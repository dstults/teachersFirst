package edu.lwtech.csd297.teachersfirst;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

class DateHelpersTests {

	Member apple;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testTimes() {
		Timestamp sometime1 = DateHelpers.toTimestamp("2011/09/03 01:23:45");
		Timestamp sometime2 = DateHelpers.fromSqlDatetimeToTimestamp("2011-09-03 01:23:45.0");
		assertEquals(sometime1, sometime2);

		LocalDate sometime3 = DateHelpers.fromSqlDateToTimestamp("2011-09-03").toLocalDateTime().toLocalDate();
		assertEquals(sometime1.toLocalDateTime().toLocalDate(), sometime3);

		Timestamp sometime4 = DateHelpers.toTimestamp(sometime1.toLocalDateTime());
		assertEquals(sometime1, sometime4);

		assertTrue(DateHelpers.isInThePast(sometime1.toLocalDateTime()));
		Timestamp sometime5 = DateHelpers.toTimestamp("2041/09/03 01:23:45");
		assertFalse(DateHelpers.isInThePast(sometime5.toLocalDateTime()));
		
		String str1 = DateHelpers.toSqlDatetimeString(sometime1);
		String str2 = DateHelpers.toSqlDatetimeString(sometime1.toLocalDateTime());
		assertEquals("2011-09-03 01:23:45", str1);
		assertEquals("2011-09-03 01:23:45", str2);

		//assertNull(DateHelpers.toTimestamp("asdf"));
		//assertNull(DateHelpers.fromSqlDateToTimestamp("asdf"));

		LocalDateTime time1 = DateHelpers.toTimestamp("2021/09/03 01:23:45").toLocalDateTime();
		LocalDateTime time2 = DateHelpers.toTimestamp("2021/08/03 01:23:45").toLocalDateTime();
		LocalDateTime time3 = DateHelpers.toTimestamp("2021/10/03 01:23:45").toLocalDateTime();
		assertTrue(DateHelpers.timeIsBetweenTimeAndTime(time1, time2, time3));
		assertTrue(DateHelpers.timeIsBetweenTimeAndTime(time1, time1, time3));
		assertFalse(DateHelpers.timeIsBetweenTimeAndTime(time2, time1, time3));
		LocalDate date1 = time1.toLocalDate();
		LocalDate date2 = time2.toLocalDate();
		LocalDate date3 = time3.toLocalDate();
		assertTrue(DateHelpers.dateIsBetweenDateAndDate(date1, date2, date3));
		assertTrue(DateHelpers.dateIsBetweenDateAndDate(date1, date1, date3));
		assertFalse(DateHelpers.dateIsBetweenDateAndDate(date2, date1, date3));

		String hoursMinutes;
		hoursMinutes = DateHelpers.convertMinutesToHM(15);
		assertEquals("15 minutes", hoursMinutes);
		hoursMinutes = DateHelpers.convertMinutesToHM(90);
		assertEquals("1 hour 30 minutes", hoursMinutes);
		hoursMinutes = DateHelpers.convertMinutesToHM(165);
		assertEquals("2 hours 45 minutes", hoursMinutes);


		String endTime;
		endTime = DateHelpers.convertDateStartTimeAndDurationToEndTime("03/15/2021", "15:30", "30 minutes");
		assertEquals("16:00", endTime);
		endTime = DateHelpers.convertDateStartTimeAndDurationToEndTime("03/15/2021", "15:30", "1 hour");
		assertEquals("16:30", endTime);
		endTime = DateHelpers.convertDateStartTimeAndDurationToEndTime("03/15/2021", "15:30", "2 hours 30 minutes");
		assertEquals("18:00", endTime);
		endTime = DateHelpers.convertDateStartTimeAndDurationToEndTime("03/15/2021", "23:30", "2 hours 30 minutes");
		assertEquals("02:00", endTime);

		assertThrows(DateTimeParseException.class, () -> {
			DateHelpers.convertDateStartTimeAndDurationToEndTime("poop", "fart", "nutsack");
		});
		endTime = DateHelpers.convertDateStartTimeAndDurationToEndTime("03/15/2021", "23:30", "2 hours 30 minutes");
		assertEquals("02:00", endTime);

	}

	@Test
	void testUntestableRelativeValues() {
		DateHelpers.previousSunday();
		DateHelpers.nextSaturday();
		DateHelpers.getNowDateTimeString();
		// This fails GitHub tests as their test doesn't seem to natively support any time zones:
		//assertEquals("America/Los_Angeles", DateHelpers.getSystemTimeZone());
		DateHelpers.calculateAgeFrom(DateHelpers.toTimestamp("2011/03/03 01:23:45"));
		DateHelpers.calculateAgeFrom(DateHelpers.toTimestamp("2016/06/03 01:23:45"));
		DateHelpers.calculateAgeFrom(DateHelpers.toTimestamp("2021/09/03 01:23:45"));
		DateHelpers.calculateAgeFrom(DateHelpers.toTimestamp("2026/12/03 01:23:45"));
	}

}
