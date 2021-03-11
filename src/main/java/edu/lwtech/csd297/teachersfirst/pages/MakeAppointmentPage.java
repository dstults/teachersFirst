package edu.lwtech.csd297.teachersfirst.pages;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;

public class MakeAppointmentPage extends PageLoader {

	// Constructor
	public MakeAppointmentPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Profile");

		if (uid > 0) {
			
			// Get Opening / Previous Data
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
			final String appointmentEndTimeString = request.getParameter("appointmentEndTime") == null ? "" : request.getParameter("appointmentEndTime");
			//final String appointmentDuration = request.getParameter("appointmentDuration") == null ? "" : request.getParameter("appointmentDuration");
			
			// If thrown here on a "go back", assign these:
			String defaultStartTime = "";
			String defaultDuration = "";
			if (appointmentStartTimeString != "" && appointmentEndTimeString != "") {
				defaultStartTime = appointmentStartTimeString;
				final LocalTime st = LocalTime.parse(appointmentStartTimeString);
				final LocalTime et = LocalTime.parse(appointmentEndTimeString);
				final int diff = (int) Duration.between(st, et).toMinutes();
				defaultDuration = DateHelpers.convertMinutesToHM(diff);
			}

			// Get all possible appointment start times and durations from the opening data:
			final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
			final LocalDateTime startDateTime = LocalDateTime.parse(dateString + " " + openingStartTimeString, timeFormatter);
			LocalDateTime endDateTime = LocalDateTime.parse(dateString + " " + openingEndTimeString, timeFormatter);
			if (endDateTime.compareTo(startDateTime) < 0) endDateTime = endDateTime.plusDays(1);
			LocalDateTime currentDateTime = startDateTime.plusSeconds(0); // clone time
			List<String> possibleStartTimes = new ArrayList<String>();
			List<String> possibleDurations = new ArrayList<String>();
			int i = 0;
			while (currentDateTime.compareTo(endDateTime) < 0 && possibleStartTimes.size() <= 40) { // limit 40: someone can and will place start and end times really far apart...
				possibleStartTimes.add(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
				currentDateTime = currentDateTime.plusMinutes(15);
				i += 15;
				possibleDurations.add(DateHelpers.convertMinutesToHM(i));
			}

			// FreeMarker
			templateDataMap.put("studentId", studentIdString);
			templateDataMap.put("studentName", studentName);
			templateDataMap.put("instructorId", instructorIdString);
			templateDataMap.put("instructorName", instructorName);
			templateDataMap.put("date", dateString);
			templateDataMap.put("openingStartTime", openingStartTimeString);
			templateDataMap.put("openingEndTime", openingEndTimeString);
			templateDataMap.put("possibleStartTimes", possibleStartTimes);
			templateDataMap.put("possibleDurations", possibleDurations);
			templateDataMap.put("defaultStartTime", defaultStartTime);
			templateDataMap.put("defaultDuration", defaultDuration);
	
		}

		// FreeMarker
		templateName = "makeAppointment.ftl";

		// Go
		trySendResponse();
	}

}