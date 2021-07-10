package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.PageLoader;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class LogoutPage extends PageLoader {

	// Constructor
	public LogoutPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

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