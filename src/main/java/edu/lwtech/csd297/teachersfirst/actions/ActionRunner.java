package edu.lwtech.csd297.teachersfirst.actions;

import java.io.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public abstract class ActionRunner {

	// Declarations

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected boolean jsonMode;
	protected int uid;
	protected boolean isAdmin;
	protected boolean isInstructor;
	protected boolean isStudent;
	protected String errorMessage = "";
	protected static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	// Constructors

	protected ActionRunner(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		jsonMode = QueryHelpers.getGetBool(request, "json");
		uid = Security.getUserId(request);
		if (uid > 0) {
			Member member = DataManager.getMemberDAO().retrieveByID(uid);
			if (member != null) {
				isAdmin = member.getIsAdmin();
				isInstructor = member.getIsInstructor();
				isStudent = member.getIsStudent();
			} else {
				errorMessage += " Failed to contact database, try again later. ";
				DataManager.resetDAOs();
			}
		} else {
			if (!DataManager.validateSQLConnection()) {
				errorMessage += " Failed to contact database, try again later. ";
				DataManager.resetDAOs();
			}
		}
	}

	// Public entry point

	public abstract void RunAction();

	// Protected Methods (shared magic between all actions)

	protected void SendPostReply(String nextPage, String query, String message) {
		if (!jsonMode) {
			// normal web behavior
			String fullResponseURL = nextPage;
			if (!query.isEmpty() || !message.isEmpty()) fullResponseURL += "?";
			if (!query.isEmpty()) fullResponseURL += query;
			if (!query.isEmpty() && !message.isEmpty()) fullResponseURL += "&";
			if (!message.isEmpty()) fullResponseURL += "message=" + message.trim();
			try {
				response.sendRedirect(fullResponseURL);
			} catch (IOException e) {
				logger.error("IO Error: ", e);
			}
		} else {
			// for RESTful applications
			String messageJson = "'message':'" + message.trim() + "'"; // include message even if empty
			if (!query.isEmpty()) messageJson += ",";
			
			String fullJson = "{" + messageJson + JsonUtils.queryToJson(query) + "}";

			// send json:
			logger.debug("Attempting to send json...");
			try (ServletOutputStream out = response.getOutputStream()) {
				out.println(fullJson);
			} catch (IOException e) {
				logger.error("IO Error: ", e);
			}
		}
	}

}