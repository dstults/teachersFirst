package org.funteachers.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class TestPage1 extends PageLoader {

	// Constructor
	public TestPage1(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		
		// ========================= Diagnostic security checks

		// Get initial bit to verify ID and operation
		final String clientIp = security.getRealIp();

		// Check if whitelisted
		if (!SecurityChecker.isWhitelisted(clientIp)) {
			sendFake404("Unauthorized user [ " + clientIp + "] attempted to access diagnostics page.");
			return;
		}

		// ========================= Place content below this line

		// Test out custom cookies
		/*
		Cookie testCookie1 = new Cookie("token", "1.1234asdf");
		testCookie1.setMaxAge(60 * 60 * 24 * 90); // 60s * 60m * 24h * 90d = 90-day cookie
		//testCookie1.setPath("/"); // / == default
		testCookie1.setSecure(true);
		testCookie1.setHttpOnly(true);
		response.addCookie(testCookie1);
		*/

		/*
		Cookie testCookie2 = new Cookie("token", "lololwhat");
		//testCookie2.setDomain("dstults.net"); // Setting to anything else will fail for modern browsers
		testCookie2.setMaxAge(60 * 60); // 60s * 60m = 1-hour cookie
		testCookie2.setPath("/test"); // this page only
		//testCookie2.setSecure(false);
		//testCookie2.setHttpOnly(false);
		testCookie2.setComment("All your base are belong to us.");
		response.addCookie(testCookie2);
		*/
		
		/*
		// Same token and path => Overwrites
		Cookie testCookie3 = new Cookie("token", "owned");
		testCookie3.setMaxAge(60 * 60 * 24 * 90); // 60s * 60m * 24h * 90d = 90-day cookie
		response.addCookie(testCookie3);
		*/

		// Get needed information dump data
		final String clientHost = request.getRemoteHost() == clientIp ? "same as IP or resolution disabled" : request.getRemoteHost();
		// Note #1: To enable getRemoteHost reverse lookup (slower!) you need to add "enablelookups=true" to
		//     the <Connector ... /> tag in server.xml.
		// Note #2: When behind a proxy (load balancer), Client IP will be the source IP, while client Host
		//      will actually show you the closest proxy's IP address.
		final String httpType = request.isSecure() ? "HTTPS" : "_http_";
		final String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
		final String uriPath = request.getRequestURI() == null ? "" : request.getRequestURI();
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		final Map<String, String[]> paramMap = request.getParameterMap();
		final Map<String, String[]> headerItems = dumpHeaderToMap(request);
		final List<Map<String, String>> cookieItems = dumpCookiesToMap(request);

		final DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
		final String memberDaoCheckNull = memberDAO != null ? "Member DAO Found" : "NULL MEMBER DAO";
		final Member member = memberDAO != null ? memberDAO.retrieveByIndex(0) : null;
		final String memberDaoCheckGet = member != null ? "Member Item Found" : "NO MEMBER ITEM COULD BE RETRIEVED";

		final DAO<Opening> openingDAO = this.connectionPackage.getOpeningDAO(this.getClass().getSimpleName());
		final String openingDaoCheckNull = openingDAO != null ? "Opening DAO Found" : "NULL OPENING DAO";
		final Opening opening = openingDAO != null ? openingDAO.retrieveByIndex(0) : null;
		final String openingDaoCheckGet = opening != null ? "Opening Item Found" : "NO OPENING ITEM COULD BE RETRIEVED";

		final DAO<Appointment> appointmentDAO = this.connectionPackage.getAppointmentDAO(this.getClass().getSimpleName());
		final String appointmentDaoCheckNull = appointmentDAO != null ? "Appointment DAO Found" : "NULL APPOINTMENT DAO";
		final Appointment appointment = appointmentDAO != null ? appointmentDAO.retrieveByIndex(0) : null;
		final String appointmentDaoCheckGet = appointment != null ? "Appointment Item Found" : "NO APPOINTMENT ITEM COULD BE RETRIEVED";

		// FreeMarker
		templateName = "test1.ftl";
		templateDataMap.put("clientIp", clientIp);
		templateDataMap.put("clientHost", clientHost);
		templateDataMap.put("httpType", httpType);
		templateDataMap.put("pathInfo", pathInfo);
		templateDataMap.put("uriPath", uriPath);
		templateDataMap.put("fullQuery", sanitizedQuery);
		templateDataMap.put("paramMap", paramMap);
		templateDataMap.put("headerItems", headerItems);
		templateDataMap.put("cookieItems", cookieItems);
		templateDataMap.put("memberDaoCheckNull", memberDaoCheckNull);
		templateDataMap.put("memberDaoCheckGet", memberDaoCheckGet);
		templateDataMap.put("openingDaoCheckNull", openingDaoCheckNull);
		templateDataMap.put("openingDaoCheckGet", openingDaoCheckGet);
		templateDataMap.put("appointmentDaoCheckNull", appointmentDaoCheckNull);
		templateDataMap.put("appointmentDaoCheckGet", appointmentDaoCheckGet);

		// Go
		trySendResponse();
	}

	private Map<String, String[]> dumpHeaderToMap(HttpServletRequest request) {
		final Enumeration<String> allHeaderNames = request.getHeaderNames();

		Map<String, String[]> result = new LinkedHashMap<>(); // Needs linked hashmap to retain insertion order
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

	private String valueOrNotAvailable(String input) {
		return input != null ? input : "n/a";
	}

	private List<Map<String, String>> dumpCookiesToMap(HttpServletRequest request) {
		final Cookie[] allCookies = request.getCookies();

		List<Map<String, String>> results = new LinkedList<>();
		for(Cookie cookie : allCookies) {
			Map<String, String> nextCookie = new LinkedHashMap<>(); // Needs linked hashmap to retain insertion order
			nextCookie.put("Name", valueOrNotAvailable(cookie.getName()));
			nextCookie.put("Domain", valueOrNotAvailable(cookie.getDomain()));
			nextCookie.put("Path", valueOrNotAvailable(cookie.getPath()));
			nextCookie.put("Value", valueOrNotAvailable(cookie.getValue()));
			nextCookie.put("Max Age", String.valueOf(cookie.getMaxAge()));  		// gets stripped on way back
			nextCookie.put("Version", String.valueOf(cookie.getVersion()));
			nextCookie.put("Secure", String.valueOf(cookie.getSecure()));			// gets stripped on way back
			nextCookie.put("HTTP-Only", String.valueOf(cookie.isHttpOnly()));		// gets stripped on way back
			nextCookie.put("Comment", valueOrNotAvailable(cookie.getComment()));
			results.add(nextCookie);
		}
		return results;
	}

}