package edu.lwtech.csd297.teachersfirst.pages;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class MakeAppointmentPage extends PageLoader {

	// Constructor
	public MakeAppointmentPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Make Appointment");

		if (uid > 0) {
			
			// Get Opening / Previous Data
			final String openingIdString = QueryHelpers.getPost(request, "openingId");
			final String studentIdString = QueryHelpers.getGet(request, "studentId", Integer.toString(uid)); // sets default to self
			int studentIdInt;
			try {
				studentIdInt = Integer.parseInt(studentIdString);
			} catch (NumberFormatException e) {
				studentIdInt = 0;
			}
			final String studentName = studentIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(studentIdInt).getDisplayName() : "";
			final String instructorIdString = QueryHelpers.getGet(request, "instructorId");
			int instructorIdInt;
			try {
				instructorIdInt = Integer.parseInt(instructorIdString);
			} catch (NumberFormatException e) {
				instructorIdInt = 0;
			}
			final String instructorName = instructorIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(instructorIdInt).getDisplayName() : "";
			final String dateString = QueryHelpers.getGet(request, "date");
			final String openingStartTimeString = QueryHelpers.getGet(request, "openingStartTime");
			final String openingEndTimeString = QueryHelpers.getGet(request, "openingEndTime");
			final String appointmentStartTimeString = QueryHelpers.getGet(request, "appointmentStartTime");
			final String appointmentEndTimeString = QueryHelpers.getGet(request, "appointmentEndTime");
			//final String appointmentDuration = QueryHelpers.getGet(request, "appointmentDuration");
			
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
			
			// Limit 40: someone can and will place start and end times really far apart...
			while (currentDateTime.compareTo(endDateTime) < 0 && possibleStartTimes.size() <= 40) {
				possibleStartTimes.add(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
				currentDateTime = currentDateTime.plusMinutes(15);
				i += 15;
				possibleDurations.add(DateHelpers.convertMinutesToHM(i));
			}

			// FreeMarker
			templateDataMap.put("openingId", openingIdString);
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