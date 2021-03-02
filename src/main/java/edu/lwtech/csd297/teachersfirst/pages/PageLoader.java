package edu.lwtech.csd297.teachersfirst.pages;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;

import freemarker.template.*;
import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public abstract class PageLoader {

	// Declarations

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	protected String templateName = null;
	protected Map<String, Object> templateDataMap = new HashMap<>();

	// Static Declarations (shared variables to handle freemarker and DAOs)

	protected static final Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());
	protected static DAO<Member> memberDAO = null;
	protected static final List<DAO<?>> allDAOs = new ArrayList<>();

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

	public static void initializeDAOs() throws ServletException {
		memberDAO = new MemberMemoryDAO();
		allDAOs.add(memberDAO);
		if (!memberDAO.initialize(""))
			throw new UnavailableException("Unable to initialize the memberDAO.");
	}

	public static void terminateDAOs() {
		// memberDAO.terminate();
		for (DAO<?> iDAO : allDAOs) {
			iDAO.terminate();
		}
	}

	// Constructors

	PageLoader(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	// Public Abstracts (entry point into child objects)

	public abstract void LoadPage();

	// Protected Methods (shared magic between all pages)

	protected void SendFake404(String description) {
		logger.debug("====================== SECURITY ALERT ======================");
		logger.debug("Description: {}", description);
		final String sanitizedQuery = QueryHelpers.getSanitizedQueryString(request);
		logger.debug("Sanitized Query: {}", sanitizedQuery);
		final String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
		logger.debug("Page Path: {}", pathInfo);
		try {
			this.response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			logger.error("Unable to send fake 404 response code.", e);
		}
	}

	protected void TrySendResponse() {

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

}