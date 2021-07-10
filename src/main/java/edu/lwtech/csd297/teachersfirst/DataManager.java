package edu.lwtech.csd297.teachersfirst;

import java.io.*;
import java.util.*;

import javax.servlet.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.obj.*;
import freemarker.core.ParseException;

import org.apache.logging.log4j.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataManager {
	
	private static final Logger logger = LogManager.getLogger(AppointmentSqlDAO.class.getName());

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

	public static final List<DAO<?>> allDAOs = new ArrayList<>();
	private static DAO<Member> memberDAO = null;
	private static DAO<Service> serviceDAO = null;
	private static DAO<Appointment> appointmentDAO = null;
	private static DAO<Opening> openingDAO = null;

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

	public static void initializeDAOs() throws ServletException {

		// Merge Connection Parameters:
		String initParams = "jdbc:mariadb://" + databaseHostname + ":" + databasePort + "/" + databaseSchema;
		initParams += "?useSSL=false&allowPublicKeyRetrieval=true";
		initParams += "&user=" + databaseUserID + "&password=" + databasePassword;    

		//DataManager.serviceDAO = new ServiceMemoryDAO();
		DataManager.serviceDAO = new ServiceSqlDAO();
		if (!DataManager.serviceDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the serviceDAO.");
		DataManager.allDAOs.add(DataManager.serviceDAO);

		DataManager.memberDAO = new MemberSqlDAO();
		if (!DataManager.memberDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the memberDAO.");
		DataManager.allDAOs.add(DataManager.memberDAO);

		DataManager.appointmentDAO = new AppointmentSqlDAO();
		if (!DataManager.appointmentDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the appointmentDAO.");
		DataManager.allDAOs.add(DataManager.appointmentDAO);

		DataManager.openingDAO = new OpeningSqlDAO();
		if (!DataManager.openingDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the openingDAO.");
		DataManager.allDAOs.add(DataManager.openingDAO);

	}

	public static void terminateDAOs() {
		// memberDAO.terminate();
		int i = 0;
		for (DAO<?> iDAO : DataManager.allDAOs) {
			try {
				iDAO.terminate();
			} catch (NullPointerException ex) {
				// This is a test-only catch, should not throw in normal use
				System.out.println("=============================================== Error");
				System.out.println("| DAO #" + i + " TRIED TO TERMINATE WHEN SET TO NULL! | Error");
				System.out.println("=============================================== Error");
			}
			i++;
		}
	}

	public static void resetDAOs() {

		DataManager.terminateDAOs();

		DataManager.allDAOs.clear();

		try {
			DataManager.initializeDAOs();
		} catch (ServletException ex) {
			System.out.println("===================================== Error");
			System.out.println("| ERROR CONNECTING TO SQL DATABASE! | Error");
			System.out.println("===================================== Error");
		}
	}

	public static boolean validateSQLConnection() {
		if (DataManager.memberDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (DataManager.memberDAO == null).");
			return false;
		} else if (DataManager.appointmentDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (DataManager.appointmentDAO == null).");
			return false;
		} else if (DataManager.openingDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (DataManager.openingDAO == null).");
			return false;
		} else if (DataManager.memberDAO.retrieveByIndex(0) == null) {
			logger.error("ERROR: Database connection validation FAILED (DataManager.memberDAO.retrieveByIndex(0) == null).");
			return false;
		}
		return true;
	}

	public static DAO<Member> getMemberDAO() {
		return DataManager.memberDAO;
	}

	public static DAO<Service> getServiceDAO() {
		return DataManager.serviceDAO;
	}

	public static DAO<Appointment> getAppointmentDAO() {
		return DataManager.appointmentDAO;
	}

	public static DAO<Opening> getOpeningDAO() {
		return DataManager.openingDAO;
	}

}
