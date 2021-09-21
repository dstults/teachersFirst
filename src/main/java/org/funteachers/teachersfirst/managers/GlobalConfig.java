package org.funteachers.teachersfirst.managers;

import java.io.*;

import org.funteachers.teachersfirst.ServerMain;
import org.apache.logging.log4j.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GlobalConfig {
	
	private static final Logger logger = LogManager.getLogger();

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
	public static String websiteTitle = "A Teachers First Site";
	public static String websiteSubtitle = "Get yours today!";

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
		// Check if file exists -- if it doesn't, create with default values
		File serverConfigFile = new File("/etc/tomcat9/myserver.conf");
		if (!serverConfigFile.exists()) {
			logger.debug("Config not found, creating one!");
			try {
				serverConfigFile.createNewFile();
				FileWriter fw = new FileWriter(serverConfigFile);
				fw.write(getDefaultConfigText());
				fw.close();
			} catch (IOException ex) {
				logger.debug("Failed to create config file, reason: {}", ex.getMessage());
				return;
			}
		}

		// Load Data into JSON object:
		JSONParser parser = new JSONParser();
		// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
		JSONObject jsonObject;
		try {
			FileReader reader = new FileReader(serverConfigFile);
			Object obj = parser.parse(reader);
			jsonObject = (JSONObject) obj;
			reader.close();
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
		if (databaseSchema.equals("")) {
			ServerMain.logger.warn("!!!!!!!! DB init params missing schema!");
			return getNewInitParams();
		}

		return "jdbc:mariadb://" + databaseHostname + ":" + databasePort + "/" + databaseSchema +
				"?useSSL=false&allowPublicKeyRetrieval=true" +
				"&user=" + databaseUserID + "&password=" + databasePassword;
	}

	public static String getNewInitParams() {
		return "jdbc:mariadb://" + databaseHostname + ":" + databasePort +
			"?useSSL=false&allowPublicKeyRetrieval=true" +
			"&user=" + databaseUserID + "&password=" + databasePassword;
	}

	private static String getDefaultConfigText() {
		// Note about quotation marks around variables that don't need them:
		// They're like that because all of the variables get passed into a string eventually so it's
		// more predictable if they're that way to begin with.
		return "{\n" +
		"{\n" +
		"	\"websiteTitle\": \"Example.com\",\n" +
		"	\"websiteSubtitle\": \"A catchy slogan!\",\n" +
		"	\"databaseHostname\": \"databaseaddress.com\",\n" +
		"	\"databasePort\": \"3306\",\n" +
		"	\"databaseUserID\": \"adminid\",\n" +
		"	\"databasePassword\": \"secretpassword\",\n" +
		"	\"databaseSchema\": \"teachersFirst\",\n" +
		"	\"enableOpenRegistration\": \"false\",\n" +
		"	\"instructorAdminMakeAppointmentsRequiresOpening\": \"false\"\n" +
		"}";
	}

}
