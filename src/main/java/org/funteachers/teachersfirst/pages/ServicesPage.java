package org.funteachers.teachersfirst.pages;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;

public class ServicesPage extends PageLoader {

	// Notes for making classes that FreeMarker can actually read:
	//  - class must be public
	//  - class variables must be private
	//  - class variables must have appropriate getters

	// Constructor
	public ServicesPage(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		templateName = "services.ftl";
		trySendResponse();
	}

}