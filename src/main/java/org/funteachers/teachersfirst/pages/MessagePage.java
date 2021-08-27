package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class MessagePage extends PageLoader {

	// Constructor
	public MessagePage(ConnectionPackage cp) { super(cp); }

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