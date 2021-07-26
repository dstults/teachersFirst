package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.obj.*;

public class LogInAction extends ActionRunner {

	public LogInAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void runAction() {
		if (uid > 0) {
			this.sendPostReply("/appointments", "", "You're already logged in!");
			return;
		}

		String loginName = QueryHelpers.getPost(request, "loginName");
		String password = QueryHelpers.getPost(request, "password");

		if (loginName == null || loginName.isEmpty() || password == null || password.isEmpty()) {
			this.sendPostReply("/login", "loginName=" + loginName, "Please enter a valid user name and password.");
			return;
		}

		if (!errorMessage.isEmpty()) {
			this.sendPostReply("/login", "loginName=" + loginName, errorMessage);
			return;
		}

		Member member = Security.checkPassword(loginName, password);
		if (member != null) {
			Security.login(request, member);
			this.sendPostReply("/appointments", "", "Welcome back, " + member.getDisplayName());
			return;
		} else {
			this.sendPostReply("/login", "loginName=" + loginName, "Could not log you in.");
			return;
		}
	}


	
}