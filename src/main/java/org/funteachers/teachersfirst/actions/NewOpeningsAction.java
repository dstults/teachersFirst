package org.funteachers.teachersfirst.actions;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.*;
import java.util.*;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.obj.*;

public class NewOpeningsAction extends ActionRunner {

	public NewOpeningsAction(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	@Override
	public void runAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}

		String instructorIdString = QueryHelpers.getPost(request, "instructorId");
		int instructorIdInt;
		try {
			instructorIdInt = Integer.parseInt(instructorIdString);
		} catch (NumberFormatException e) {
			instructorIdInt = 0;
		}
		final boolean instructorExists = DataManager.getMemberDAO().retrieveByID(instructorIdInt) != null;
		String startDateString = QueryHelpers.getPost(request, "startDate");
		String endDateString = QueryHelpers.getPost(request, "endDate");
		String daysOfWeekString = QueryHelpers.getPost(request, "daysOfWeek").toLowerCase(); // SuMoTuWdThFrSa
		String startTimeString = QueryHelpers.getPost(request, "startTime");
		String endTimeString = QueryHelpers.getPost(request, "endTime");

		final String retryString = "instructorId=" + instructorIdString + "&startDate=" + startDateString + "&endDate=" + endDateString + "&daysOfWeek=" + daysOfWeekString + "&startTime=" + startTimeString + "&endTime=" + endTimeString;

		final int startHour;
		final int startMinute;
		try {
			final String[] timeInfo = startTimeString.split(":");
			if (timeInfo.length != 2) throw new NumberFormatException();
			startHour = Integer.parseInt(timeInfo[0]);
			startMinute = Integer.parseInt(timeInfo[1]);
		} catch (NumberFormatException e) {
			this.sendPostReply("/make_openings", retryString, "Could not parse start time: %5B" + startTimeString + "%5D !");
			return;
		}
		final int endHour;
		final int endMinute;
		try {
			final String[] timeInfo = endTimeString.split(":");
			if (timeInfo.length != 2) throw new NumberFormatException();
			endHour = Integer.parseInt(timeInfo[0]);
			endMinute = Integer.parseInt(timeInfo[1]);
		} catch (NumberFormatException e) {
			this.sendPostReply("/make_openings", retryString, "Could not parse end time: %5B" + endTimeString + "%5D !");
			return;
		}

		if (!instructorExists) {
			this.sendPostReply("/make_openings", retryString, "Please provide a valid instructor ID.");
			return;
		}
		if (startDateString.isEmpty()) {
			this.sendPostReply("/make_openings", retryString, "Please provide a valid start date.");
			return;
		}
		if (endDateString.isEmpty()) {
			this.sendPostReply("/make_openings", retryString, "Please provide a valid end date.");
			return;
		}
		if (daysOfWeekString.isEmpty() ) {
			this.sendPostReply("/make_openings", retryString, "Please provide days of the week.");
			return;
		}
		if (startTimeString.isEmpty()) {
			this.sendPostReply("/make_openings", retryString, "Please provide a valid start time.");
			return;
		}
		if (endTimeString.isEmpty()) {
			this.sendPostReply("/make_openings", retryString, "Please provide a valid end time.");
			return;
		}
		
		// Get days of the week that will be opened up:
		List<DayOfWeek> openedDays = new ArrayList<>();
		DayOfWeek dayOfWeek;		
		if(daysOfWeekString.contains("su")) openedDays.add(DayOfWeek.SUNDAY);
		if(daysOfWeekString.contains("mo")) openedDays.add(DayOfWeek.MONDAY);
		if(daysOfWeekString.contains("tu")) openedDays.add(DayOfWeek.TUESDAY);
		if(daysOfWeekString.contains("we")) openedDays.add(DayOfWeek.WEDNESDAY);
		if(daysOfWeekString.contains("th")) openedDays.add(DayOfWeek.THURSDAY);
		if(daysOfWeekString.contains("fr")) openedDays.add(DayOfWeek.FRIDAY);
		if(daysOfWeekString.contains("sa")) openedDays.add(DayOfWeek.SATURDAY);
		if (openedDays.size() == 0) {
			this.sendPostReply("/make_openings", retryString, "Couldn't parse your days of the week.");
			return;
		}

		logger.debug("Attempting to create batch openings ...");
		DAO<Opening> openingDAO = DataManager.getOpeningDAO();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 		
		LocalDate startDate = LocalDate.parse(startDateString, formatter);
		LocalDate today = startDate.plusDays(0);
		LocalDate endDate = LocalDate.parse(endDateString, formatter);
		Timestamp startDateTime;
		Timestamp endDateTime;

		if (endDate.compareTo(startDate) < 0) {
			this.sendPostReply("/make_openings", retryString, "End date can't be before the start date.");
			return;
		}

		//logger.debug("Start: " + startDate.toString());
		//logger.debug("End:   " + endDate.toString());
		while (today.compareTo(endDate) <= 0) {
			//logger.debug("Today: " + today.toString());
			dayOfWeek = today.getDayOfWeek();
			if (openedDays.contains(dayOfWeek)) {
				startDateTime = DateHelpers.toTimestamp(today.atTime(startHour, startMinute));
				endDateTime = DateHelpers.toTimestamp(today.atTime(endHour, endMinute));
				openingDAO.insert(new Opening(instructorIdInt, startDateTime, endDateTime));
			}
			today = today.plusDays(1);
		}

		this.sendPostReply("/openings", "", "Openings made, good job!");
		return;
	}
	
}
