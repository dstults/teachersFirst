package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class MembersPage extends PageLoader {

	// Constructor
	public MembersPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void LoadPage(String sanitizedQuery) {
		
		// Should only show members that it should show based on who's querying...
		final List<Member> members = memberDAO.retrieveAll();
		
		// FreeMarker
		templateName = "members.ftl";
		templateDataMap.put("members", members);

		// Go
		TrySendResponse();
	}

}