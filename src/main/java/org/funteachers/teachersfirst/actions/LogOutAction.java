package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.QueryHelpers;
import org.funteachers.teachersfirst.managers.*;

public class LogOutAction extends ActionRunner {

	public LogOutAction(ConnectionPackage cp) { super(cp); }

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
