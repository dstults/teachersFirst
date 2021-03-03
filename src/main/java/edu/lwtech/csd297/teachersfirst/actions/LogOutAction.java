package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;

public class LogOutAction extends ActionRunner {

	public LogOutAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		request.getSession().setAttribute("USER_ID", 0);
		request.getSession().setAttribute("USER_NAME", "");
		this.SendRedirectToPage("/services");
	}
	
}
