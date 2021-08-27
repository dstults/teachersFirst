package org.funteachers.teachersfirst;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.actions.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.funteachers.teachersfirst.pages.*;
//import org.mariadb.jdbc.internal.util.ConnectionState;

@WebServlet(name = "teachersFirst", urlPatterns = { "/" }, loadOnStartup = 0)
public class ServerMain extends HttpServlet {

	// Declarations

	private static final long serialVersionUID = 1L; // Unused
	private static final String SERVLET_NAME = "teachersFirst";
	private static final String RESOURCES_DIR = "/WEB-INF/classes";

	// Public
	public static final Logger logger = LogManager.getLogger(ServerMain.class);

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
		SecurityChecker.populateWhitelist();
		logger.info("Successfully populated the IP whitelist!");

		logger.info("Initializing FreeMarker...");
		PageLoader.initializeFreeMarker(resourcesDir);
		logger.info("Successfully initialized FreeMarker");

		logger.info("Initializing site data...");
		DataManager.initializeSiteData();
		logger.info("Successfully initialized site data!");

		logger.info("Testing database connectivity...");
		ConnectionPackage connectionPackage = new ConnectionPackage(null, null);
		LoggedEvent.log(connectionPackage, 0, "SERVER INIT");
		connectionPackage.terminate();
		logger.info("All tests passed!");


		logger.warn("");
		logger.warn("Servlet initialization complete!");
		logger.warn("");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		final String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		final ConnectionPackage connectionPackage = new ConnectionPackage(request, response);
		final String logInfo = connectionPackage.getSecurity().getRealIp() + " " + request.getMethod() + " " + pagePath + " " + sanitizedQuery;
		if (pagePath != "/health" && pagePath != "/dynamic.css") // Don't log "health" or "dynamic.css" requests
			logger.debug("IN - {}", logInfo);

		try {
			switch (pagePath) {
				case "":
				case "/":
				case "/services":
					new ServicesPage(connectionPackage).loadPage();
					break;
				case "/msg":
				case "/message":
				case "/messageOnly":
					new MessagePage(connectionPackage).loadPage();
					break;
				case "/appointments":
					new AppointmentsPage(connectionPackage).loadPage();
					break;
				case "/make_appointment_batch":
					new MakeAppointmentBatchPage(connectionPackage).loadPage();
					break;
				case "/make_appointment":
					new MakeAppointmentPage(connectionPackage).loadPage();
					break;
				case "/confirm_make_appointment":
					new ConfirmMakeAppointmentPage(connectionPackage).loadPage();
					break;
				case "/openings":
					new OpeningsPage(connectionPackage).loadPage();
					break;
				case "/make_openings":
					new MakeOpeningsPage(connectionPackage).loadPage();
					break;
				case "/profile":
					new ProfilePage(connectionPackage).loadPage();
					break;
				case "/members":
					new MembersPage(connectionPackage).loadPage();
					break;
				case "/register":
					new RegisterPage(connectionPackage).loadPage();
					break;
				case "/login":
					new LoginPage(connectionPackage).loadPage();
					break;
				case "/logout":
					new LogoutPage(connectionPackage).loadPage();
					break;

				case "/log_in": // intentionally different - debug/json use
					new LogInAction(connectionPackage).runAction(); // action, not page
					connectionPackage.terminate();
					return; // don't log
				case "/log_out": // intentionally different - debug/json use
					new LogOutAction(connectionPackage).runAction(); // action, not page
					connectionPackage.terminate();
					return; // don't log

				case "/dynamic.css":
					new DynamicCssFile(connectionPackage).loadPage();
					connectionPackage.terminate();
					return; // don't log

				case "/health":
					try {
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} catch (IOException e) {
						logger.error("IO Error sending health response: ", e);
					}
					connectionPackage.terminate();
					return; // don't log

				case "/test":
					new DiagnosticsPage(connectionPackage).loadPage();
					break;

				default:
					//String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
					if (isValidCustomPath(pagePath)) {
						String filename = pagePath;
						File file = new File("/var/www/", filename);
						if (!file.exists() || file.isDirectory()) {
							logger.debug("====================== CUSTOM FAIL ======================");
							logger.debug("Is Directory:     {}", file.isDirectory());
							logger.debug("Sanitized Query:  {}", sanitizedQuery);
							logger.debug("Page Path:        {}", pagePath);
							response.sendError(HttpServletResponse.SC_NOT_FOUND);
							return; // Use above logging instead of standard logging
						}
						response.setHeader("Content-Type", getServletContext().getMimeType(filename));
						response.setHeader("Content-Length", String.valueOf(file.length()));
						response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");						
						// Cache for 30 days
						response.setDateHeader("Expires", System.currentTimeMillis() + 30 * DateHelpers.millisecondsPerDay);
						Files.copy(file.toPath(), response.getOutputStream());
						break; // Use standard logging
					}

					logger.debug("====================== Debug Me ======================");
					logger.debug("Sanitized Query:  {}", sanitizedQuery);
					logger.debug("Page Path:        {}", pagePath);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					connectionPackage.terminate();
					return; // Use above logging instead of standard logging
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
		connectionPackage.terminate();
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
		final ConnectionPackage connectionPackage = new ConnectionPackage(request, response);
		final String logInfo = connectionPackage.getSecurity().getRealIp() + " " + request.getMethod() + " " + pagePath + " " + parameters;
		logger.debug("IN - {}", logInfo); // Don't log "health" commands
		final String action = request.getParameter("action") == null ? "" : QueryHelpers.sanitizeForLog(request.getParameter("action"));

		try {
			switch (action) {
				case "log_in":
					new LogInAction(connectionPackage).runAction();
					break;
				case "log_out":
					new LogOutAction(connectionPackage).runAction();
					break;
				case "register_member":
					new OpenRegisterAction(connectionPackage).runAction();
					break;
				case "update_member":
					new UpdateMemberAction(connectionPackage).runAction();
					break;
				case "make_openings":
					new NewOpeningsAction(connectionPackage).runAction();
					break;
				case "make_appointment":
					new NewAppointmentAction(connectionPackage).runAction();
					break;
				case "make_appointment_batch":
					new NewAppointmentBatchAction(connectionPackage).runAction();
					break;
				case "delete_appointment":
					new DeleteAppointmentAction(connectionPackage).runAction();
					break;
				case "refund_appointment":
					new UpdateAppointmentStateAction(connectionPackage, Appointment.STATE_MISSED_REFUNDED).runAction();
					break;
				case "miss_appointment":
					new UpdateAppointmentStateAction(connectionPackage, Appointment.STATE_MISSED).runAction();
					break;
				case "complete_appointment":
					new UpdateAppointmentStateAction(connectionPackage, Appointment.STATE_COMPLETED).runAction();
					break;
				case "cancel_appointment":
					new UpdateAppointmentStateAction(connectionPackage, Appointment.STATE_CANCELLED).runAction();
					break;
				case "delete_opening":
					new DeleteOpeningAction(connectionPackage).runAction();
					break;

				case "reset_daos":
					String secret = QueryHelpers.getPost(request, "secret");
					if (secret.equals("makeLoveNotWar")) {
						logger.warn("======================================= Warning");
						logger.warn("| Issuing manual connection reset ... | Warning");
						logger.warn("======================================= Warning");
						connectionPackage.reset();
						logger.warn("======================================= Warning");
						logger.warn("| Manual reset should have completed. | Warning");
						logger.warn("======================================= Warning");
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} else {
						logger.warn("SECURITY ALERT: Someone might be trying to damage database, password used: {}", secret);
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
					connectionPackage.terminate();
					return; // different log

				default:
					logger.debug("====================== Debug Me ======================");
					logger.debug("Post Parameters:  {}", parameters);
					logger.debug("Page Path:        {}", pagePath);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					connectionPackage.terminate();
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
		connectionPackage.terminate();
	}

	@Override
	public void destroy() {
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

	private boolean isValidCustomPath(String pagePath) {
		// test for min length
		if (pagePath.length() < 8) return false;
		
		// check for a "custom/"
		if (!pagePath.substring(0, 8).equals("/custom/")) return false;

		// check for illegal chars
		final String regex = "[^-_.A-Za-z0-9\\/]";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(pagePath);
		if (matcher.matches()) return false;

		// check for any double dots
		if (pagePath.lastIndexOf("..") != -1) return false;
		
		return true;
	}
	

	/*
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
	*/

}
