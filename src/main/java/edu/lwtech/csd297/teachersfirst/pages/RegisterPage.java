package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.DataManager;
import edu.lwtech.csd297.teachersfirst.QueryHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class RegisterPage extends PageLoader {

	// Constructor
	public RegisterPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		if (!DataManager.enableOpenRegistration) {
			this.sendFake404("WARNING: User attempted to load open registration page when said page should not be visible.");
			return;
		}

		templateDataMap.put("title", "Register");
		String loginName = QueryHelpers.getGet(request, "loginName");
		String displayName = QueryHelpers.getGet(request, "displayName");
		String gender = QueryHelpers.getGet(request, "gender");
		String phone1 = QueryHelpers.getGet(request, "phone1");
		String phone2 = QueryHelpers.getGet(request, "phone2");
		String email = QueryHelpers.getGet(request, "email");

		// FreeMarker
		templateDataMap.put("loginName", loginName);
		templateDataMap.put("displayName", displayName);
		templateDataMap.put("gender", gender);
		templateDataMap.put("phone1", phone1);
		templateDataMap.put("phone2", phone2);
		templateDataMap.put("email", email);
		templateName = "register.ftl";

		// Go
		trySendResponse();
	}

}