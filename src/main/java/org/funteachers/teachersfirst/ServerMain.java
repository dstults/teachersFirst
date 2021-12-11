package org.funteachers.teachersfirst;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.actions.*;
import org.funteachers.teachersfirst.actions.admin.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.funteachers.teachersfirst.pages.*;
//import org.mariadb.jdbc.internal.util.ConnectionState;
//import org.mariadb.jdbc.Driver;

@WebServlet(name = "teachersFirst", urlPatterns = { "/" }, loadOnStartup = 0)
public class ServerMain extends HttpServlet {

	// Declarations

	private static final long serialVersionUID = 1L; // Unused
	private static final String SERVLET_NAME = "teachersFirst";
	private static final String RESOURCES_DIR = "/WEB-INF/classes";

	// Public
	public static Logger logger; // = LogManager.getLogger();

	public ServerMain() {

		// Set up logger
		String path = "/usr/share/tomcat9/logs";
		if (!Files.exists(Paths.get(path))) path = "/var/log/tomcat9/";
		System.setProperty("logfile.name", path);
		ServerMain.logger = LogManager.getLogger();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		logger.warn("");
		logger.warn("==================================================");
		logger.warn("    Server init() started!");
		logger.warn("    SERVLET: " + SERVLET_NAME);
		logger.warn("==================================================");
		logger.warn("");

		getServletContext();
		String resourcesDir = config.getServletContext().getRealPath(RESOURCES_DIR);
		logger.info("res.dir = {}", StringTools.left(resourcesDir, 40));

		SecurityChecker.populateWhitelist();
		logger.info("IP whitelist populated.");

		FreeMarkerSetup.initializeFreeMarker(resourcesDir);
		logger.info("FreeMarker initialized.");

		GlobalConfig.initializeSiteData();
		logger.info("Site data initialized.");

		ConnectionPackage connectionPackage = new ConnectionPackage(null, null);
		LoggedEvent.log(connectionPackage, 0, "SERVER INIT");
		connectionPackage.terminate();
		logger.info("Database connectivity tests complete.");

		logger.info("");
		logger.info("--------------------------------------------------");
		logger.info("    Servlet initialization complete!");
		logger.info("--------------------------------------------------");
		logger.info("");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		final String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		final ConnectionPackage connectionPackage = new ConnectionPackage(request, response);
		final String logInfo = connectionPackage.getSecurity().getRealIp() + " " + request.getMethod() + " " + pagePath + " " + sanitizedQuery;
		if (pagePath != "/health" && pagePath != "/dynamic.css" && pagePath != "/dynamic.js") // Don't log "health" or "dynamic.css" requests
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
				case "/conflicts":
					new AppointmentConflicts(connectionPackage).loadPage();
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
					new RegisterPage(connectionPackage, true).loadPage();
					break;
				case "/create_user":
					new RegisterPage(connectionPackage, false).loadPage();
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

				case "/dynamic.js":
					new DynamicJsFile(connectionPackage).loadPage();
					connectionPackage.terminate();
					return; // don't log

				case "/health":
					new HealthPage(connectionPackage).loadPage();
					connectionPackage.terminate();
					return; // don't log

				case "/test1":
					new TestPage1(connectionPackage).loadPage();
					break;
				case "/test2":
					new TestPage2(connectionPackage).loadPage();
					break;

				default:
					//String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
					if (isValidCustomPath(pagePath)) {
						new CustomServer(connectionPackage).serve(pagePath, sanitizedQuery, this.getServletContext());
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
					new AddMemberAction(connectionPackage).runAction();
					break;
				case "update_member":
					new UpdateMemberAction(connectionPackage).runAction();
					break;
				case "delete_member":
					new DeleteMemberAction(connectionPackage).runAction();
					break;
				case "undelete_member":
					new UndeleteMemberAction(connectionPackage).runAction();
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
		
		logger.warn("");
		logger.warn("==================================================");
		logger.warn("    Server destroy() started!");
		logger.warn("==================================================");
		logger.warn("");

		deregisterJdbcDrivers();
		
		logger.info("");
		logger.info("--------------------------------------------------");
		logger.info("    Server destruction complete!");
		logger.info("--------------------------------------------------");
		logger.info("");

	}

	@Override
	public String getServletInfo() {
		return "teachersFirst Servlet";
	}

	// =================================================================

	private void deregisterJdbcDrivers() {
		logger.info("Deregistering JDBC drivers...");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		String result = "";
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() == cl) {
				try {
					DriverManager.deregisterDriver(driver);
					result = "deregistered";
				} catch (SQLException ex) {
					result = "error";
				}
			} else {
				//logger.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
				result = "ignored";
			}
			logger.debug("...{}...{}!", driver, result);
		}
	}

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
	
}
