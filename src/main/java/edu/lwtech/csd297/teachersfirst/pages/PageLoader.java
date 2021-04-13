package edu.lwtech.csd297.teachersfirst.pages;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;

import freemarker.template.*;
import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public abstract class PageLoader {

	// Declarations

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	protected String templateName = null;
	protected Map<String, Object> templateDataMap;
	protected int uid;
	protected boolean isAdmin = false;
	protected boolean isInstructor = false;
	protected boolean isStudent = false;

	// Static Declarations (shared variables to handle freemarker and DAOs)

	protected static final Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());

	// Static Methods (basically meta-constructors/destructors for class-internal
	// variables)

	public static void initializeFreeMarker(String resourcesDir) throws ServletException {
		String templateDir = resourcesDir + "/templates";
		try {
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			String msg = "Template directory not found: " + templateDir;
			logger.fatal(msg, e);
			throw new UnavailableException(msg);
		}
	}

	// Constructors

	protected PageLoader(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		templateDataMap = new HashMap<>();

		// Handle session / cookies
		uid = Security.getUserId(request);
		if (uid > 0) {
			Member member = DataManager.getMemberDAO().retrieveByID(uid);
			if (member != null) {
				isAdmin = member.getIsAdmin();
				isInstructor = member.getIsInstructor();
				isStudent = member.getIsStudent();
			} else {
				Security.logout(request, "Bad session data or failure to contact database, try again later.");
				templateDataMap.put("messge", "Error contacting SQL server. Error code: " + uid + ".a5j // For your own security you will need to log in again.");
				templateName = "messageOnly.ftl";
				this.trySendResponse();
				DataManager.resetDAOs();
				return; // abort here
			}
		} else {
			if (!DataManager.validateSQLConnection()) {
				DataManager.resetDAOs();
			}
		}
		String userName = QueryHelpers.getSessionValue(request, "USER_NAME", "Stranger");
		String message = QueryHelpers.getGet(request, "message");

		templateDataMap.put("websiteTitle", DataManager.websiteTitle);
		templateDataMap.put("websiteSubtitle", DataManager.websiteSubtitle);
		templateDataMap.put("showWelcome", true);
		templateDataMap.put("userId", uid);
		templateDataMap.put("userName", userName);
		templateDataMap.put("isAdmin", isAdmin);
		templateDataMap.put("isInstructor", isInstructor);
		templateDataMap.put("isStudent", isStudent);
		templateDataMap.put("message", message);
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
		trySendResponse("");
	}

	protected void trySendResponse(String headerOverwrite) {

		if (templateName == null) {

			// Send 404 error response
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e) {
				logger.error("Unable to send 404 response code.", e);
			}
			return;

		} else {

			// Process template:
			logger.debug("Processing Template: " + templateName);
			if (headerOverwrite != "") {
				response.setHeader("Content-Type", headerOverwrite);
			}
			try (PrintWriter out = response.getWriter()) {
				Template view = freeMarkerConfig.getTemplate(templateName);
				view.process(templateDataMap, out);
			} catch (TemplateException | MalformedTemplateNameException e) {
				logger.error("Template Error: ", e);
			} catch (IOException e) {
				logger.error("IO Error: ", e);
			}

		}
		
	}

	protected void trySendJson(String json) {
		// send json:
		logger.debug("Attempting to send json...");
		try (ServletOutputStream out = response.getOutputStream()) {
			out.println(json);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

}