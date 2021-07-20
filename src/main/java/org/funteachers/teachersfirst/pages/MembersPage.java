package org.funteachers.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class MembersPage extends PageLoader {

	// Constructor
	public MembersPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Members");

		// Check DAO connection
		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		if (memberDAO == null) {
			// Failed to contact SQL Server
			templateName = "messageOnly.ftl";
			templateDataMap.put("message", "Failed to connect with database, please try again.");
			trySendResponse();
			DataManager.resetDAOs();
			return;
		}

		// Get data from DAO
		final List<Member> members;
		if (uid > 0) {
			if (isAdmin || isInstructor) members = DataManager.getMemberDAO().retrieveAll();
			else members = List.of(DataManager.getMemberDAO().retrieveByID(uid));
		} else {
			members = null;
		}

		if (jsonMode) {
			// JSON
			String json = JsonUtils.BuildArrays(members);

			// Go
			trySendJson(json);
		} else {
			// FreeMarker
			templateName = "members.ftl";
			templateDataMap.put("members", members);

			// Go
			trySendResponse();
		}
	}

}