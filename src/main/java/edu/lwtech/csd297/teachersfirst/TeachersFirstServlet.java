package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.actions.*;
import edu.lwtech.csd297.teachersfirst.pages.*;

@WebServlet(name = "teachersFirst", urlPatterns = { "/" }, loadOnStartup = 0)
public class TeachersFirstServlet extends HttpServlet {

	// Declarations

	private static final long serialVersionUID = 1L; // Unused
	private static final String SERVLET_NAME = "teachersFirst";
	private static final String RESOURCES_DIR = "/WEB-INF/classes";

	// Public
	public static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		logger.warn("");
		logger.warn("===========================================================");
		logger.warn("          " + SERVLET_NAME + " init() started");
		logger.warn("               http://<team-server>");
		logger.warn("===========================================================");
		logger.warn("");

		String resourcesDir = config.getServletContext().getRealPath(RESOURCES_DIR);
		logger.info("resourcesDir = {}", resourcesDir);

		logger.info("Populating the IP whitelist...");
		Security.populateWhitelist();
		logger.info("Successfully populated the IP whitelist!");

		logger.info("Initializing FreeMarker...");
		PageLoader.initializeFreeMarker(resourcesDir);
		logger.info("Successfully initialized FreeMarker");

		logger.info("Initializing the DAOs...");
		DataManager.initializeDAOs();
		logger.info("Successfully initialized the DAOs!");

		logger.warn("");
		logger.warn("Servlet initialization complete!");
		logger.warn("");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		final String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		final String logInfo = request.getRemoteAddr() + " " + request.getMethod() + " " + pagePath + " " + sanitizedQuery;
		if (pagePath != "/health" && pagePath != "/dynamic.css") // Don't log "health" or "dynamic.css" requests
			logger.debug("IN - {}", logInfo);

		try {
			switch (pagePath) {
				case "":
				case "/":
				case "/home":
					//TODO: If logged in, redirect to appointments, otherwise, redirect to services
				case "/services":
					new ServicesPage(request, response).loadPage();
					break;
				case "/appointments":
					new AppointmentsPage(request, response).loadPage();
					break;
				case "/make_appointment":
					new MakeAppointmentPage(request, response).loadPage();
					break;
				case "/confirm_make_appointment":
					new ConfirmMakeAppointmentPage(request, response).loadPage();
					break;
				case "/openings":
					new OpeningsPage(request, response).loadPage();
					break;
				case "/new_openings":
					new NewOpeningsPage(request, response).loadPage();
					break;
				case "/profile":
					new ProfilePage(request, response).loadPage();
					break;
				case "/members":
					new MembersPage(request, response).loadPage();
					break;
				case "/register":
					new RegisterPage(request, response).loadPage();
					break;
				case "/login":
					new LoginPage(request, response).loadPage();
					break;
				case "/logout":
					new LogoutPage(request, response).loadPage();
					break;

				case "/log_in": // intentionally different - debug/json use
					new LogInAction(request, response).RunAction(); // action, not page
					return; // don't log
				case "/log_out": // intentionally different - debug/json use
					new LogOutAction(request, response).RunAction(); // action, not page
					return; // don't log

				case "/dynamic.css":
					new DynamicCssFile(request, response).loadPage();
					return; // don't log

				case "/health":
					try {
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} catch (IOException e) {
						logger.error("IO Error sending health response: ", e);
					}
					return; // don't log

				case "/test":
					new DiagnosticsPage(request, response).loadPage();
					break;

				default:
					logger.debug("====================== Debug Me ======================");
					logger.debug("Sanitized Query: {}", sanitizedQuery);
					logger.debug("Page Path: {}", pagePath);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
			}


		} catch (IOException e) {
			// Typically, this is because the connection was closed prematurely
			logger.debug("Unexpected I/O exception: ", e);
		} catch (RuntimeException e) {
			logger.error("Unexpected runtime exception: ", e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Oh no! Something went wrong. The appropriate parties have been alerted.");
			} catch (IOException ex) {
				logger.error("Unable to send 500 response code.", ex);
			}
		}
		long time = System.currentTimeMillis() - startTime;
		logger.info("OUT- {} {}ms", logInfo, time);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		final String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		final Map<String, String[]> paramMap = request.getParameterMap();
		String parameters = "";
		String comma = "";
		for (String key : paramMap.keySet()) {
			for (String value : paramMap.get(key)) {
				parameters += comma + "{" + QueryHelpers.sanitizeForLog(key) + ": " + QueryHelpers.sanitizeForLog(value) + "}";
				comma = ", ";
			}
		}		
		final String logInfo = request.getRemoteAddr() + " " + request.getMethod() + " " + pagePath + " " + parameters;
		logger.debug("IN - {}", logInfo); // Don't log "health" commands
		final String action = request.getParameter("action") == null ? "" : QueryHelpers.sanitizeForLog(request.getParameter("action"));

		try {
			switch (action) {
				case "log_in":
					new LogInAction(request, response).RunAction();
					break;
				case "log_out":
					new LogOutAction(request, response).RunAction();
					break;
				case "register_new_member":
					new NewMemberAction(request, response).RunAction();
					break;
				case "new_openings":
					new NewOpeningsAction(request, response).RunAction();
					break;
				case "make_appointment":
					new NewAppointmentAction(request, response).RunAction();
					break;
				case "delete_appointment":
					new DeleteAppointmentAction(request, response).RunAction();
					break;
				case "delete_opening":
					new DeleteOpeningAction(request, response).RunAction();
					break;

				case "reset_daos":
					String secret = QueryHelpers.getPost(request, "secret");
					if (secret.equals("makeLoveNotWar")) {
						logger.warn("======================================= Warning");
						logger.warn("| Issuing manual DAO reset command... | Warning");
						logger.warn("======================================= Warning");
						DataManager.resetDAOs();
						logger.warn("======================================= Warning");
						logger.warn("| Manual reset should have completed. | Warning");
						logger.warn("======================================= Warning");
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} else {
						logger.warn("SECURITY ALERT: Someone might be trying to damage database, password used: {}", secret);
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
					return; // different log

				default:
					logger.debug("====================== Debug Me ======================");
					logger.debug("Post Parameters: {}", parameters);
					logger.debug("Page Path: {}", pagePath);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return; // use above log instead
			}


		} catch (IOException e) {
			// Typically, this is because the connection was closed prematurely
			logger.debug("Unexpected I/O exception: ", e);
		} catch (RuntimeException e) {
			logger.error("Unexpected runtime exception: ", e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Oh no! Something went wrong. The appropriate parties have been alerted.");
			} catch (IOException ex) {
				logger.error("Unable to send 500 response code.", ex);
			}
		}
		long time = System.currentTimeMillis() - startTime;
		logger.info("OUT- {} {}ms", logInfo, time);
	}

	@Override
	public void destroy() {
		DataManager.terminateDAOs();
		logger.warn("-----------------------------------------");
		logger.warn("  " + SERVLET_NAME + " destroy() completed!");
		logger.warn("-----------------------------------------");
		logger.warn(" ");
	}

	@Override
	public String getServletInfo() {
		return "teachersFirst Servlet";
	}

	// =================================================================

	private static int parseInt(String s) {
		int i = -1;
		if (s != null) {
			try {
				i = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				i = -2;
			}
		}
		return i;
	}

}
