package org.funteachers.teachersfirst.pages;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;

public class MessagePage extends PageLoader {

	// Constructor
	public MessagePage(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	// Page-specific
	@Override
	public void loadPage() {
		templateDataMap.put("title", "Message");

		// FreeMarker
		templateName = "messageOnly.ftl";

		// Go
		trySendResponse();
	}

}