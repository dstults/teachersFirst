package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.funteachers.teachersfirst.daos.sql.*;

public class DeleteMemberAction extends ActionRunner {

	public DeleteMemberAction(ConnectionPackage cp) { super(cp); }

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
		final MemberSqlDAO memberDAO = (MemberSqlDAO) this.connectionPackage.getMemberDAO();
		final Member member = memberDAO.retrieveByID(memberIdInt);
		if (member == null) {
			this.sendJsonMessage("Member %5B" + memberIdString + "%5D not found!", false);
			return;
		}

		// Make sure the person has the authority
		if (!Permissions.MemberCanDeleteMember(this.operator, member)) {
			this.sendJsonMessage("You are not authorized to delete this member.", false);
			return;
		}

		logger.debug("Attempting to delete member " + member.toString() + " ...");
		
		memberDAO.softDelete(memberIdInt);
		//logger.info(DataManager.getMemberDAO().size() + " records total");
		logger.debug("Soft-deleted member ID: [{}]", memberIdInt);
		
		this.sendJsonMessage("Member %5B" + memberIdString + "%5D, deleted!", true, "/members");
		return;
	}
	
}
