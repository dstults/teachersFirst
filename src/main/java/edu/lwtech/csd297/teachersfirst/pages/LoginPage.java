package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class LoginPage extends PageLoader {

	// Constructor
	public LoginPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Log In");
		String loginName = QueryHelpers.getGet(request, "loginName");

		// FreeMarker
		templateDataMap.put("loginName", loginName);
		templateName = "login.ftl";

		// Go
		trySendResponse();
	}

}