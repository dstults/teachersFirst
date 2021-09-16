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
	final private static String internalDefaultProfilePath = System.getProperty("user.dir") + "/webapps/ROOT/images/profileNeutral.png";
	final private static String customDefaultProfilePath = customServeBasePath + "images/profileNeutral.png";

	final protected ConnectionPackage connectionPackage;
	final protected HttpServletRequest request;
	final protected HttpServletResponse response;
	final protected SecurityChecker security;

	final protected Member operator;

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
	}

	public void serve(String pagePath, String sanitizedQuery, ServletContext servletContext) throws IOException {

		// Check to see if it's a profile image
		final String regex = "^/custom/profiles/u([0-9]+).*$";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(pagePath);
		final boolean isProfilePic = matcher.matches();
		boolean showRealProfilePic = false;
		if (isProfilePic) {
			int otherId = Integer.parseInt(matcher.group(1));
			Member other = this.connectionPackage.getMemberDAO(this.getClass().toString()).retrieveByID(otherId);
			if (Permissions.MemberCanSeeMember(this.operator, other)) {
				showRealProfilePic = true;
			}
		}
		
		// Check to see if file exists
		String filename = pagePath;
		File file;
		if (isProfilePic && showRealProfilePic) {
			// Find the right profile pic file type by thrashing around
			final String customProfileBasePath = "/custom/profiles/u" + matcher.group(1) + "/profile.";
			file = new File(customServeBasePath, customProfileBasePath + "png");
			if (!file.exists()) file = new File(customServeBasePath, customProfileBasePath + "gif");
			if (!file.exists()) file = new File(customServeBasePath, customProfileBasePath + "jpg");
			if (!file.exists()) file = new File(customDefaultProfilePath);
			if (!file.exists()) file = new File(internalDefaultProfilePath);
		} else if (isProfilePic && !showRealProfilePic) {
			file = new File(customDefaultProfilePath);
			if (!file.exists()) file = new File(internalDefaultProfilePath);
		} else {
			file = new File(customServeBasePath, filename);
		}
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

		// Send file -- set headers and send raw data
		this.response.setHeader("Content-Type", servletContext.getMimeType(filename));
		this.response.setHeader("Content-Length", String.valueOf(file.length()));
		this.response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");						
		// Cache for 30 days
		this.response.setDateHeader("Expires", System.currentTimeMillis() + 30 * DateHelpers.millisecondsPerDay);
		Files.copy(file.toPath(), this.response.getOutputStream());
	}

}
