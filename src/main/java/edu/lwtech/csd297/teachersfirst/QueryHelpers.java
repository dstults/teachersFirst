package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.net.*;

import javax.servlet.http.*;

public class QueryHelpers {
	
	public static String getSanitizedQueryString(HttpServletRequest request) {
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

	private static String sanitizedString(String s) {
		return s.replaceAll("[\n|\t]", "_");
	}

}
