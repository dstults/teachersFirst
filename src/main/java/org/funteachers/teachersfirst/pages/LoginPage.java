package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class LoginPage extends PageLoader {

	// Constructor
	public LoginPage(ConnectionPackage cp) { super(cp); }

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