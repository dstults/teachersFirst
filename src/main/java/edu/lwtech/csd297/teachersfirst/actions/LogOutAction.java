package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;

public class LogOutAction extends ActionRunner {

	public LogOutAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		// Do this no matter what to make sure it's clean:
		Security.logout(request, "User " + uid + " normal log out.");
		if (uid > 0 ) {
			this.SendPostReply("/services", "", "Have a nice day!");
		} else {
			this.SendPostReply("/services", "", "You're not logged in.");
		}
	}
	
}
