package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class ConfirmMakeAppointmentPage extends PageLoader {

	// Constructor
	public ConfirmMakeAppointmentPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Confirm Appointment");

		if (uid > 0) {
			
			// Get Data
			final String openingIdString = QueryHelpers.getPost(request, "openingId");
			final String studentIdString = QueryHelpers.getGet(request, "studentId"); // no defaults -- this is confirmation page -- no auto-changes allowed
			int studentIdInt;
			try {
				studentIdInt = Integer.parseInt(studentIdString);
			} catch (NumberFormatException e) {
				studentIdInt = 0;
			}
			final DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
			final String studentName = studentIdInt > 0 ? memberDAO.retrieveByID(studentIdInt).getDisplayName() : "";
			final String instructorIdString = QueryHelpers.getGet(request, "instructorId");
			int instructorIdInt;
			try {
				instructorIdInt = Integer.parseInt(instructorIdString);
			} catch (NumberFormatException e) {
				instructorIdInt = 0;
			}
			final String instructorName = instructorIdInt > 0 ? memberDAO.retrieveByID(instructorIdInt).getDisplayName() : "";
			final String dateString = QueryHelpers.getGet(request, "date");
			final String openingStartTimeString = QueryHelpers.getGet(request, "openingStartTime");
			final String openingEndTimeString = QueryHelpers.getGet(request, "openingEndTime");
			final String appointmentStartTimeString = QueryHelpers.getGet(request, "appointmentStartTime");
			final String appointmentDuration = QueryHelpers.getGet(request, "appointmentDuration");
			final String appointmentEndTimeString = DateHelpers.convertDateStartTimeAndDurationToEndTime(dateString, appointmentStartTimeString, appointmentDuration);
			
			// FreeMarker
			templateDataMap.put("openingId", openingIdString);
			templateDataMap.put("studentId", studentIdString);
			templateDataMap.put("studentName", studentName);
			templateDataMap.put("instructorId", instructorIdString);
			templateDataMap.put("instructorName", instructorName);
			templateDataMap.put("date", dateString);
			templateDataMap.put("openingStartTime", openingStartTimeString);
			templateDataMap.put("openingEndTime", openingEndTimeString);
			templateDataMap.put("appointmentStartTime", appointmentStartTimeString);
			templateDataMap.put("appointmentEndTime", appointmentEndTimeString);
		}

		// FreeMarker
		templateName = "confirmMakeAppointment.ftl";

		// Go
		trySendResponse();
	}

}