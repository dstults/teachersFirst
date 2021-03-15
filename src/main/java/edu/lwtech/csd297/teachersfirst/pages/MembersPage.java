package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class MembersPage extends PageLoader {

	// Constructor
	public MembersPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Members");

		// Should only show members that it should show based on who's querying...
		final List<Member> members;  // let's play effectively final games...

		if (uid > 0) {
			if (isAdmin || isInstructor) members = DataManager.getMemberDAO().retrieveAll();
			else members = List.of(DataManager.getMemberDAO().retrieveByID(uid));
		} else {
			members = null;
		}
		
		// FreeMarker
		templateName = "members.ftl";
		templateDataMap.put("members", members);

		// Go
		trySendResponse();
	}

}