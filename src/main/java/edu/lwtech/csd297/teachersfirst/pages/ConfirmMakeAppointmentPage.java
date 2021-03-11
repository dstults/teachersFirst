package edu.lwtech.csd297.teachersfirst.pages;

import java.text.DateFormat;
import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class ConfirmMakeAppointmentPage extends PageLoader {

	// Constructor
	public ConfirmMakeAppointmentPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Confirm Appointment");

		if (uid > 0) {
			
			// Get Data
			final String studentIdString = request.getParameter("studentId") == null ? Integer.toString(uid) : request.getParameter("studentId");
			int studentIdInt;
			try {
				studentIdInt = Integer.parseInt(studentIdString);
			} catch (NumberFormatException e) {
				studentIdInt = 0;
			}
			final String studentName = studentIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(studentIdInt).getDisplayName() : "";
			final String instructorIdString = request.getParameter("instructorId") == null ? "" : request.getParameter("instructorId");
			int instructorIdInt;
			try {
				instructorIdInt = Integer.parseInt(instructorIdString);
			} catch (NumberFormatException e) {
				instructorIdInt = 0;
			}
			final String instructorName = instructorIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(instructorIdInt).getDisplayName() : "";
			final String dateString = request.getParameter("date") == null ? "" : request.getParameter("date");
			final String openingStartTimeString = request.getParameter("openingStartTime") == null ? "" : request.getParameter("openingStartTime");
			final String openingEndTimeString = request.getParameter("openingEndTime") == null ? "" : request.getParameter("openingEndTime");
			final String appointmentStartTimeString = request.getParameter("appointmentStartTime") == null ? "" : request.getParameter("appointmentStartTime");
			final String appointmentDuration = request.getParameter("appointmentDuration") == null ? "" : request.getParameter("appointmentDuration");
			final String appointmentEndTimeString = DateHelpers.convertDateStartTimeAndDurationToEndTime(dateString, appointmentStartTimeString, appointmentDuration);
			
			// FreeMarker
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