package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class LogInAction extends ActionRunner {

	public LogInAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		if (uid > 0) {
			this.SendPostReply("/appointments", "", "You're already logged in!");
			return;
		}

		String loginName = QueryHelpers.getPost(request, "loginName");
		String password = QueryHelpers.getPost(request, "password");

		if (loginName == null || loginName.isEmpty() || password == null || password.isEmpty()) {
			this.SendPostReply("/login", "loginName=" + loginName, "Please enter a valid user name and password.");
			return;
		}

		if (!errorMessage.isEmpty()) {
			this.SendPostReply("/login", "loginName=" + loginName, errorMessage);
			return;
		}

		Member member = Security.checkPassword(loginName, password);
		if (member != null) {
			Security.login(request, member);
			this.SendPostReply("/appointments", "", "Welcome back, " + member.getDisplayName());
			return;
		} else {
			this.SendPostReply("/login", "loginName=" + loginName, "Could not log you in.");
			return;
		}
	}


	
}