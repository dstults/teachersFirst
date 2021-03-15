package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.servlet.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DataManager {
	
	public static String primaryHighlightAdmin = "#96bbff";
	public static String primaryHighlightDarkAdmin = "#7da9fa";
	public static String backgroundColorAdmin = "#c6d9ff";
	public static String backgroundColorDarkAdmin = "#96bbff"; // same as primary highlight?

	public static String primaryHighlightInstructor = "#bbff96";
	public static String primaryHighlightDarkInstructor = "#a9fa7d";
	public static String backgroundColorInstructor = "#d9ffc6";
	public static String backgroundColorDarkInstructor = "#bbff96"; // same as primary highlight?

	public static String primaryHighlightGeneral = "#ffbb96";
	public static String primaryHighlightDarkGeneral = "#faa97d";
	public static String backgroundColorGeneral = "#ffd9c6";
	public static String backgroundColorDarkGeneral = "#ffbb96"; // same as primary highlight?

	public static String websiteTitle = "CoolTutors.org";
	public static String websiteSubtitle = "The coolest tutors on the web!";

	public static final List<DAO<?>> allDAOs = new ArrayList<>();
	private static DAO<Member> memberDAO = null;
	private static DAO<Service> serviceDAO = null;
	private static DAO<Appointment> appointmentDAO = null;
	private static DAO<Opening> openingDAO = null;

	// Meta constructors and destructors

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

		DataManager.appointmentDAO = new AppointmentMemoryDAO();
		if (!DataManager.appointmentDAO.initialize("")) throw new UnavailableException("Unable to initialize the appointmentDAO.");
		DataManager.allDAOs.add(DataManager.appointmentDAO);

		DataManager.openingDAO = new OpeningMemoryDAO();
		if (!DataManager.openingDAO.initialize("")) throw new UnavailableException("Unable to initialize the openingDAO.");
		DataManager.allDAOs.add(DataManager.openingDAO);

	}

	public static void terminateDAOs() {
		// memberDAO.terminate();
		for (DAO<?> iDAO : DataManager.allDAOs) {
			iDAO.terminate();
		}
	}

	// methods

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
