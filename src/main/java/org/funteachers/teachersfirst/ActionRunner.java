package org.funteachers.teachersfirst;

import java.io.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public abstract class ActionRunner {

	// Declarations

	final protected static Logger logger = LogManager.getLogger();

	final protected ConnectionPackage connectionPackage;
	final protected HttpServletRequest request;
	final protected HttpServletResponse response;
	final protected SecurityChecker security;

	final protected Member operator;
	final protected int uid;
	final protected boolean isAdmin;
	final protected boolean isInstructor;
	final protected boolean isStudent;

	protected String errorMessage = "";

	// Constructors

	protected ActionRunner(ConnectionPackage cp) {
		//if (!cp.validate()) cp.reset(); // Try to validate-reset SQL connection

		this.connectionPackage = cp;
		this.request = cp.getRequest();
		this.response = cp.getResponse();
		this.security = cp.getSecurity();

		this.operator = security.getMemberFromRequestCookieToken();
		if (this.operator != null) {
			this.uid = this.operator.getRecID();
			this.isAdmin = this.operator.getIsAdmin();
			this.isInstructor = this.operator.getIsInstructor();
			this.isStudent = this.operator.getIsStudent();
		} else {
			this.uid = 0;
			this.isAdmin = false;
			this.isInstructor = false;
			this.isStudent = false;
		}

	}

	// Public entry point

	public abstract void runAction();

	// Protected Methods (shared magic between all actions)

	protected void sendJsonMessage(String message, boolean success) {
		sendJsonMessage(message, success, "");
	}

	protected void sendJsonMessage(String message, boolean success, String redirect) {
		final String messageJson = "\"message\": \"" + message.trim() + "\""; // include message even if empty
		final String successJson = ", \"success\": " + (success ? "true" : "false"); // indicate if operation succeeded or failed
		final String redirectJson = !redirect.isEmpty() ? ", \"redirect\": \"" + redirect.trim() + "\"" : ""; // include if redirect link provided
		final String fullJson = "{ " + messageJson + successJson + redirectJson + " }";

		// send json:
		//logger.debug("Attempting to send JSON POST reply...");
		response.setHeader("Content-Type", "application/json");
		response.setStatus(200);
		try (ServletOutputStream out = response.getOutputStream()) {
			out.println(fullJson);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

	protected void sendPostReply(String nextPage, String query, String message) {
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
	}

}