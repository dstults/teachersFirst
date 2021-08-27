package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class ServicesPage extends PageLoader {

	// Notes for making classes that FreeMarker can actually read:
	//  - class must be public
	//  - class variables must be private
	//  - class variables must have appropriate getters

	// Constructor
	public ServicesPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		templateName = "services.ftl";
		trySendResponse();
	}

}