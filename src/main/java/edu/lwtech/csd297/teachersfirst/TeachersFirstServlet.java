package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;

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
		PageLoader.initializeDAOs();
		logger.info("Successfully initialized the DAOs!");

		logger.warn("");
		logger.warn("Servlet initialization complete!");
		logger.warn("");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();

		String logInfo = request.getRemoteAddr() + " " + request.getMethod() + " " + request.getRequestURI();
		final String sanitizedQuery = QueryHelpers.getSanitizedQueryString(request);
		logInfo += sanitizedQuery;

		// Get the cmd parameter from the URI (defaults to 'home')
		final String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		//String page = request.getParameter("page");
		// if (page == null) page = "appointments";
		//if (page != "health") logger.debug("IN - {}", logInfo); // Don't log "health" commands

		try {
			switch (pagePath) {
				case "":
				case "/":
				case "/home":
				case "/appointments":
					new AppointmentsPage(request, response).LoadPage();
					break;
				case "/make_appointment":
					new MakeAppointmentPage(request, response).LoadPage();
					break;
				case "/openings":
					new OpeningsPage(request, response).LoadPage();
					break;
				case "/services":
					new ServicesPage(request, response).LoadPage();
					break;
				case "/calendar":
					new CalendarPage(request, response).LoadPage();
					break;
				case "/profile":
					new ProfilePage(request, response).LoadPage();
					break;
				case "/members":
					new MembersPage(request, response).LoadPage();
					break;

				case "/health":
					try {
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} catch (IOException e) {
						logger.error("IO Error sending health response: ", e);
					}
					return;

				case "/test":
					new DiagnosticsPage(request, response).LoadPage();
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
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Oh no! Something went wrong. The appropriate authorities have been alerted.");
			} catch (IOException ex) {
				logger.error("Unable to send 500 response code.", ex);
			}
		}
		long time = System.currentTimeMillis() - startTime;
		logger.info("OUT- {} {}ms", logInfo, time);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	@Override
	public void destroy() {
		PageLoader.terminateDAOs();
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
