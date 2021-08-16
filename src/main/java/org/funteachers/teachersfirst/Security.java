package org.funteachers.teachersfirst;

import java.net.*;
import java.util.*;
import java.util.regex.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.daos.sql.MemberSqlDAO;
import org.funteachers.teachersfirst.obj.*;

public class Security {
	
	private static final Logger logger = LogManager.getLogger(ServerMain.class);
	private static final List<String> ipWhitelist = new ArrayList<String>();

	public static void populateWhitelist() {
		// Manual entries:		
		whitelistIp("192.168.1.129");

		// Automatic entries:		
		// WARNING: GitHub build fails this test, so cannot use:
		//whitelistIp(nsLookup("dstults.net"));
	}

	public static String getRealIp(HttpServletRequest request) {
		String proxyResult = request.getHeader("X-FORWARDED-FOR");
		if (proxyResult != null) return proxyResult;

		String result = request.getRemoteAddr();
		return result;
	}

	private static void whitelistIp(String ip) {
		if (ip == null) {
			logger.warn("Invalid argument: ip is null");
			return;
		}
		if (ip.isEmpty()) {
			throw new IllegalArgumentException("Invalid argument: ip is empty");
		}

		ipWhitelist.add(ip);
		logger.info("Added IP [ " + ip + " ] to whitelist.");
	}

	public static boolean isWhitelisted(String ip) {
		return ipWhitelist.contains(ip);
	}

	public static String nsLookup(String domain) {
		try {
			InetAddress inetHost = InetAddress.getByName(domain);
			logger.debug("Resolving [" + domain + "] ... IP is [" + inetHost.getHostAddress() + "]");
			return inetHost.getHostAddress();
		} catch(UnknownHostException ex) {
			logger.warn("Resolving [" + domain + "] ... Warning: Hostname resolution failed or unrecognized host!");
			return "";
		}
	}

	public static Member checkPassword(String loginName, String password) {
		MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
		Member member = memberDAO.retrieveByLoginNameAndPassword(loginName, password);

		if (member == null) {
			logger.debug(loginName + " failed to log in with password: " + password);
			return null;
		}

		return member;
	}

	public static void login(HttpServletRequest request, Member member) {
		//TODO: Set info in cookie
		request.getSession().setAttribute("USER_ID", member.getRecID());
		request.getSession().setAttribute("USER_NAME", member.getDisplayName());
		logger.debug(member.getRecID() + "/" + member.getLoginName() + " logged in.");
	}

	public static void logout(HttpServletRequest request, String info) {
		request.getSession().setAttribute("USER_ID", 0);
		request.getSession().setAttribute("USER_NAME", "");
		logger.debug("User logged out: " + info);
	}

	private static Cookie getCookieByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) return cookie;
		}
		return null;
	}

	private static String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie found = getCookieByName(request, name);
		if (found == null) return "";
		return found.getValue();
	}

	public Member getMemberFromRequestCookieToken(HttpServletRequest request) {

		// check for a token cookie
		final String input = getCookieValueByName(request, "token");

		// check if a valid token
		final String regex = "[0-9]+.[A-Za-z0-9+\\/=]+";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(input);
		if (!matcher.matches()) return null;
		
		// check database for match
		final int uid;
		try {
			uid = Integer.parseInt(matcher.group(1));
		} catch (Exception e) {
			// TODO: Clear cookie with bad token here
			logger.debug("Failed to parse UID from token: [ {} ]", matcher.group(0));
			return null;
		}
		final String token = matcher.group(2);

		final MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
		final Member member = memberDAO.retrieveByIdAndToken(uid, token);

		if (member == null) {
			// TODO: Clear cookie with bad token here
			logger.debug("UID [ {} ] failed to log in with token: [ {} ]", uid, token);
		}
		// might be null
		return member;

		// check 
		int uid = ;

		return 0;
	}

	// !!! This has its own process to ensure security
	// Must validate a token against a User ID to proceed
	public static int getUserId(HttpServletRequest request) {
		
		// USER ID
		if (request.getSession().getAttribute("USER_ID") == null) return 0;
		if (request.getSession().getAttribute("USER_ID").toString() == null) return 0;
		if (request.getSession().getAttribute("USER_ID").toString().isEmpty()) return 0;
		String uid = request.getSession().getAttribute("USER_ID").toString();
		try {
			return Integer.parseInt(uid);
		} catch (NumberFormatException e) {
			logger.fatal("SECURITY RISK: Invalid data stored in session's USER_ID value!");
		}
		return 0;
	}

}
