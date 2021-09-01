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

		// General
		templateName = "register.ftl";
		templateDataMap.put("title", "Register");
		templateDataMap.put("registerOther", !this.selfRegister);
		
		// Get query data
		String loginName = QueryHelpers.getGet(request, "loginName");
		String displayName = QueryHelpers.getGet(request, "displayName");
		String gender = QueryHelpers.getGet(request, "gender");
		String phone1 = QueryHelpers.getGet(request, "phone1");
		String phone2 = QueryHelpers.getGet(request, "phone2");
		String email = QueryHelpers.getGet(request, "email");

		// Add query data to FM
		templateDataMap.put("loginName", loginName);
		templateDataMap.put("displayName", displayName);
		templateDataMap.put("maleChecked", gender.equals("m") ? " checked" : "");
		templateDataMap.put("femaleChecked", gender.equals("f") ? " checked" : "");
		templateDataMap.put("otherChecked", !gender.equals("m") && !gender.equals("f") ? " checked" : "");
		templateDataMap.put("phone1", phone1);
		templateDataMap.put("phone2", phone2);
		templateDataMap.put("email", email);

		// Go
		trySendResponse();
	}

}