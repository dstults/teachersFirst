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

	// Meta constructors and destructors

	public static void initializeDAOs() throws ServletException {
		DataManager.memberDAO = new MemberMemoryDAO();
		if (!DataManager.memberDAO.initialize("")) throw new UnavailableException("Unable to initialize the memberDAO.");
		DataManager.allDAOs.add(DataManager.memberDAO);

		DataManager.serviceDAO = new ServiceMemoryDAO();
		if (!DataManager.serviceDAO.initialize("")) throw new UnavailableException("Unable to initialize the serviceDAO.");
		DataManager.allDAOs.add(DataManager.serviceDAO);
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

}
