package edu.lwtech.csd297.teachersfirst.pages;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class ProfilePage extends PageLoader {

	// Constructor
	public ProfilePage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Profile");
		boolean jsonMode = QueryHelpers.getGetBool(request, "json");

		final String memberIdString = QueryHelpers.getGet(request, "memberId", Integer.toString(uid));
		int memberId;
		try {
			memberId = Integer.parseInt(memberIdString);
		} catch (NumberFormatException e) {
			memberId = 0;
		}
		Member member = null;

		// Make sure logged in
		if (uid > 0) {
			boolean isSelf = memberId == uid;
			templateDataMap.put("isSelf", isSelf);

			// Try to get member data
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

		// Go

		if (jsonMode) {
			if (member != null ) {
				String json = member.toJson();
				//logger.debug("Json: " + json);
				trySendJson(json);
			} else {
				sendFake404("Attempt to load JSON profile page while not logged in.");
			}
		} else {
			// FreeMarker
			templateName = "profile.ftl";
			templateDataMap.put("member", member);

			trySendResponse();
		}
	}

}