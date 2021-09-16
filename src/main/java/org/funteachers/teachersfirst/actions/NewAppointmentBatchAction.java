package org.funteachers.teachersfirst.actions;

import java.time.*;
import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class NewAppointmentBatchAction extends ActionRunner {

	public NewAppointmentBatchAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}
		if (GlobalConfig.instructorAdminMakeAppointmentsRequiresOpening || (!isAdmin && !isInstructor)) {
			this.sendPostReply("/services", "", "Batch appointment function disabled.");
		}

		final String studentIdString = QueryHelpers.getPost(request, "studentId");
		final int studentIdInt;
		try {
			studentIdInt = Integer.parseInt(studentIdString);
		} catch (NumberFormatException e) {
			this.sendPostReply("/make_appointment_batch", "", "Could not parse student ID!");
			return;
		}
		final DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().toString());
		final Member student = memberDAO.retrieveByID(studentIdInt);
		if (student == null) {
			this.sendPostReply("/make_appointment_batch", "", "Student with ID %5B" + studentIdString + "%5D does not exist!");
			return;
		}

		final String instructorIdString = QueryHelpers.getPost(request, "instructorId");
		final int instructorIdInt;
		try {
			instructorIdInt = Integer.parseInt(instructorIdString);
		} catch (NumberFormatException e) {
			this.sendPostReply("/make_appointment_batch", "", "Could not parse instructor ID!");
			return;
		}
		final Member instructor = memberDAO.retrieveByID(instructorIdInt);
		if (instructor == null) {
			this.sendPostReply("/make_appointment_batch", "", "Instructor with ID %5B" + instructorIdString + "%5D does not exist!");
			return;
		} else if (studentIdInt == instructorIdInt) {
			this.sendPostReply("/make_appointment_batch", "", "Student ID and Instructor ID both " + studentIdString + " -- appointments can not be made with self.");
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
			this.sendPostReply("/make_appointment_batch", "", "Could not parse start date: %5B" + startDateString + "%5D !");
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
			this.sendPostReply("/make_appointment_batch", "", "Could not parse end date: %5B" + endDateString + "%5D !");
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
			this.sendPostReply("/make_appointment_batch", "", "Could not parse start time: %5B" + startTimeString + "%5D L:" + startTimeString.split(":").length + " !");
			return;
		}

		if (startMinute != 0 && startMinute != 15 && startMinute != 30 && startMinute != 45) {
			this.sendPostReply("/make_appointment_batch", "", "Start minute %5B" + startMinute + "%5D not allowed, must be multiple of 15!");
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
			this.sendPostReply("/make_appointment_batch", "", "Could not parse end time: %5B" + endTimeString + "%5D !");
			return;
		}

		if (endMinute != 0 && endMinute != 15 && endMinute != 30 && endMinute != 45) {
			this.sendPostReply("/make_appointment_batch", "", "End minute %5B" + endMinute + "%5D not allowed, must be multiple of 15!");
			return;
		}

		int startTime = startHour * 60 + startMinute;
		int endTime = endHour * 60 + endMinute;
		if (endTime < startTime) {
			endTime += 1440;
			endDay++;
		}
		if (startTime > 1440 || endTime > 1440) {
			this.sendPostReply("/make_appointment_batch", "", "Invalid start time or end time. Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
			return;
		}
		if (endTime - 720 > startTime) {
			this.sendPostReply("/make_appointment_batch", "", "Appointments must not be longer than 12 hours! Start Time: %5B" + startTime + "%5D End Time: %5B" + endTime + "%5D");
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
			this.sendPostReply("/make_appointment_batch", "", "Couldn't parse your days of the week.");
			return;
		}

		// Create planned appointment list
		List<PlannedAppointment> plannedAppointments = PlannedAppointment.MakeList(
			studentIdInt, instructorIdInt, scheduledDays,
			startYear, startMonth, startDay, startHour, startMinute,
			endYear, endMonth, endDay, endHour, endMinute);
		logger.debug("PLANS GOT: " + plannedAppointments.size());

		// Make sure list length greater than zero
		if (plannedAppointments.size() == 0) {
			this.sendPostReply("/make_appointment_batch", "", "Could not find any valid planned appointments in provided scope.");
			return;
		}

		// Make sure no conflicting appointments
		final DAO<Appointment> appointmentDAO = this.connectionPackage.getAppointmentDAO(this.getClass().toString());
		final List<Appointment> allAppointments = appointmentDAO.retrieveAll();
		logger.debug("APPOINTMENTS GOT: " + allAppointments.size());

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

		int successCount = 0;
		float lengthEach = plannedAppointments.get(0).getLength();
		for (PlannedAppointment plan : plannedAppointments) {
			if (plan.getResult().contains("OK")) {
				successCount++;
				Appointment appointment = new Appointment(plan);
				appointmentDAO.insert(appointment);
				sb.append("OK: %5B").append(appointment.getDateFormatted()).append("%5D//");
				logger.debug("Created new appointment: [{}]", appointment);
			} else {
				sb.append("! CONFLICT: %5B").append(plan.getDateString()).append("%5D//");
				logger.debug("Skipped planned appointment: [{}]", plan);
			}
		}
		sb.append("- End of List -");

		// Deduct credits from student based on number of successful creations times length of each
		if (successCount > 0 && lengthEach > 0.0) {
			float credits = student.getCredits();
			credits -= successCount * lengthEach;
			student.setCredits(this.connectionPackage, uid, operator.getLoginName(), "batch create " + successCount + " @ " + lengthEach + " hrs", credits);
		}

		logger.info(appointmentDAO.size() + " records total");
		this.sendPostReply("/appointments", "", sb.toString());
		return;
	}
	
}
