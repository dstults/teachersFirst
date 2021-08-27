package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.QueryHelpers;
import org.funteachers.teachersfirst.managers.SecurityChecker;

public class LogOutAction extends ActionRunner {

	public LogOutAction(HttpServletRequest request, HttpServletResponse response, SecurityChecker security) { super(request, response, security); }

	@Override
	public void runAction() {
		boolean allDevices = QueryHelpers.getGetBool(request, "allDevices");
		
		// Do this no matter what to make sure it's clean:
		security.logout(operator, "Normal log out.", allDevices);
		if (uid > 0 ) {
			this.sendPostReply("/services", "", "Have a nice day!");
		} else {
			this.sendPostReply("/services", "", "You're not logged in.");
		}
	}
	
}
