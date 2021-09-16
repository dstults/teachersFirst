package org.funteachers.teachersfirst.pages;

import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.funteachers.teachersfirst.daos.sql.*;

public class MembersPage extends PageLoader {

	// Constructor
	public MembersPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		if (this.jsonMode) {

			// JSON
			getMemberJson();

		} else {

			// FreeMarker
			templateDataMap.put("title", "Members");
			templateName = "members.ftl";

			trySendResponse();
		}
	}

	private void getMemberJson() {
		boolean userIsLoggedIn = this.uid > 0;
		if (!userIsLoggedIn) {
			sendJsonMessage("Error: You are not logged in.");
			return;
		}
		boolean connectedToDatabase = this.connectionPackage.getConnection(this.getClass().getSimpleName()) != null;
		if (!connectedToDatabase) {
			sendJsonMessage("Error: Failed to contact database, please try again.");
			return;
		}

		final boolean showDeleted = QueryHelpers.getGetBool(request, "deleted");

		// Get data from DAO
		final MemberSqlDAO memberDAO = (MemberSqlDAO) this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
		final List<Member> members;
		if (isAdmin || isInstructor) {
			if (showDeleted) {
				members = memberDAO.retrieveAll();
			} else {
				members = memberDAO.retrieveAllUndeleted();
			}
		} else {
			members = memberDAO.retrieveAllUndeleted();
			members.removeIf(m -> (!m.getIsInstructor() && m.getRecID() != this.uid));
		}

		String json = JsonUtils.BuildArrays(members);

		trySendJson(json);
	}

}