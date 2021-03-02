package edu.lwtech.csd297.teachersfirst;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelpers {

	public static Timestamp StringToTimestamp(String s) {
		String myDate = "2014/10/29 18:10:45";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(myDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			TeachersFirstServlet.logger.debug(e.getStackTrace().toString());
			return null;
		}
		long timeInMillis = date.getTime();
		return new Timestamp(timeInMillis);
	}
}
