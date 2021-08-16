package org.funteachers.teachersfirst.pages;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.Security;

public class LogoutPage extends PageLoader {

	// Constructor
	public LogoutPage(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Log Out");

		// FreeMarker
		templateName = "logout.ftl";

		// Go
		trySendResponse();
	}

}