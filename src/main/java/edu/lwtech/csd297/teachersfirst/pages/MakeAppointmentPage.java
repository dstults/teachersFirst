package edu.lwtech.csd297.teachersfirst.pages;

import java.text.DateFormat;
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
		templateDataMap.put("title", "Profile");

		if (uid > 0) {
			
			// Get Data
			final String instructor = request.getParameter("instructor") == null ? "" : request.getParameter("instructor");
			final String dateString = request.getParameter("date") == null ? "" : request.getParameter("date");
			final String startTimeString = request.getParameter("startTime") == null ? "" : request.getParameter("startTime");
			final String endTimeString = request.getParameter("endTime") == null ? "" : request.getParameter("endTime");

			final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
			final LocalDateTime startDateTime = LocalDateTime.parse(dateString + " " + startTimeString, timeFormatter);
			LocalDateTime endDateTime = LocalDateTime.parse(dateString + " " + endTimeString, timeFormatter);
			if (endDateTime.compareTo(startDateTime) < 0) endDateTime = endDateTime.plusDays(1);
			LocalDateTime currentDateTime = startDateTime.plusSeconds(0); // clone time
			List<String> startTimes = new ArrayList<String>();
			List<String> durations = new ArrayList<String>();
			int i = 0;
			while (currentDateTime.compareTo(endDateTime) < 0 && startTimes.size() <= 40) { // limit 40: someone can and will place start and end times really far apart...
				startTimes.add(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
				currentDateTime = currentDateTime.plusMinutes(15);
				i += 15;
				if (i >= 120) {
					durations.add((i / 60) + " hours " + (i % 60) + " minutes");
				} else if (i >= 60) {
					durations.add((i / 60) + " hour " + (i % 60) + " minutes");
				}
				else {
					durations.add(i + " minutes");
				}
			}

			int instructorId = DataManager.getMemberDAO().search(instructor).get(0).getRecID();

			// FreeMarker
			templateDataMap.put("instructorId", instructorId);
			templateDataMap.put("instructor", instructor);
			templateDataMap.put("date", dateString);
			templateDataMap.put("earliestStartTime", startTimeString);
			templateDataMap.put("latestEndTime", endTimeString);
			templateDataMap.put("startTimes", startTimes);
			templateDataMap.put("durations", durations);
	
		}

		// FreeMarker
		templateName = "makeAppointment.ftl";

		// Go
		trySendResponse();
	}

}