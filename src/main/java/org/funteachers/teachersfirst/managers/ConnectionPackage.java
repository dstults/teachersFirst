package org.funteachers.teachersfirst.managers;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.daos.sql.*;
import org.funteachers.teachersfirst.obj.*;

import org.apache.logging.log4j.*;
	
public class ConnectionPackage {

	private static final Logger logger = LogManager.getLogger();

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

	public ConnectionPackage(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		if (this.request != null && this.response != null) this.security = new SecurityChecker(request, response, this);
		this.initialize();
	}

	public void initialize() {
		String dbParameters = DataManager.getInitParams();
		this.connection = SQLUtils.connect(dbParameters);
		if (this.connection == null) {
			System.out.println("===================================== Error");
			System.out.println("| ERROR CONNECTING TO SQL DATABASE! | Error");
			System.out.println("===================================== Error");
			logger.warn("ConnectionPackage failed to initialize() new connection!");
			return;
		}

		memberDAO = new MemberSqlDAO(this.connection);
		allDAOs.add(memberDAO);
		appointmentDAO = new AppointmentSqlDAO(this.connection);
		allDAOs.add(appointmentDAO);
		openingDAO = new OpeningSqlDAO(this.connection);
		allDAOs.add(openingDAO);
		loggedEventDAO = new LoggedEventSqlDAO(this.connection);
		allDAOs.add(loggedEventDAO);
		
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

	public void terminate() {
		if (this.connection == null) {

		}
		// memberDAO.terminate();
		SQLUtils.disconnect(this.connection);
		this.connection = null;

	}

	public boolean reseto() {
		this.terminate();
		//this.allDAOs.clear();
		this.initialize();
		return this.connection != null;
	}

	public boolean validateo() {
		if (this.connection == null) {
			logger.warn("Attempted to validate non-initialized SQL connections. Force initializing!");
			this.initialize();
		}

		boolean didAllTestsPass = true;
		if (this.connection == null) {
			logger.error("ERROR: Failed to establish database connection!");
			didAllTestsPass = false;
		}
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
