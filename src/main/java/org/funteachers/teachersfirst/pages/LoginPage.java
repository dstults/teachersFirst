package org.funteachers.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.obj.*;

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