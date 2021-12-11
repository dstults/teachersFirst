package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class MakeOpeningsPage extends PageLoader {

	// Constructor
	public MakeOpeningsPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Create Openings");

		// FreeMarker Template
		templateName = "newOpenings.ftl";

		// Go
		trySendResponse();
	}

}