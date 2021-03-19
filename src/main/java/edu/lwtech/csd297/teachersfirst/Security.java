package edu.lwtech.csd297.teachersfirst;

import java.net.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.daos.MemberSqlDAO;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class Security {
	
	private static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);
	private static final List<String> ipWhitelist = new ArrayList<String>();

	public static void populateWhitelist() {
		
		// Manual entries:		

		whitelistIp("50.35.66.200"); // Darren
		whitelistIp("97.127.115.35"); // Edmund
		whitelistIp("73.140.252.100"); // Tanya

		//TODO: Add some sort of remote server/SQL-like verification method in case get locked out of IPs
		//or even a separate webadmin login

		// Automatic entries:		
		// Oh no: There is a problem doing nslookups on jenkins builds -- they probably have this
		// functionality disabled
		whitelistIp(nsLookup("dstults.net"));

	}

	private static void whitelistIp(String ip) {
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

		if(member != null){
			return member;
		}
		// should perform sql query, for now, just does this
		logger.debug(loginName + " failed to log in with password: " + password);
		return null;
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

	// This has its own process to ensure security
	public static int getUserId(HttpServletRequest request) {
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
