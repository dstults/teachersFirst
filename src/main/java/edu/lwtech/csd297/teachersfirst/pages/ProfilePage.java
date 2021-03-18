package edu.lwtech.csd297.teachersfirst.pages;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class ProfilePage extends PageLoader {

	// Constructor
	public ProfilePage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Profile");

		// Should only show members that it should show based on who's querying...
		final Member member = DataManager.getMemberDAO().retrieveAll().get(0);
		
		// FreeMarker
		templateName = "profile.ftl";
		templateDataMap.put("member", member);

		// Go
		trySendResponse();
	}

}