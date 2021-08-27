package org.funteachers.teachersfirst.managers;

import java.math.BigInteger;
import java.security.*;
import java.util.*;
import java.util.regex.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.ServerMain;
import org.funteachers.teachersfirst.daos.sql.*;
import org.funteachers.teachersfirst.obj.*;

public class SecurityChecker {
	
	private static final Logger logger = LogManager.getLogger();

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

	/*
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
	*/

	/*
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
	*/

	// ===================================================================================
	//   Passwords, Tokens, and Logins
	// ===================================================================================
	// Note: To be thread safe, these musn't be static.

	final private HttpServletRequest request;
	final private HttpServletResponse response;
	final private ConnectionPackage connectionPackage;

	public SecurityChecker(HttpServletRequest request, HttpServletResponse response, ConnectionPackage connectionPackage) {
		this.request = request;
		this.response = response;
		this.connectionPackage = connectionPackage;
	}

	public String getRealIp() {
		String proxyResult = request.getHeader("X-FORWARDED-FOR");
		if (proxyResult != null) return proxyResult;

		String result = request.getRemoteAddr();
		return result;
	}

	public Member checkPassword(String loginName, String password) {
		MemberSqlDAO memberDAO = (MemberSqlDAO) connectionPackage.getMemberDAO();
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

	public void logout(Member member, String info, boolean allDevices) {
		clearTokenCookie(member, allDevices);
		logger.debug("User [ ({}) {} ] logged out: [{}]", member.getRecID(), member.getLoginName(), info);
	}

	private Cookie getCookieByName(String name) {
		final Cookie[] cookies = request.getCookies();

		if (cookies == null || cookies.length == 0)
			return null;

		for (Cookie cookie : cookies)
			if (cookie.getName().equals(name)) return cookie;

		return null;
	}

	private String getCookieValueByName(String name) {
		final Cookie found = getCookieByName(name);
		if (found == null) return "";
		return found.getValue();
	}

	public static String encryptString(String input)
	{
		String algorithm = "SHA-1";
		// https://www.geeksforgeeks.org/sha-1-hash-in-java/
		try {
			// getInstance() method is called with algorithm SHA-1
			MessageDigest md = MessageDigest.getInstance(algorithm);

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 32)
				hashtext = "0" + hashtext;

			// return the HashText
			return hashtext;

		} catch (NoSuchAlgorithmException e) {

			logger.error("Do not support [ {} ] algorithm!", algorithm);
			throw new RuntimeException(e);
		}
	}

	private String getToken() {
		// Salt:
		StringBuilder sb = new StringBuilder();
		sb.append(getRealIp());
		Random rnd = new Random();
		for (int i = 0; i < 10; i++) {
			sb.append(rnd.nextInt(10));
		}
		String saltStr = encryptString(sb.toString());
		return saltStr;
	}

	private void giveTokenCookie(Member member) {
		// Check for an already existing token
		MemberSqlDAO memberDAO = (MemberSqlDAO) connectionPackage.getMemberDAO();
		String token = memberDAO.retrieveToken(member.getRecID());
		
		// Make new token if none exists
		if (token == null || token.isEmpty()) {
			token = getToken();				
			// Update database if token generated
			memberDAO.updateToken(member, token);
		}
		logger.debug("Giving token [ {} ] to member [ ({}) {} ]", token, member.getRecID(), member.getLoginName());
		
		// Set new cookie
		final Cookie tokenCookie = new Cookie("token", member.getRecID() + "." +token);
		tokenCookie.setMaxAge(60 * 60 * 24 * 90); // Expires in 90 days
		tokenCookie.setSecure(true);
		tokenCookie.setHttpOnly(true);
		response.addCookie(tokenCookie);
	}

	private void clearTokenCookie(Member member, boolean allDevices) {

		// Update database if member provided
		if (member != null && allDevices) {
			MemberSqlDAO memberDAO = (MemberSqlDAO) connectionPackage.getMemberDAO();
			memberDAO.updateToken(member, null);
			logger.debug("Clearing token for member [ ({}) {} ]", member.getRecID(), member.getLoginName());
		}

		// Update cookie to expire
		final Cookie tokenCookie = new Cookie("token", "");
		tokenCookie.setMaxAge(-1); // Expires in the past by 1 second
		response.addCookie(tokenCookie);
	}

	private void refreshCookie(Member member, String token) {
		// Security warning: token should not actually be logged -- this has been removed though because it's spammy
		//logger.debug("Refreshing token maxAge for memberID [ ({}) {} ]", member.getRecID(), member.getLoginName());

		// Refresh cookie with new expiration
		final Cookie tokenCookie = new Cookie("token", member.getRecID() + "." + token);
		tokenCookie.setMaxAge(60 * 60 * 24 * 90); // Expires in 90 days
		tokenCookie.setSecure(true);
		tokenCookie.setHttpOnly(true);
		response.addCookie(tokenCookie);
	}

	public Member getMemberFromRequestCookieToken() {

		// check for a token cookie
		final String input = getCookieValueByName("token");
		if (input == null || input.isEmpty()) return null;

		// check if a valid token
		final String regex = "([0-9]+).([A-Za-z0-9+\\/=]+)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(input);
		if (!matcher.matches()) return null;
		
		// check database for match
		final int uid;
		try {
			uid = Integer.parseInt(matcher.group(1));
		} catch (Exception e) {
			clearTokenCookie(null, false);
			logger.debug("Failed to parse UID from token: [ {} ], found [ {} ], expected [ int ]", matcher.group(0), matcher.group(1));
			return null;
		}
		final String token = matcher.group(2);

		final MemberSqlDAO memberDAO = (MemberSqlDAO) connectionPackage.getMemberDAO();
		final Member member = memberDAO.retrieveByIdAndToken(uid, token);

		if (member == null) {
			clearTokenCookie(null, false);
			logger.debug("Failed token validation for UID [ {} ], token used: [ {} ]", uid, token);
		} else {
			refreshCookie(member, token);
		}

		return member;
	}

	// This was replaced by the token method:
	/*
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
	*/

}
