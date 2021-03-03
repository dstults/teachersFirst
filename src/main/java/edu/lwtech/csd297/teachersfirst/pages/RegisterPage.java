package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class RegisterPage extends PageLoader {

	// Constructor
	public RegisterPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Register");

		// FreeMarker
		templateName = "register.ftl";

		// Go
		trySendResponse();
	}

}