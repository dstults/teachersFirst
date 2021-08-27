package org.funteachers.teachersfirst.pages;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.SecurityChecker;
import org.funteachers.teachersfirst.obj.*;

public class ProfilePage extends PageLoader {

	// Constructor
	public ProfilePage(HttpServletRequest request, HttpServletResponse response, SecurityChecker security) { super(request, response, security); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Profile");

		// Go
		if (jsonMode) {
			final String memberIdString = QueryHelpers.getGet(request, "memberId", Integer.toString(uid));
			int memberId;
			try {
				memberId = Integer.parseInt(memberIdString);
			} catch (NumberFormatException e) {
				memberId = 0;
			}
			Member member = null;
			
			// Check DAO connection
			if (uid > 0) {
				boolean isSelf = memberId == uid;
				templateDataMap.put("isSelf", isSelf);
				
				// Get data from DAO
				try {
					member = DataManager.getMemberDAO().retrieveByID(memberId);
				} catch (IndexOutOfBoundsException ex) {
					templateDataMap.put("message", "Invalid member ID.");
				}
				
				// Check authority to view: user is self, student is viewing instructor, or instructor/admin sees all
				if (isAdmin || isInstructor || uid == memberId || (isStudent && member.getIsInstructor())) {
					// OK
				} else {
					member = null; // clear this back out
					templateDataMap.put("message", "Error retrieving member data.");
				}
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
		} else {
			// FreeMarker
			templateName = "profile.ftl";

			trySendResponse();
		}
	}

}