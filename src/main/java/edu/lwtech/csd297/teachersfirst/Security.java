package edu.lwtech.csd297.teachersfirst;

import java.net.*;
import java.util.*;

import org.apache.logging.log4j.*;

public class Security {
	
	private static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);
	private static final List<String> ipWhitelist = new ArrayList<String>();

	public static void populateWhitelist() {
		
		// Manual entries:		
		
		//whitelistIp("1.1.1.1");
		//whitelistIp("1.1.1.1");
		//whitelistIp("1.1.1.1");
		//whitelistIp("1.1.1.1");
		
		// Automatic entries:
		
		//TODO: these should refresh about once a day, maybe, or it should just be checked on the fly -- it is fast after all
		//well, food for thought.
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


	public static boolean checkPassword(int uid, String password) {
		// should perform sql query, for now, just does this
		logger.debug(uid + " attempted to log in with password: " + password);
		return password.equals("Password01");
	}

}
