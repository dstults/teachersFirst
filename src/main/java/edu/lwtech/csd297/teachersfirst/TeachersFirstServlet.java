package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;
import freemarker.template.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

@WebServlet(name = "teachersFirst", urlPatterns = { "/" }, loadOnStartup = 0)
public class TeachersFirstServlet extends HttpServlet {

	private static final long serialVersionUID = 1L; // Unused
	private static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	private static final String SERVLET_NAME = "teachersFirst";
	private static final String RESOURCES_DIR = "/WEB-INF/classes";
	private static final Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());

	private DAO<Member> memberDAO = null;

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

		logger.info("Initializing FreeMarker...");
		String templateDir = resourcesDir + "/templates";
		try {
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			String msg = "Template directory not found: " + templateDir;
			logger.fatal(msg, e);
			throw new UnavailableException(msg);
		}
		logger.info("Successfully initialized FreeMarker");

		logger.info("Initializing the DAOs...");
		memberDAO = new MemberMemoryDAO();

		if (!memberDAO.initialize(""))
			throw new UnavailableException("Unable to initialize the TeachersFirstDAO.");
		logger.info("Successfully initialized the DAOs!");

		logger.warn("");
		logger.warn("Servlet initialization complete!");
		logger.warn("");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();

		String logInfo = request.getRemoteAddr() + " " + request.getMethod() + " " + request.getRequestURI();
		final String sanitizedQuery = getSanitizedQueryString(request);
		logInfo += sanitizedQuery;

		// Get the cmd parameter from the URI (defaults to 'home')
		String pagePath = request.getPathInfo() == null ? "" : request.getPathInfo();
		String page = request.getParameter("page");
		// if (page == null) page = "appointments";
		if (page != "health")
			logger.debug("IN - {}", logInfo); // Don't log "health" commands

		try {
			// Initialize the variables that control Freemarker's output
			// These should be changed to appropriate values inside of the corresponding
			// case statement below
			String templateName = null;
			Map<String, Object> templateDataMap = new HashMap<>();
			List<Member> members = memberDAO.retrieveAll();
			templateDataMap.put("members", members);

			// Process the GET command
			switch (pagePath) {
				case "/home":
				case "/appointments":
					templateName = "appointments.ftl";
					break;
				case "/messages":
					templateName = "messages.ftl";
					break;
				case "/services":
					templateName = "services.ftl";
					break;
				case "/calendar":
					templateName = "calendar.ftl";
					break;
				case "/members":
				case "/profile":
					templateName = "members.ftl";
					break;

				case "health":
					try {
						response.sendError(HttpServletResponse.SC_OK, "OK");
					} catch (IOException e) {
						logger.error("IO Error sending health response: ", e);
					}
					return;

				case "test":
					final String clientIp = request.getRemoteAddr();
					templateDataMap.put("clientIp", clientIp);
					final String clientHost = request.getRemoteHost() == clientIp ? "same as IP or resolution disabled"
							: request.getRemoteHost();
					templateDataMap.put("clientHost", clientHost);
					final String httpType = request.isSecure() ? "HTTPS" : "_http_";
					templateDataMap.put("httpType", httpType);
					final String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
					templateDataMap.put("pathInfo", pathInfo);
					final String uriPath = request.getRequestURI() == null ? "" : request.getRequestURI();
					templateDataMap.put("uriPath", uriPath);
					templateDataMap.put("fullQuery", sanitizedQuery);
					final Map<String, String[]> paramMap = request.getParameterMap();
					templateDataMap.put("paramMap", paramMap);
					final Map<String, String[]> headerItems = dumpHeaderToMap(request);
					templateDataMap.put("headerItems", headerItems);
					templateName = "test.ftl";
					break;

				default:
					logger.info("Unknown GET command received: {}", sanitizedQuery);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
			}

			if (templateName == null) {
				// Send 404 error response
				try {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} catch (IOException e) {
					logger.error("Unable to send 404 response code.", e);
				}
				return;
			}

			// Have Freemarker merge the template and seond out the result
			processTemplate(response, templateName, templateDataMap);

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
		memberDAO.terminate();
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

	private static void processTemplate(HttpServletResponse response, String template, Map<String, Object> model) {
		logger.debug("Processing Template: " + template);

		try (PrintWriter out = response.getWriter()) {
			Template view = freeMarkerConfig.getTemplate(template);
			view.process(model, out);
		} catch (TemplateException | MalformedTemplateNameException e) {
			logger.error("Template Error: ", e);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

	private String getSanitizedQueryString(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString == null)
			return "";

		try {
			queryString = URLDecoder.decode(queryString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Should never happen
			throw new IllegalStateException(e);
		}
		queryString = sanitizedString(queryString);
		return queryString;
	}

	private String sanitizedString(String s) {
		return s.replaceAll("[\n|\t]", "_");
	}

	private Map<String, String[]> dumpHeaderToMap(HttpServletRequest request) {
		final Enumeration<String> allHeaderNames = request.getHeaderNames();

		Map<String, String[]> result = new HashMap<>();
		while (allHeaderNames.hasMoreElements()) {
			String next = allHeaderNames.nextElement();
			Enumeration<String> values = request.getHeaders(next);
			List<String> t = new ArrayList<>(); // should only ever have one value each but just in case
			while (values.hasMoreElements()) {
				t.add(values.nextElement());
			}
			result.put(next, t.toArray(new String[0]));
		}
		return result;
	}
}
