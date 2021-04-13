package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DiagnosticsPage extends PageLoader {

	// Constructor
	public DiagnosticsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		
		// Get initial bit to verify ID and operation
		final String clientIp = request.getRemoteAddr();

		// Check if whitelisted
		if (!Security.isWhitelisted(clientIp)) {
			sendFake404("Unauthorized user attempted to access diagnostics page.");
			return;
		}

		// Get needed information dump data
		final String clientHost = request.getRemoteHost() == clientIp ? "same as IP or resolution disabled" : request.getRemoteHost();
		final String httpType = request.isSecure() ? "HTTPS" : "_http_";
		final String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
		final String uriPath = request.getRequestURI() == null ? "" : request.getRequestURI();
		final String sanitizedQuery = QueryHelpers.getSanitizedFullQueryString(request);
		final Map<String, String[]> paramMap = request.getParameterMap();
		final Map<String, String[]> headerItems = dumpHeaderToMap(request);

		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		final String memberDaoCheckNull = memberDAO != null ? "Member DAO Found" : "NULL MEMBER DAO";
		final Member member = memberDAO != null ? memberDAO.retrieveByIndex(0) : null;
		final String memberDaoCheckGet = member != null ? "Member Item Found" : "NO MEMBER ITEM COULD BE RETRIEVED";

		final DAO<Opening> openingDAO = DataManager.getOpeningDAO();
		final String openingDaoCheckNull = openingDAO != null ? "Opening DAO Found" : "NULL OPENING DAO";
		final Opening opening = openingDAO != null ? openingDAO.retrieveByIndex(0) : null;
		final String openingDaoCheckGet = opening != null ? "Opening Item Found" : "NO OPENING ITEM COULD BE RETRIEVED";

		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		final String appointmentDaoCheckNull = appointmentDAO != null ? "Appointment DAO Found" : "NULL APPOINTMENT DAO";
		final Appointment appointment = appointmentDAO != null ? appointmentDAO.retrieveByIndex(0) : null;
		final String appointmentDaoCheckGet = appointment != null ? "Appointment Item Found" : "NO APPOINTMENT ITEM COULD BE RETRIEVED";

		// FreeMarker
		templateName = "diagnostics.ftl";
		templateDataMap.put("clientIp", clientIp);
		templateDataMap.put("clientHost", clientHost);
		templateDataMap.put("httpType", httpType);
		templateDataMap.put("pathInfo", pathInfo);
		templateDataMap.put("uriPath", uriPath);
		templateDataMap.put("fullQuery", sanitizedQuery);
		templateDataMap.put("paramMap", paramMap);
		templateDataMap.put("headerItems", headerItems);
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