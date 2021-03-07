package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.servlet.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DataManager {
	
	public static String primaryHighlight = "#ffbb96";
	public static String primaryHighlightDark = "#faa97d";
	public static String backgroundColor = "#ffd9c6";

	public static String websiteTitle = "CoolTutors";
	public static String websiteSubtitle = "The coolest tutors on the web!";

	public static final List<DAO<?>> allDAOs = new ArrayList<>();
	private static DAO<Member> memberDAO = null;
	private static DAO<Service> serviceDAO = null;
	private static DAO<Appointment> appointmentDAO = null;
	private static DAO<Opening> openingDAO = null;

	// Meta constructors and destructors

	public static void initializeDAOs() throws ServletException {

		DataManager.memberDAO = new MemberMemoryDAO();
		if (!DataManager.memberDAO.initialize("")) throw new UnavailableException("Unable to initialize the memberDAO.");
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
