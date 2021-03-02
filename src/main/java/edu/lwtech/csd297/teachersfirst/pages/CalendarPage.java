package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class CalendarPage extends PageLoader {

	// Constructor
	public CalendarPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void LoadPage() {
		templateDataMap.put("title", "Calendar");
		
		// Should only show members that it should show based on who's querying...
		final List<Member> members = memberDAO.retrieveAll();
		
		// FreeMarker
		templateName = "calendar.ftl";
		templateDataMap.put("members", members);

		// Go
		TrySendResponse();
	}

}