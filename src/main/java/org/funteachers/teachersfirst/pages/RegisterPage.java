package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class RegisterPage extends PageLoader {

	private boolean selfRegister;

	// Constructor
	public RegisterPage(ConnectionPackage cp, boolean selfRegister) {
		super(cp);
		this.selfRegister = selfRegister;
	}

	// Page-specific

	@Override
	public void loadPage() {
		if (this.selfRegister && !DataManager.enableOpenRegistration) {
			this.sendFake404("WARNING: User attempted to load open registration page when said page should not be visible.");
			return;
		}
		if (!this.selfRegister && !this.isInstructor && !this.isAdmin) {
			this.sendFake404("WARNING: A non-administrator, non-instructor user is attempting to create a new user.");
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