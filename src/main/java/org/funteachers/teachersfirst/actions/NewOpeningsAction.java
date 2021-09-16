package org.funteachers.teachersfirst.actions;

import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class NewOpeningsAction extends ActionRunner {

	public NewOpeningsAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendJsonMessage("Please sign in or register to use this feature!", false);
			return;
		}

		String instructorIdString = QueryHelpers.getPost(request, "instructorId");
		int instructorIdInt;
		try {
			instructorIdInt = Integer.parseInt(instructorIdString);
		} catch (NumberFormatException e) {
			instructorIdInt = 0;
		}
		final DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
		final boolean instructorExists = memberDAO.retrieveByID(instructorIdInt) != null;
		final String startDateString = QueryHelpers.getPost(request, "startDate");
		final String endDateString = QueryHelpers.getPost(request, "endDate");
		final String daysOfWeekString = QueryHelpers.getPost(request, "daysOfWeek").toLowerCase(); // SuMoTuWdThFrSa
		final String startTimeString = QueryHelpers.getPost(request, "startTime");
		final String endTimeString = QueryHelpers.getPost(request, "endTime");

		//final String retryString = "instructorId=" + instructorIdString + "&startDate=" + startDateString + "&endDate=" + endDateString + "&daysOfWeek=" + daysOfWeekString + "&startTime=" + startTimeString + "&endTime=" + endTimeString;

		final int startHour;
		final int startMinute;
		try {
			final String[] timeInfo = startTimeString.split(":");
			if (timeInfo.length != 2) throw new NumberFormatException();
			startHour = Integer.parseInt(timeInfo[0]);
			startMinute = Integer.parseInt(timeInfo[1]);
		} catch (NumberFormatException e) {
			//this.sendPostReply("/make_openings", retryString, "Could not parse start time: %5B" + startTimeString + "%5D !");
			this.sendJsonMessage("Could not parse start time: [" + startTimeString + "] !", false);
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
			//this.sendPostReply("/make_openings", retryString, "Could not parse end time: %5B" + endTimeString + "%5D !");
			this.sendJsonMessage("Could not parse end time: [" + endTimeString + "] !", false);
			return;
		}

		if (!instructorExists) {
			//this.sendPostReply("/make_openings", retryString, "Please provide a valid instructor ID.");
			this.sendJsonMessage("Please provide a valid instructor ID.", false);
			return;
		}
		if (startDateString.isEmpty()) {
			//this.sendPostReply("/make_openings", retryString, "Please provide a valid start date.");
			this.sendJsonMessage("Please provide a valid start date.", false);
			return;
		}
		if (endDateString.isEmpty()) {
			//this.sendPostReply("/make_openings", retryString, "Please provide a valid end date.");
			this.sendJsonMessage("Please provide a valid end date.", false);
			return;
		}
		if (daysOfWeekString.isEmpty() ) {
			//this.sendPostReply("/make_openings", retryString, "Please provide days of the week.");
			this.sendJsonMessage("Please provide days of the week.", false);
			return;
		}
		if (startTimeString.isEmpty()) {
			//this.sendPostReply("/make_openings", retryString, "Please provide a valid start time.");
			this.sendJsonMessage("Please provide a valid start time.", false);
			return;
		}
		if (endTimeString.isEmpty()) {
			//this.sendPostReply("/make_openings", retryString, "Please provide a valid end time.");
			this.sendJsonMessage("Please provide a valid end time.", false);
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
			//this.sendPostReply("/make_openings", retryString, "Couldn't parse your days of the week.");
			this.sendJsonMessage("Couldn't parse your days of the week.", false);
			return;
		}

		logger.debug("Attempting to create batch openings ...");
		final DAO<Opening> openingDAO = this.connectionPackage.getOpeningDAO(this.getClass().getSimpleName());
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 		
		final LocalDate startDate = LocalDate.parse(startDateString, formatter);
		final LocalDate endDate = LocalDate.parse(endDateString, formatter);

		if (endDate.compareTo(startDate) < 0) {
			//this.sendPostReply("/make_openings", retryString, "End date can't be before the start date.");
			this.sendJsonMessage("End date can't be before the start date.", false);
			return;
		}

		// Temp vars, used to test addDay and in while loop:
		LocalDate today = startDate.plusDays(0);
		LocalDateTime startDateTime = today.atTime(startHour, startMinute);
		LocalDateTime endDateTime = today.atTime(endHour, endMinute);
		final boolean addDay = endDateTime.compareTo(startDateTime) < 0;
		if (addDay) endDateTime = endDateTime.plusDays(1);

		// Finally, test to make sure span is not greater than 14 hours or equal to zero, (note: it
		// can't be "less than" zero because a day is added, just to be safe, still using lte):
		long hours = ChronoUnit.HOURS.between(startDateTime, endDateTime);
		if (hours >= 14) {
			//this.sendPostReply("/make_openings", retryString, "End time can't be more than 14 hours after the start time.");
			this.sendJsonMessage("End time can't be more than 14 hours after the start time.", false);
			return;
		} else if (hours <= 0) {
			//this.sendPostReply("/make_openings", retryString, "Time span must be greater than zero hours.");
			this.sendJsonMessage("Time span must be greater than zero hours.", false);
			return;
		}

		int openingsCreated = 0;
		while (today.compareTo(endDate) <= 0) {
			dayOfWeek = today.getDayOfWeek();
			if (openedDays.contains(dayOfWeek)) {
				startDateTime = today.atTime(startHour, startMinute);
				endDateTime = today.atTime(endHour, endMinute);
				if (addDay) endDateTime = endDateTime.plusDays(1);
				openingDAO.insert(new Opening(instructorIdInt, DateHelpers.toTimestamp(startDateTime), DateHelpers.toTimestamp(endDateTime)));
				openingsCreated++;
			}
			today = today.plusDays(1);
		}

		//this.sendPostReply("/openings", "", "Openings made, good job!");
		if (openingsCreated > 0) {
			final String s = openingsCreated > 1 ? "s" : "";
			this.sendJsonMessage("Successfully created " + openingsCreated + " opening" + s + ".", true, "/openings");
		} else {
			this.sendJsonMessage("No openings were created! Make sure the checked days of week are within the date span.", false);
		}
		
		return;
	}
	
}
