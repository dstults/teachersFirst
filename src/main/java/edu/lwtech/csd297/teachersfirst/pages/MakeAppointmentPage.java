package edu.lwtech.csd297.teachersfirst.pages;

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

		// Get Data
		final String instructor = request.getParameter("instructor") == null ? "" : request.getParameter("instructor");
		final String startTime = request.getParameter("startTime") == null ? "" : request.getParameter("startTime");
		final String endTime = request.getParameter("endTime") == null ? "" : request.getParameter("endTime");

		LinkedList<String> durations = new LinkedList<String>();
		for (int i = 15; i <= 120; i += 15) {
			if (i >= 60) {
				durations.add((i / 60) + " hour " + (i % 60) + " minutes");
			}
			else {
				durations.add(i + " minutes");
			}
		}

		// FreeMarker
		templateName = "makeAppointment.ftl";
		templateDataMap.put("instructor", instructor);
		templateDataMap.put("startTime", startTime);
		templateDataMap.put("endTime", endTime);
		templateDataMap.put("durations", durations);

		// Go
		trySendResponse();
	}

}