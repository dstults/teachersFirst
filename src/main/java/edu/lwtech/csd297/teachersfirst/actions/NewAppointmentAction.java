package edu.lwtech.csd297.teachersfirst.actions;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class NewAppointmentAction extends ActionRunner {

	public NewAppointmentAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.SendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}

		final String openingIdString = QueryHelpers.getPost(request, "openingId");
		int openingIdInt;
		try {
			openingIdInt = Integer.parseInt(openingIdString);
		} catch (NumberFormatException e) {
			openingIdInt = 0;
		}
		final Opening referralOpening = DataManager.getOpeningDAO().retrieveByID(openingIdInt);
		if (referralOpening == null) {
			this.SendPostReply("/openings", "", "Opening with ID %5B" + openingIdString + "%5D does not exist!");
			return;
		}

		final String studentIdString = QueryHelpers.getPost(request, "studentId");
		int studentIdInt;
		try {
			studentIdInt = Integer.parseInt(studentIdString);
		} catch (NumberFormatException e) {
			studentIdInt = 0;
		}
		if (DataManager.getMemberDAO().retrieveByID(studentIdInt) == null) {
			this.SendPostReply("/openings", "", "Student with ID %5B" + studentIdString + "%5D does not exist!");
			return;
		}
		final String instructorIdString = QueryHelpers.getPost(request, "instructorId");
		int instructorIdInt;
		try {
			instructorIdInt = Integer.parseInt(instructorIdString);
		} catch (NumberFormatException e) {
			instructorIdInt = 0;
		}

		if (DataManager.getMemberDAO().retrieveByID(instructorIdInt) == null) {
			this.SendPostReply("/openings", "", "Instructor with ID %5B" + instructorIdString + "%5D does not exist!");
			return;
		} else if (studentIdInt == instructorIdInt) {
			this.SendPostReply("/openings", "", "Student ID and Instructor ID both " + studentIdString + " -- appointments can not be made with self.");
			return;
		}

		final String dateString = QueryHelpers.getPost(request, "date");
		final String startTimeString = QueryHelpers.getPost(request, "appointmentStartTime");
		final String endTimeString = QueryHelpers.getPost(request, "appointmentEndTime");

		int month = 1;
		int day = 1;
		int year = 1800;
		try {
			final String[] dateInfo = dateString.split("/");
			if (dateInfo.length != 3) throw new NumberFormatException();
			month = Integer.parseInt(dateInfo[0]);
			day = Integer.parseInt(dateInfo[1]);
			year = Integer.parseInt(dateInfo[2]);
		} catch (NumberFormatException e) {
			this.SendPostReply("/openings", "", "Could not parse date: %5B" + dateString + "%5D !");
			return;
		}

		int startHour = 0;
		int startMinute = 0;
		try {
			final String[] timeInfo = startTimeString.split(":");
			if (timeInfo.length != 2) throw new NumberFormatException();
			startHour = Integer.parseInt(timeInfo[0]);
			startMinute = Integer.parseInt(timeInfo[1]);
		} catch (NumberFormatException e) {
			this.SendPostReply("/openings", "", "Could not parse start time: %5B" + startTimeString + "%5D L:" + startTimeString.split(":").length + " !");
			return;
		}

		if (startMinute != 0 && startMinute != 15 && startMinute != 30 && startMinute != 45) {
			this.SendPostReply("/openings", "", "Start minute %5B" + startMinute + "%5D not allowed, must be multiple of 15!");
			return;
		}

		int endHour = 0;
		int endMinute = 0;
		try {
			final String[] timeInfo = endTimeString.split(":");
			if (timeInfo.length != 2) throw new NumberFormatException();
			endHour = Integer.parseInt(timeInfo[0]);
			endMinute = Integer.parseInt(timeInfo[1]);
		} catch (NumberFormatException e) {
			this.SendPostReply("/openings", "", "Could not parse end time: %5B" + endTimeString + "%5D !");
			return;
		}

		if (endMinute != 0 && endMinute != 15 && endMinute != 30 && endMinute != 45) {
			this.SendPostReply("/openings", "", "End minute %5B" + endMinute + "%5D not allowed, must be multiple of 15!");
			return;
		}

		int endDay = day;
		int startTime = startHour * 60 + startMinute;
		int endTime = endHour * 60 + endMinute;
		if (endTime < startTime) {
			endTime += 1440;
			endDay++;
		}
		if (startTime > 1440 || endTime > 1440) {
			this.SendPostReply("/openings", "", "Invalid start time or end time. Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
			return;
		}
		if (endTime - 720 > startTime) {
			this.SendPostReply("/openings", "", "Appointments must not be longer than 12 hours! Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
			return;
		}

		LocalDateTime startTimeLdt = LocalDateTime.of(year, month, day, startHour, startMinute, 0);
		LocalDateTime endTimeLdt = LocalDateTime.of(year, month, day, endHour, endMinute, 0);

		// Make sure within scope of openings
		if (!DateHelpers.timeIsBetweenTimeAndTime(
				startTimeLdt,
				referralOpening.getStartTime().toLocalDateTime(),
				referralOpening.getEndTime().toLocalDateTime()) &&
			!DateHelpers.timeIsBetweenTimeAndTime(
				endTimeLdt,
				referralOpening.getStartTime().toLocalDateTime(),
				referralOpening.getEndTime().toLocalDateTime())) {

				this.SendPostReply("/openings", "", "Appointment not within scope of opening!");
			return;
		}

		// Make sure no conflicting appointments
		List<Appointment> allAppointments = DataManager.getAppointmentDAO().retrieveAll();
		for(Appointment appointment : allAppointments) {
			if (DateHelpers.timeIsBetweenTimeAndTime(
					startTimeLdt.plusMinutes(1),
					appointment.getStartTime().toLocalDateTime(),
					appointment.getEndTime().toLocalDateTime()) ||
				DateHelpers.timeIsBetweenTimeAndTime(
					endTimeLdt.minusMinutes(1),
					appointment.getStartTime().toLocalDateTime(),
					appointment.getEndTime().toLocalDateTime()) ||
				DateHelpers.timeIsBetweenTimeAndTime( // catches edge case of iAppointment being inside potential new one
					appointment.getStartTime().toLocalDateTime().plusMinutes(1),
					startTimeLdt,
					endTimeLdt)) {

				this.SendPostReply("/openings", "", "Appointment conflicts with appointment: %5B" + appointment.getRecID() + "%5D!");
				return;
			}
		}

		logger.debug("Attempting to create new appointment ...");
		
		Appointment appointment = new Appointment(studentIdInt, instructorIdInt, year, month, day, startHour, startMinute, year, month, endDay, endHour, endMinute);
		DataManager.getAppointmentDAO().insert(appointment);
		logger.info(DataManager.getAppointmentDAO().size() + " records total");
		logger.debug("Created new appointment: [{}]", appointment);
		
		this.SendPostReply("/appointments", "", "Appointment created!");
		return;
	}
	
}
