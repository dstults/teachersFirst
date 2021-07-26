package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;

public class LogOutAction extends ActionRunner {

	public LogOutAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void runAction() {
		// Do this no matter what to make sure it's clean:
		Security.logout(request, "User " + uid + " normal log out.");
		if (uid > 0 ) {
			this.sendPostReply("/services", "", "Have a nice day!");
		} else {
			this.sendPostReply("/services", "", "You're not logged in.");
		}
	}
	
}
