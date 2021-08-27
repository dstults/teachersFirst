package org.funteachers.teachersfirst;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

import freemarker.template.*;

public abstract class PageLoader {

	// Declarations
	
	final protected static Logger logger = LogManager.getLogger();

	final protected ConnectionPackage connectionPackage;
	final protected HttpServletRequest request;
	final protected HttpServletResponse response;
	final protected SecurityChecker security;

	final protected Member operator;
	final protected int uid;
	final protected String userName;
	final protected boolean isAdmin;
	final protected boolean isInstructor;
	final protected boolean isStudent;
	final protected Map<String, Object> templateDataMap;
	final protected boolean jsonMode;

	protected String templateName = null;
	protected String errorMessage = "";

	private void getGetMessage() {
		String message = QueryHelpers.getGet(request, "message");
		message = message.replace("//", "<br>\n			");
		this.templateDataMap.put("message", message);
	}

	// Constructors
	protected PageLoader(ConnectionPackage cp) {
		if (!cp.validate()) cp.reset(); // Validate SQL connection first

		this.connectionPackage = cp;
		this.request = cp.getRequest();
		this.response = cp.getResponse();
		this.security = cp.getSecurity();

		this.operator = security.getMemberFromRequestCookieToken();
		if (operator != null) {
			this.uid = this.operator.getRecID();
			this.userName = this.operator.getDisplayName();
			this.isAdmin = this.operator.getIsAdmin();
			this.isInstructor = this.operator.getIsInstructor();
			this.isStudent = this.operator.getIsStudent();
		} else {
			this.uid = 0;
			this.userName = "Guest";
			this.isAdmin = false;
			this.isInstructor = false;
			this.isStudent = false;
		}
		//logger.debug("isAdmin [{}] isInstructor [ {} ] isStudent [ {} ]", this.isAdmin, this.isInstructor, this.isStudent);

		this.templateDataMap = new HashMap<>();
		getGetMessage(); // Checks query for message data
		this.jsonMode = QueryHelpers.getGetBool(request, "json");

		templateDataMap.put("canRegister", DataManager.enableOpenRegistration);
		templateDataMap.put("websiteTitle", DataManager.websiteTitle);
		templateDataMap.put("websiteSubtitle", DataManager.websiteSubtitle);
		templateDataMap.put("showWelcome", true);

		// TODO: This should be unified into one Zulu output, and handled client-side with JS for dynamic updating purposes
		// TODO: These should be set by configuration file
		templateDataMap.put("time1Name", "Beijing");
		templateDataMap.put("time1Time", DateHelpers.getNowDateTimeString("Asia/Shanghai"));
		templateDataMap.put("time2Name", "Los Angeles");
		templateDataMap.put("time2Time", DateHelpers.getNowDateTimeString("America/Los_Angeles"));

		templateDataMap.put("userId", this.uid);
		templateDataMap.put("userName", this.userName);
		templateDataMap.put("isAdmin", this.isAdmin);
		templateDataMap.put("isInstructor", this.isInstructor);
		templateDataMap.put("isStudent", this.isStudent);
	}

	// Public entry point

	public abstract void loadPage();

	// Protected Methods (shared magic between all pages)

	protected void sendFake404(String description) {
		logger.debug("====================== SECURITY ALERT ======================");
		logger.debug("Description: {}", description);
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		logger.debug("Sanitized Query: {}", sanitizedQuery);
		final String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
		logger.debug("Page Path: {}", pathInfo);
		try {
			this.response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			logger.error("Unable to send fake 404 response code.", e);
		}
	}

	protected void trySendResponse() {
		trySendResponse("", 200);
	}

	protected void trySendResponse(int statusCode) {
		trySendResponse("", statusCode);
	}

	protected void trySendResponse(String headerOverwrite) {
		trySendResponse(headerOverwrite, 200);
	}

	protected void trySendResponse(String headerOverwrite, int statusCode) {

		if (this.templateName == null || this.templateName == "") {
			// Send 404 error response
			try {
				this.response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e) {
				logger.error("Unable to send 404 response code.", e);
			}
			return;
		}

		if (!headerOverwrite.equals("")) response.setHeader("Content-Type", headerOverwrite);
		response.setStatus(statusCode);

		// Process template:
		logger.debug("Processing Template: " + this.templateName);
		try (PrintWriter out = response.getWriter()) {
			Template view = FreeMarkerSetup.freeMarkerConfig.getTemplate(this.templateName);
			view.process(templateDataMap, out);
		} catch (TemplateException | MalformedTemplateNameException e) {
			logger.error("Template Error: ", e);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

	protected void trySendJson(String json) {
		// Send JSON:
		logger.debug("Attempting to send JSON GET reply...");
		response.setHeader("Content-Type", "application/json");
		response.setStatus(200);
		try (ServletOutputStream out = response.getOutputStream()) {
			out.println(json);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

	protected void sendJsonMessage(String message) {
		final String messageJson = "\"message\": \"" + message.trim() + "\""; // include message even if empty
		final String fullJson = "{ " + messageJson + " }";

		// Send JSON:
		logger.debug("Attempting to send JSON GET reply...");
		response.setHeader("Content-Type", "application/json");
		response.setStatus(200);
		try (ServletOutputStream out = response.getOutputStream()) {
			out.println(fullJson);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

}