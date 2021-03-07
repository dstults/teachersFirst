package edu.lwtech.csd297.teachersfirst;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelpers {

	public static Timestamp ToTimestamp(int year, int month, int day, int hour, int minute, int second) {
		return ToTimestamp(year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);
	}

	public static Timestamp ToTimestamp(String myDate) {
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

	public static int CalculateAgeFrom(Timestamp birthdate) {
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

}
