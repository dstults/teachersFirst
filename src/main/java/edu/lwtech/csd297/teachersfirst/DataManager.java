package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.servlet.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DataManager {
	
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

	public static String websiteTitle = "CoolTutors.org";
	public static String websiteSubtitle = "The coolest tutors on the web!";

	public static final List<DAO<?>> allDAOs = new ArrayList<>();
	private static DAO<Member> memberDAO = null;
	private static DAO<Service> serviceDAO = null;
	private static DAO<Appointment> appointmentDAO = null;
	private static DAO<Opening> openingDAO = null;

	// Meta "construct" and "destruct" (and "reset")

	public static void initializeDAOs() throws ServletException {

		String initParams = "jdbc:mariadb://lwtech-csd297.cv18zcsjzteu.us-west-2.rds.amazonaws.com:3306/mercer";
        initParams += "?useSSL=false&allowPublicKeyRetrieval=true";
        initParams += "&user=mercer&password=mercer-rox";    

		DataManager.memberDAO = new MemberSqlDAO();
		if (!DataManager.memberDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the memberDAO.");
		DataManager.allDAOs.add(DataManager.memberDAO);

		DataManager.serviceDAO = new ServiceMemoryDAO();
		if (!DataManager.serviceDAO.initialize("")) throw new UnavailableException("Unable to initialize the serviceDAO.");
		DataManager.allDAOs.add(DataManager.serviceDAO);

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
		if (DataManager.memberDAO == null) return false;
		if (DataManager.appointmentDAO == null) return false;
		if (DataManager.openingDAO == null) return false;
		if (DataManager.memberDAO.retrieveByIndex(0) == null) return false;
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
