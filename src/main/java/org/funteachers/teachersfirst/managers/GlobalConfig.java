package org.funteachers.teachersfirst.managers;

import java.io.*;

//import org.apache.logging.log4j.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GlobalConfig {
	
	//private static final Logger logger = LogManager.getLogger();

	// WEBSITE CUSTOMIZABLE COLORS
	public static String primaryHighlightAdmin = "#96bbff";
	public static String primaryHighlightDarkAdmin = "#7da9fa";
	public static String backgroundColorAdmin = "#c6d9ff";
	public static String backgroundColorLightAdmin = "#dde5ff";

	public static String primaryHighlightInstructor = "#bbff96";
	public static String primaryHighlightDarkInstructor = "#a9fa7d";
	public static String backgroundColorInstructor = "#d9ffc6";
	public static String backgroundColorLightInstructor = "#e5ffdd";

	public static String primaryHighlightGeneral = "#ffbb96";
	public static String primaryHighlightDarkGeneral = "#faa97d";
	public static String backgroundColorGeneral = "#ffd9c6";
	public static String backgroundColorLightGeneral = "#ffe5dd";

	// WEBSITE CUSTOMIZABLE VARIABLES
	public static String websiteTitle = "CoolTutors.org";
	public static String websiteSubtitle = "The coolest tutors on the web!";

	// WEBSITE RIGHTS TOGGLES
	public static boolean enableOpenRegistration = true;
	public static boolean instructorAdminMakeAppointmentsRequiresOpening = true;

	// DATABASE CREDENTIALS, DO NOT PUBLIC
	private static String databaseHostname = "";
	private static String databasePort = "3306";
	private static String databaseUserID = "";
	private static String databasePassword = "";
	private static String databaseSchema = "";

	// Meta "construct" and "destruct" (and "reset")

	public static void initializeSiteData() {
		JSONParser parser = new JSONParser();
		// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
		JSONObject jsonObject;
		try {
			Object obj = parser.parse(new FileReader("/etc/tomcat9/myserver.conf"));
			jsonObject = (JSONObject) obj;
		} catch (FileNotFoundException ex) {
			websiteTitle = "Howdy!"; // low key/plain sight error message
			ex.printStackTrace();
			return;
		} catch (IOException ex) {
			websiteTitle = "Hello!"; // low key/plain sight error message
			ex.printStackTrace();
			return;
		} catch (org.json.simple.parser.ParseException ex) {
			websiteTitle = "Welcome!"; // low key/plain sight error message
			ex.printStackTrace();
			return;
		}

		if (jsonObject.containsKey("websiteTitle"))
			websiteTitle = jsonObject.get("websiteTitle").toString();
		if (jsonObject.containsKey("websiteSubtitle"))
			websiteSubtitle = jsonObject.get("websiteSubtitle").toString();
		if (jsonObject.containsKey("databaseHostname"))
			databaseHostname = jsonObject.get("databaseHostname").toString();
		if (jsonObject.containsKey("databasePort"))
			databasePort = jsonObject.get("databasePort").toString();
		if (jsonObject.containsKey("databaseUserID"))
			databaseUserID = jsonObject.get("databaseUserID").toString();
		if (jsonObject.containsKey("databasePassword"))
			databasePassword = jsonObject.get("databasePassword").toString();
		if (jsonObject.containsKey("databaseSchema"))
			databaseSchema = jsonObject.get("databaseSchema").toString();
		if (jsonObject.containsKey("enableOpenRegistration"))
			enableOpenRegistration = Boolean.parseBoolean(jsonObject.get("enableOpenRegistration").toString());
		if (jsonObject.containsKey("instructorAdminMakeAppointmentsRequiresOpening"))
			instructorAdminMakeAppointmentsRequiresOpening = Boolean.parseBoolean(jsonObject.get("instructorAdminMakeAppointmentsRequiresOpening").toString());
	}

	public static String getInitParams() {
		return "jdbc:mariadb://" + databaseHostname + ":" + databasePort + "/" + databaseSchema +
				"?useSSL=false&allowPublicKeyRetrieval=true" +
				"&user=" + databaseUserID + "&password=" + databasePassword;
	}

}
