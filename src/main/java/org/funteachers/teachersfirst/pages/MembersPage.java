package org.funteachers.teachersfirst.pages;

import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

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
		boolean connectedToDatabase = this.connectionPackage.getConnection() != null;
		if (!connectedToDatabase) {
			sendJsonMessage("Error: Failed to contact database, please try again.");
			return;
		}

		// Get data from DAO
		final List<Member> members;
		if (isAdmin || isInstructor) {
			members = this.connectionPackage.getMemberDAO().retrieveAll();
		} else {
			members = List.of(this.connectionPackage.getMemberDAO().retrieveByID(uid));
		}

		String json = JsonUtils.BuildArrays(members);

		trySendJson(json);
	}

}