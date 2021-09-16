package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class ProfilePage extends PageLoader {

	// Constructor
	public ProfilePage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		if (this.jsonMode) {

			// JSON
			getProfileJson();

		} else {

			// FreeMarker
			templateDataMap.put("title", "Profile");
			templateName = "profile.ftl";

			trySendResponse();
		}
	}

	private void getProfileJson() {
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

		final String memberIdString = QueryHelpers.getGet(request, "memberId", Integer.toString(uid));
		int memberId;
		try {
			memberId = Integer.parseInt(memberIdString);
		} catch (NumberFormatException e) {
			memberId = 0;
		}
		
		// Get data from DAO
		final Member member;
		try {
			member = this.connectionPackage.getMemberDAO().retrieveByID(memberId);
		} catch (IndexOutOfBoundsException ex) {
			sendJsonMessage("Error: Invalid member ID.");
			return;
		}

		// Check for authority to view profile
		if (!Permissions.MemberCanSeeMember(this.operator, member)) {
			sendJsonMessage("Error retrieving member data.");
			return;
		}
		
		if (member != null ) {
			final boolean showPrivates = isAdmin || isInstructor || uid == memberId;
			final String json;
			if (showPrivates) {
				json = member.toJsonPrivate();
			} else {
				json = member.toJsonPublic();
			}
			//logger.debug("Json: " + json);
			trySendJson(json);
		} else {
			sendJsonMessage("Could not complete request.");
		}
	}

}