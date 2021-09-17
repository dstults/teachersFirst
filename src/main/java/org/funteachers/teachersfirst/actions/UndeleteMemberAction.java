package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.funteachers.teachersfirst.daos.sql.*;

public class UndeleteMemberAction extends ActionRunner {

	public UndeleteMemberAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendJsonMessage("Please sign in or register to use this feature!", false);
			return;
		}

		final String memberIdString = QueryHelpers.getPost(request, "memberID");
		int memberIdInt;
		try {
			memberIdInt = Integer.parseInt(memberIdString);
		} catch (NumberFormatException e) {
			memberIdInt = 0;
		}
		final MemberSqlDAO memberDAO = (MemberSqlDAO) this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
		final Member member = memberDAO.retrieveByID(memberIdInt);
		if (member == null) {
			this.sendJsonMessage("Member [ID: " + memberIdString + " ] not found!", false);
			return;
		}

		// Make sure the person has the authority
		if (!Permissions.MemberCanUndeleteMember(this.operator, member)) {
			this.sendJsonMessage("You are not authorized to undelete this member.", false);
			return;
		}

		memberDAO.softUndelete(memberIdInt);
		
		this.sendJsonMessage("Member [ID: " + memberIdString + " ] undeleted!", true, "/members");
		return;
	}
	
}
