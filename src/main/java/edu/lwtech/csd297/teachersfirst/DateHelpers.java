package edu.lwtech.csd297.teachersfirst;

import java.sql.Timestamp;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateHelpers {

	public static final long millisecondsPerDay = 86_400_000;
	public static final String homeZoneId = "America/Los_Angeles";
	public static final ZoneId homeZone = ZoneId.of(homeZoneId);

	public static Timestamp toTimestamp(int year, int month, int day, int hour, int minute, int second) {
		return toTimestamp(year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);
	}

	public static Timestamp toTimestamp(LocalDateTime ldt) {
		return Timestamp.valueOf(ldt);
	}

	public static Timestamp toTimestamp(String dateTimeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(dateTimeString);
		} catch (ParseException e) {
			System.out.println("ParseException: Cannot parse date " + dateTimeString + " at position " + e.getErrorOffset());
			return null;
		}
		long timeInMillis = date.getTime();
		return new Timestamp(timeInMillis);
	}

	public static Timestamp fromSqlDatetimeToTimestamp(String sqlDatetime) {
		///String choppedDecisecond = sqlDatetime.split(".")[0];
		String choppedDecisecond = sqlDatetime.substring(0, sqlDatetime.length() - 2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(choppedDecisecond);
		} catch (ParseException e) {
			// This cannot be called during testing
			//TeachersFirstServlet.logger.debug(e.getStackTrace().toString());
			e.printStackTrace();
			return null;
		}
		long timeInMillis = date.getTime();
		return new Timestamp(timeInMillis);
	}


	public static Timestamp fromSqlDateToTimestamp(String sqlDate) {
		///String choppedDecisecond = sqlDatetime.split(".")[0];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sdf.parse(sqlDate);
		} catch (ParseException e) {
			// This cannot be called during testing
			//TeachersFirstServlet.logger.debug(e.getStackTrace().toString());
			e.printStackTrace();
			return null;
		}
		long timeInMillis = date.getTime();
		return new Timestamp(timeInMillis);
	}

	public static String toSqlDatetimeString(Timestamp timestamp) {
		return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String toSqlDatetimeString(LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static int calculateAgeFrom(Timestamp birthdate) {
		// Thanks to: https://howtodoinjava.com/java/calculate-age-from-date-of-birth/
		int years = 0;
		int months = 0;
		int days;

		//create calendar object for birthday
		Calendar birthDay = Calendar.getInstance();
		birthDay.setTimeInMillis(birthdate.getTime());

		//create calendar object for current day
		long currentTime = System.currentTimeMillis();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(currentTime);

		//Get difference between years
		years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		int currMonth = now.get(Calendar.MONTH) + 1;
		int birthMonth = birthDay.get(Calendar.MONTH) + 1;

		//Get difference between months
		months = currMonth - birthMonth;

		//if month difference is in negative then reduce years by one 
		//and calculate the number of months.
		if (months < 0) {
			years--;
			months = 12 - birthMonth + currMonth;
			if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) months--;
		} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
			years--;
			months = 11;
		}

		//Calculate the days
		if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE)) {
			days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
		} else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
			int today = now.get(Calendar.DAY_OF_MONTH);
			now.add(Calendar.MONTH, -1);
			days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
		} else {
			days = 0;
			if (months == 12) {
				years++;
				months = 0;
			}
		}
		
		return years;
	}

	public static boolean isInThePast(LocalDateTime comparedDateTime) {
		return comparedDateTime.compareTo(LocalDateTime.now()) < 0;
	}

	public static boolean dateIsBetweenDateAndDate(LocalDate time, LocalDate start, LocalDate finish) {
		return time.compareTo(start) >= 0 && time.compareTo(finish) <= 0;
	}

	public static boolean timeIsBetweenTimeAndTime(LocalDateTime time, LocalDateTime start, LocalDateTime finish) {
		return time.compareTo(start) >= 0 && time.compareTo(finish) <= 0;
	}

	public static LocalDateTime previousSunday() {
		final LocalDate today = LocalDate.now(homeZone);
		return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atStartOfDay();
		
		/* Doesn't do timezones:
        Calendar cal = Calendar.getInstance();
        int diff = Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK);
		if (diff > 7) diff -= 7;
        cal.add(Calendar.DAY_OF_MONTH, diff);
        return new Timestamp(cal.getTimeInMillis()); */
	}

	public static LocalDateTime nextSaturday() {
		final LocalDate today = LocalDate.now(homeZone);
		return today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).atTime(23, 59, 59); // LocalTime.MAX looks ugly when stringified

		/* Doesn't do timezones natively:
		Calendar cal = Calendar.getInstance();
		int diff = Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK);
		if (diff <= 0) diff += 7;
		cal.add(Calendar.DAY_OF_MONTH, diff);
		return new Timestamp(cal.getTimeInMillis()); */
	}

	public static String getNowDateTimeString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}

	public static String getSystemTimeZone() {
		return ZoneId.systemDefault().toString();
	}

	public static String convertMinutesToHM(int minutes) {
		if (minutes >= 120) {
			return (minutes / 60) + " hours " + (minutes % 60) + " minutes";
		} else if (minutes >= 60) {
			return (minutes / 60) + " hour " + (minutes % 60) + " minutes";
		} else {
			return minutes + " minutes";
		}
	}

	public static String convertDateStartTimeAndDurationToEndTime(String date, String startTime, String duration) {
		final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
		final LocalDateTime startDateTime = LocalDateTime.parse(date + " " + startTime, timeFormatter);
		String[] durationVals = duration.split(" ");
		int minutes;
		try {
			if (durationVals.length > 2) {
				minutes = 60 * Integer.parseInt(durationVals[0]) + Integer.parseInt(durationVals[2]);
			} else {
				if (durationVals[1].contains("hour")) {
					minutes = 60 * Integer.parseInt(durationVals[0]);
				} else {
					minutes = Integer.parseInt(durationVals[0]);
				}
			}
		} catch (DateTimeParseException e) {
			return "";
		}
		return startDateTime.plusMinutes(minutes).format(DateTimeFormatter.ofPattern("HH:mm"));
	}

}
