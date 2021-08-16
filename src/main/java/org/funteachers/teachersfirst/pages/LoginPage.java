package org.funteachers.teachersfirst.pages;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;

public class LoginPage extends PageLoader {

	// Constructor
	public LoginPage(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Log In");
		String loginName = QueryHelpers.getGet(request, "loginName");

		// FreeMarker
		templateDataMap.put("loginName", loginName);
		templateName = "login.ftl";

		// Go
		trySendResponse();
	}

}