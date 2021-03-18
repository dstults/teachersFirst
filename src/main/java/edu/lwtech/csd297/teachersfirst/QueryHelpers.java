package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.net.*;

import javax.servlet.http.*;

public class QueryHelpers {
	
	public static String getSanitizedFullQueryString(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString == null)
			return "";

		try {
			queryString = URLDecoder.decode(queryString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Should never happen
			throw new IllegalStateException(e);
		}
		queryString = sanitizeForLog(queryString);
		return queryString;
	}

	public static String sanitizeForLog(String s) {
		return s.replaceAll("[\n|\t]", "_").trim();
	}

	public static String sanitizeForWeb(String s) {
		if (s == null) return "";
		try {
			s = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		// This might be redundant with URLDecoder, but just to be safe...
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("&", "&amp;");
		return s.trim();
	}

	public static String getGet(HttpServletRequest request, String keyName) {
		return getGet(request, keyName, "");
	}

	public static String getPost(HttpServletRequest request, String keyName) {
		return getPost(request, keyName, "");
	}

	public static String getPost(HttpServletRequest request, String keyName, String defaultValue) {
		return getGet(request, keyName, defaultValue);
	}

	public static boolean getGetBool(HttpServletRequest request, String keyName) {
		if (request.getParameter(keyName) == null) return false; // /appointments => should return false
		if (request.getParameter(keyName).isEmpty()) return true; // /appointments?json => should return true
		if (request.getParameter(keyName).toString().equals("0")) return false; // /appointments?json=0 => should return false
		if (request.getParameter(keyName).toString().toLowerCase().equals("false")) return false; // /appointments?json=FALSE => should return false
		return true; // anything else should return true
	}

	public static String getGet(HttpServletRequest request, String keyName, String defaultValue) {
		if (request.getParameter(keyName) == null) return defaultValue;
		if (request.getParameter(keyName).isEmpty()) return defaultValue;
		return sanitizeForWeb(request.getParameter(keyName));
	}

	public static String getSessionValue(HttpServletRequest request, String sessionArg) {
		return getSessionValue(request, sessionArg, "");
	}

	public static String getSessionValue(HttpServletRequest request, String sessionArg, String defaultValue) {
		if (request.getSession().getAttribute(sessionArg) == null) return defaultValue;
		if (request.getSession().getAttribute(sessionArg).toString() == null) return defaultValue;
		if (request.getSession().getAttribute(sessionArg).toString().isEmpty()) return defaultValue;
		return request.getSession().getAttribute(sessionArg).toString();
	}

}
