package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class LoginPage extends PageLoader {

	// Constructor
	public LoginPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Log In");
		String name = getGetValue("name", "");

		// FreeMarker
		templateDataMap.put("name", name);
		templateName = "login.ftl";

		// Go
		trySendResponse();
	}

}