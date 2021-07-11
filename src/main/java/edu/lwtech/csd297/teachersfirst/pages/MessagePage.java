package edu.lwtech.csd297.teachersfirst.pages;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;

public class MessagePage extends PageLoader {

	// Constructor
	public MessagePage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

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