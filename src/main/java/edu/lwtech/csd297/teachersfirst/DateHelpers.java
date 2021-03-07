package edu.lwtech.csd297.teachersfirst;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateHelpers {

	public static final long millisecondsPerDay = 86_400_000;
	public static final String homeZoneId = "America/Los_Angeles";
	public static final ZoneId homeZone = ZoneId.of(homeZoneId);

	public static Timestamp toTimestamp(Calendar cal) {
		return new Timestamp(cal.getTimeInMillis());
	}

	public static Timestamp toTimestamp(int year, int month, int day, int hour, int minute, int second) {
		return toTimestamp(year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);
	}

	public static Timestamp toTimestamp(String myDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(myDate);
		} catch (ParseException e) {
			// This cannot be called during testing
			//TeachersFirstServlet.logger.debug(e.getStackTrace().toString());
			e.printStackTrace();
			return null;
		}
		long timeInMillis = date.getTime();
		return new Timestamp(timeInMillis);
	}

	public static int calculateAgeFrom(Timestamp birthdate) {
		// Thanks to: https://howtodoinjava.com/java/calculate-age-from-date-of-birth/
		int years = 0;
		int months = 0;
		int days = 0;

		//create calendar object for birth day
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
		if (months < 0)
		{
			years--;
			months = 12 - birthMonth + currMonth;
			if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			months--;
		} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		{
			years--;
			months = 11;
		}

		//Calculate the days
		if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
			days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
		else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		{
			int today = now.get(Calendar.DAY_OF_MONTH);
			now.add(Calendar.MONTH, -1);
			days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
		} 
		else
		{
			days = 0;
			if (months == 12)
			{
			years++;
			months = 0;
			}
		}
		
		return years;
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
		return today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).atTime(23, 59, 59); // LocalTime.MAX looks ugly when stringified

		/* Doesn't do timezones natively:
		Calendar cal = Calendar.getInstance();
		int diff = Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK);
		if (diff <= 0) diff += 7;
		cal.add(Calendar.DAY_OF_MONTH, diff);
		return new Timestamp(cal.getTimeInMillis()); */
	}

	public static String toDateString(Timestamp ts) {
		LocalDate date = Instant.ofEpochMilli(ts.getTime() * 1000).atZone(homeZone).toLocalDate();
		return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}

	public static String getDateTimeString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}

	public static String getSystemTimeZone() {
		return ZoneId.systemDefault().toString();
	}

}
