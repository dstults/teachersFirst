package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class AppointmentsPage extends PageLoader {

	// Constructor
	public AppointmentsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void LoadPage(String sanitizedQuery) {

		// Should only get info for one member...
		//final Member member = memberDAO.retrieveAll();
		//templateDataMap.put("member", member);
		final List<Member> members = memberDAO.retrieveAll();

		// Should get all appointments for said member...
		//final List<Appointment> appointments = appointmentDAO.retrieveAll();

		// FreeMarker
		templateDataMap.put("members", members);
		//templateDataMap.put("appointments", appointments);
		templateName = "appointments.ftl";

		// Go
		TrySendResponse();
	}

}