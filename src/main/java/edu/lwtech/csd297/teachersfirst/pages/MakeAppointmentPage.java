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

		// FreeMarker
		templateName = "makeAppointment.ftl";
		templateDataMap.put("instructor", instructor);
		templateDataMap.put("startTime", startTime);
		templateDataMap.put("endTime", endTime);

		// Go
		trySendResponse();
	}

}