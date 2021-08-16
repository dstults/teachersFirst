package org.funteachers.teachersfirst;

import java.net.*;
import java.util.*;
import java.util.regex.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.daos.sql.MemberSqlDAO;
import org.funteachers.teachersfirst.obj.*;

public class Security {
	
	private static final Logger logger = LogManager.getLogger(ServerMain.class);

	// ===================================================================================
	//   IP Whitelist
	// ===================================================================================
	// Note: These are instantiated once and afterwards read-only, can be static.

	private static final List<String> ipWhitelist = Arrays.asList(
		"192.168.1.129"
	);

	public static boolean isWhitelisted(String ip) {
		return ipWhitelist.contains(ip);
	}

	public static void populateWhitelist() {
		// Manual entries:		
		//whitelistIp("");

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

	private static String nsLookup(String domain) {
		try {
			InetAddress inetHost = InetAddress.getByName(domain);
			logger.debug("Resolving [" + domain + "] ... IP is [" + inetHost.getHostAddress() + "]");
			return inetHost.getHostAddress();
		} catch(UnknownHostException ex) {
			logger.warn("Resolving [" + domain + "] ... Warning: Hostname resolution failed or unrecognized host!");
			return "";
		}
	}

	// ===================================================================================
	//   Passwords, Tokens, and Logins
	// ===================================================================================
	// Note: To be thread safe, these musn't be static.

	final private HttpServletRequest request;
	final private HttpServletResponse response;

	public Security(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public Member checkPassword(String loginName, String password) {
		MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
		Member member = memberDAO.retrieveByLoginNameAndPassword(loginName, password);

		if (member == null) {
			logger.debug(loginName + " failed to log in with password: " + password);
			return null;
		}

		login(member);
		return member;
	}

	public void login(Member member) {
		giveTokenCookie(member);
		logger.debug("User [ ({}) {} ] logged in.", member.getRecID(), member.getLoginName());
	}

	public void logout(Member member, String info) {
		clearTokenCookie(member);
		logger.debug("User [ ({}) {} ] logged out: [{}]", member.getRecID(), member.getLoginName(), info);
	}

	private Cookie getCookieByName(String name) {
		final Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) return cookie;
		}
		return null;
	}

	private String getCookieValueByName(String name) {
		final Cookie found = getCookieByName(name);
		if (found == null) return "";
		return found.getValue();
	}

	private void giveTokenCookie(Member member) {
		// TODO: Make token random and salted
		String token = "1234asdf";
		
		// Update database
		MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
		memberDAO.updateToken(member, token);
		logger.debug("Giving token [ {} ] to member [ ({}) {} ]", token, member.getRecID(), member.getLoginName());
		
		// Set new cookie
		final Cookie tokenCookie = new Cookie("token", member.getRecID() + "." + token);
		tokenCookie.setMaxAge(60 * 60 * 24 * 90); // Expires in 90 days
		response.addCookie(tokenCookie);
	}

	private void clearTokenCookie(Member member) {

		// Update database if member provided
		if (member != null) {
			MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
			memberDAO.updateToken(member, null);
			logger.debug("Clearing token for member [ ({}) {} ]", member.getRecID(), member.getLoginName());
		}

		// Update cookie to expire
		final Cookie tokenCookie = new Cookie("token", "");
		tokenCookie.setMaxAge(-1); // Expires in the past by 1 second
		response.addCookie(tokenCookie);
	}

	private void refreshCookie(int uid, String token) {

		// Refresh cookie with new expiration
		final Cookie tokenCookie = new Cookie("token", uid + "." + token);
		tokenCookie.setMaxAge(60 * 60 * 24 * 90); // Expires in 90 days
		response.addCookie(tokenCookie);
	}

	public Member getMemberFromRequestCookieToken() {

		// check for a token cookie
		final String input = getCookieValueByName("token");
		if (input == null || input.isEmpty()) return null;

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
			clearTokenCookie(null);
			logger.debug("Failed to parse UID from token: [ {} ]", matcher.group(0));
			return null;
		}
		final String token = matcher.group(2);

		final MemberSqlDAO memberDAO = (MemberSqlDAO) DataManager.getMemberDAO();
		final Member member = memberDAO.retrieveByIdAndToken(uid, token);

		if (member == null) {
			clearTokenCookie(null);
			logger.debug("Failed login for UID [ {} ], token used: [ {} ]", uid, token);
		} else {
			refreshCookie(uid, token);
		}

		return member;
	}

	// This was replaced by the token method:
	public int getUserId(HttpServletRequest request) {
		
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
