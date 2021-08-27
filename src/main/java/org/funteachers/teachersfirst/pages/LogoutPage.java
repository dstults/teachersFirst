package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class LogoutPage extends PageLoader {

	// Constructor
	public LogoutPage(ConnectionPackage cp) { super(cp); }

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