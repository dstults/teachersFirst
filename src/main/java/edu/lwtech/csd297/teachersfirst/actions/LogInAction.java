package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class LogInAction extends ActionRunner {

	public LogInAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		String loginName = QueryHelpers.getPost(request, "loginName");
		String password = QueryHelpers.getPost(request, "password");

		if (loginName == null || loginName.isEmpty() || password == null || password.isEmpty()) {
			this.SendRedirectToPage("/login?name=" + loginName + "&message=Please enter a valid user name and password.");
			return;
		}

		Member member = Security.checkPassword(loginName, password);
		if (member != null) {
			Security.login(request, member);
			this.SendRedirectToPage("/appointments");
			return;
		} else {
			this.SendRedirectToPage("/login?name=" + loginName + "&message=Could not log you in.");
			return;
		}
	}
	
}