package edu.lwtech.csd297.teachersfirst.actions;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class NewAppointmentBatchAction extends ActionRunner {

	public NewAppointmentBatchAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// INPUT: {action: make_appointment_batch}, {instructorId: 1}, {studentId: 2}, {daysOfWeek: SuMoWeThSa}, {startDate: 2021-05-02}, {startTime: 09:00}, {endDate: 2021-05-08}, {endTime: 10:00}
		// OUTPUT: Invalid value for MonthOfYear (Line 164)
		// Fixed.
		// INPUT: {action: make_appointment_batch}, {instructorId: 1}, {studentId: 2}, {daysOfWeek: MoTuWeFrSa}, {startDate: 2021-05-02}, {startTime: 09:00}, {endDate: 2021-05-08}, {endTime: 10:00}
		// OUTPUT: Froze somewhere at or after List<Appointment> allAppointments = DataManager.getAppointmentDAO().retrieveAll();

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.SendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}
		if (DataManager.instructorAdminMakeAppointmentsRequiresOpening || (!isAdmin && !isInstructor)) {
			this.SendPostReply("/services", "", "Batch appointment function disabled.");
		}

		final String studentIdString = QueryHelpers.getPost(request, "studentId");
		int studentIdInt;
		try {
			studentIdInt = Integer.parseInt(studentIdString);
		} catch (NumberFormatException e) {
			studentIdInt = 0;
		}
		if (DataManager.getMemberDAO().retrieveByID(studentIdInt) == null) {
			this.SendPostReply("/make_appointment_batch", "", "Student with ID %5B" + studentIdString + "%5D does not exist!");
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
			this.SendPostReply("/make_appointment_batch", "", "Instructor with ID %5B" + instructorIdString + "%5D does not exist!");
			return;
		} else if (studentIdInt == instructorIdInt) {
			this.SendPostReply("/make_appointment_batch", "", "Student ID and Instructor ID both " + studentIdString + " -- appointments can not be made with self.");
			return;
		}

		final String startDateString = QueryHelpers.getPost(request, "startDate");
		final String endDateString = QueryHelpers.getPost(request, "endDate");
		final String startTimeString = QueryHelpers.getPost(request, "startTime");
		final String endTimeString = QueryHelpers.getPost(request, "endTime");

		int startMonth = 1;
		int startDay = 1;
		int startYear = 1800;
		try {
			final String[] dateInfo = startDateString.split("-");
			if (dateInfo.length != 3) throw new NumberFormatException();
			startYear = Integer.parseInt(dateInfo[0]);
			startMonth = Integer.parseInt(dateInfo[1]);
			startDay = Integer.parseInt(dateInfo[2]);
		} catch (NumberFormatException e) {
			this.SendPostReply("/make_appointment_batch", "", "Could not parse start date: %5B" + startDateString + "%5D !");
			return;
		}

		int endMonth = 1;
		int endDay = 1;
		int endYear = 1800;
		try {
			final String[] dateInfo = endDateString.split("-");
			if (dateInfo.length != 3) throw new NumberFormatException();
			endYear = Integer.parseInt(dateInfo[0]);
			endMonth = Integer.parseInt(dateInfo[1]);
			endDay = Integer.parseInt(dateInfo[2]);
		} catch (NumberFormatException e) {
			this.SendPostReply("/make_appointment_batch", "", "Could not parse end date: %5B" + endDateString + "%5D !");
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
			this.SendPostReply("/make_appointment_batch", "", "Could not parse start time: %5B" + startTimeString + "%5D L:" + startTimeString.split(":").length + " !");
			return;
		}

		if (startMinute != 0 && startMinute != 15 && startMinute != 30 && startMinute != 45) {
			this.SendPostReply("/make_appointment_batch", "", "Start minute %5B" + startMinute + "%5D not allowed, must be multiple of 15!");
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
			this.SendPostReply("/make_appointment_batch", "", "Could not parse end time: %5B" + endTimeString + "%5D !");
			return;
		}

		if (endMinute != 0 && endMinute != 15 && endMinute != 30 && endMinute != 45) {
			this.SendPostReply("/make_appointment_batch", "", "End minute %5B" + endMinute + "%5D not allowed, must be multiple of 15!");
			return;
		}

		int startTime = startHour * 60 + startMinute;
		int endTime = endHour * 60 + endMinute;
		if (endTime < startTime) {
			endTime += 1440;
			endDay++;
		}
		if (startTime > 1440 || endTime > 1440) {
			this.SendPostReply("/make_appointment_batch", "", "Invalid start time or end time. Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
			return;
		}
		if (endTime - 720 > startTime) {
			this.SendPostReply("/make_appointment_batch", "", "Appointments must not be longer than 12 hours! Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
			return;
		}

		String daysOfWeekString = QueryHelpers.getPost(request, "daysOfWeek").toLowerCase(); // SuMoTuWdThFrSa
		List<DayOfWeek> scheduledDays = new ArrayList<>();
		if(daysOfWeekString.contains("su")) scheduledDays.add(DayOfWeek.SUNDAY);
		if(daysOfWeekString.contains("mo")) scheduledDays.add(DayOfWeek.MONDAY);
		if(daysOfWeekString.contains("tu")) scheduledDays.add(DayOfWeek.TUESDAY);
		if(daysOfWeekString.contains("we")) scheduledDays.add(DayOfWeek.WEDNESDAY);
		if(daysOfWeekString.contains("th")) scheduledDays.add(DayOfWeek.THURSDAY);
		if(daysOfWeekString.contains("fr")) scheduledDays.add(DayOfWeek.FRIDAY);
		if(daysOfWeekString.contains("sa")) scheduledDays.add(DayOfWeek.SATURDAY);
		if (scheduledDays.size() == 0) {
			this.SendPostReply("/make_openings", "", "Couldn't parse your days of the week.");
			return;
		}

		// Make sure no conflicting appointments
		List<Appointment> allAppointments = DataManager.getAppointmentDAO().retrieveAll();
		logger.debug("Appointments GOT");
		List<PlannedAppointment> plannedAppointments = PlannedAppointment.MakeList(
			studentIdInt, instructorIdInt, scheduledDays,
			startYear, startMonth, startDay, startHour, startMinute,
			endYear, endMonth, endDay, endHour, endMinute);
		logger.debug("PLANS GOT");

		// Might be very first appointment, in which case this is null
		if (allAppointments != null) {
			for(PlannedAppointment plan : plannedAppointments) {
				// returns bool but we don't care, this should run with or without failure
				logger.debug("TESTING PLAN " + plan);
				plan.hasConflictWithAppointments(allAppointments);
			}
		}

		logger.debug("PLANS VERIFIED");

		StringBuilder sb = new StringBuilder("Batch appointment creation results://");
		logger.debug("Attempting to batch-create new appointments ...");
		for (PlannedAppointment plan : plannedAppointments) {
			if (plan.getResult().contains("OK")) {
				Appointment appointment = new Appointment(plan);
				DataManager.getAppointmentDAO().insert(appointment);
				sb.append("OK: %5B").append(appointment.getDateFormatted()).append("%5D//");
				logger.debug("Created new appointment: [{}]", appointment);
			} else {
				sb.append("! CONFLICT: %5B").append(plan.getDateString()).append("%5D//");
				logger.debug("Skipped planned appointment: [{}]", plan);
			}
		}
		sb.append("- End of List -");
		logger.info(DataManager.getAppointmentDAO().size() + " records total");
		this.SendPostReply("/appointments", "", sb.toString());
		return;
	}
	
}
