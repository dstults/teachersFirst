package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;

public class LogInAction extends ActionRunner {

	public LogInAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		String name = getPostValue("name", "");
		String password = getPostValue("password", "");

		if (name == null || name == "" || password == null || password == "") {
			this.SendRedirectToPage("/login?name=" + name + "&message=Please enter a valid user name and password.");
			return;
		}

		if (Security.checkPassword(1, password)) {
			logger.debug(name + " logged in.");
			request.getSession().setAttribute("USER_ID", 1);
			request.getSession().setAttribute("USER_NAME", name);
			this.SendRedirectToPage("/appointments");
			return;
		} else {
			this.SendRedirectToPage("/login?name=" + name + "&message=Could not log you in.");
			return;
		}
	}
	
}
