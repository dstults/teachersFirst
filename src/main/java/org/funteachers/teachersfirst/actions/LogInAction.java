package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.QueryHelpers;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class LogInAction extends ActionRunner {

	public LogInAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {
		if (uid > 0) {
			this.sendPostReply("/appointments", "", "You're already logged in!");
			return;
		}

		String loginName = QueryHelpers.getPost(request, "loginName");
		String password = QueryHelpers.getPost(request, "password");

		if (loginName == null || loginName.isEmpty() || password == null || password.isEmpty()) {
			if ((loginName == null || loginName.isEmpty()) && password != null && !password.isEmpty()) {
				this.sendPostReply("/login", "loginName=" + loginName, "Please enter a valid password.");
			} else if (loginName != null && !loginName.isEmpty() && (password == null || password.isEmpty())) {
				this.sendPostReply("/login", "loginName=" + loginName, "Please enter a valid user name.");
			} else {
				this.sendPostReply("/login", "loginName=" + loginName, "Please enter a valid user name and password.");
			}
			return;
		}

		if (!errorMessage.isEmpty()) {
			this.sendPostReply("/login", "loginName=" + loginName, errorMessage);
			return;
		}

		Member member = security.checkPassword(loginName, password);
		if (member != null) {
			this.sendPostReply("/appointments", "", "Welcome back, " + member.getDisplayName());
			return;
		} else {
			this.sendPostReply("/login", "loginName=" + loginName, "Could not log you in.");
			return;
		}
	}
	
}