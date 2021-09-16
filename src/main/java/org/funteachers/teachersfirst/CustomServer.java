package org.funteachers.teachersfirst;

import java.io.*;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class CustomServer {

	// Declarations
	
	final protected static Logger logger = LogManager.getLogger();
	final private static String customServeBasePath = "/var/www/";

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

	protected String templateName = null;
	protected String errorMessage = "";

	// Constructors
	protected CustomServer(ConnectionPackage cp) {
		//if (!cp.validate()) cp.reset(); // Try to validate-reset SQL connection

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

	}

	public void serve(String pagePath, String sanitizedQuery, ServletContext servletContext) throws IOException {

		// Check to see if file exists
		String filename = pagePath;
		File file = new File(customServeBasePath, filename);
		if (!file.exists() || file.isDirectory()) {
			logger.debug("====================== CUSTOM FILE NOT FOUND ======================");
			logger.debug("Is Directory:     {}", file.isDirectory());
			logger.debug("Sanitized Query:  {}", sanitizedQuery);
			logger.debug("Page Path:        {}", pagePath);
			logger.debug("Search Path:      {}", customServeBasePath);
			logger.debug("Working Dir:      {}", System.getProperty("user.dir"));
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return; // Use above logging instead of standard logging
		}

		// Check to see if it's a profile image
		final String regex = "^/custom/profiles/u([0-9]+)/.*$";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(pagePath);
		logger.debug("Matcher matches: {}", matcher.matches());
		logger.debug("Matcher groups: {}", matcher.groupCount());
		logger.debug("Matcher group {}: {}", 0, matcher.group(0));
		logger.debug("Matcher group {}: {}", 1, matcher.group(1));
		if (matcher.matches()) {
			int otherId = Integer.parseInt(matcher.group(1));
			Member other = this.connectionPackage.getMemberDAO().retrieveByID(otherId);
			if (!Permissions.MemberCanSeeMember(this.operator, other)) {
				// Send fake file not found when not authorized to view profile pic
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Send file -- set headers and send raw data
		this.response.setHeader("Content-Type", servletContext.getMimeType(filename));
		this.response.setHeader("Content-Length", String.valueOf(file.length()));
		this.response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");						
		// Cache for 30 days
		this.response.setDateHeader("Expires", System.currentTimeMillis() + 30 * DateHelpers.millisecondsPerDay);
		Files.copy(file.toPath(), this.response.getOutputStream());
	}

}
