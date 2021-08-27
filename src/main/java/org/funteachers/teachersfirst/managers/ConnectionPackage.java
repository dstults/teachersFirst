package org.funteachers.teachersfirst.managers;

import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.daos.sql.*;
import org.funteachers.teachersfirst.obj.*;

import org.apache.logging.log4j.*;
	
public class ConnectionPackage {

	private static final Logger logger = LogManager.getLogger(AppointmentSqlDAO.class.getName());

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	final List<DAO<?>> allDAOs = new ArrayList<>();
	private DAO<Member> memberDAO;
	private DAO<Appointment> appointmentDAO;
	private DAO<Opening> openingDAO;
	private DAO<LoggedEvent> loggedEventDAO;

	private SecurityChecker security;

	private Connection connection;
	
	// ================ CONSTRUCTOR ================

	ConnectionPackage(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.security = new SecurityChecker(request, response, this);
	}

	public void initializeDatabaseConnections() throws ServletException {
		String initParams = DataManager.getInitParams();
		memberDAO = new MemberSqlDAO();
		if (!memberDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the memberDAO.");
		appointmentDAO = new AppointmentSqlDAO();
		if (!appointmentDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the appointmentDAO.");
		openingDAO = new OpeningSqlDAO();
		if (!openingDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the openingDAO.");
		loggedEventDAO = new LoggedEventSqlDAO();
		if (!loggedEventDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the loggedEventDAO.");
	}

	// ================ GETTERS ================

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public SecurityChecker getSecurity() {
		return security;
	}

	public Connection getConnection() {
		return connection;
	}

	public DAO<Member> getMemberDAO() {
		return this.memberDAO;
	}

	public DAO<Appointment> getAppointmentDAO() {
		return this.appointmentDAO;
	}

	public DAO<Opening> getOpeningDAO() {
		return this.openingDAO;
	}

	public DAO<LoggedEvent> getLoggedEventDAO() {
		return this.loggedEventDAO;
	}

	// ================ METHODS ================

	public void terminateDAOs() {
		// memberDAO.terminate();
		int i = 0;
		for (DAO<?> iDAO : this.allDAOs) {
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

	public boolean resetDAOs() {

		this.terminateDAOs();

		this.allDAOs.clear();

		try {
			this.initializeDatabaseConnections();
			return true;
		} catch (ServletException ex) {
			System.out.println("===================================== Error");
			System.out.println("| ERROR CONNECTING TO SQL DATABASE! | Error");
			System.out.println("===================================== Error");
			return false;
		}
	}

	public boolean validateSQLConnection() {
		boolean didAllTestsPass = true;
		if (this.memberDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (memberDAO == null).");
			didAllTestsPass = false;
		}
		if (this.appointmentDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (appointmentDAO == null).");
			didAllTestsPass = false;
		} 
		if (this.openingDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (openingDAO == null).");
			didAllTestsPass = false;
		}
		if (this.loggedEventDAO == null) {
			logger.warn("WARNING: Database connection validation FAILED (loggedEventDAO == null).");
			didAllTestsPass = false;
		}
		if (this.memberDAO.retrieveByIndex(0) == null) {
			logger.error("ERROR: Database connection validation FAILED (memberDAO.retrieveByIndex(0) == null).");
			didAllTestsPass = false;
		}
		return didAllTestsPass;
	}

}
