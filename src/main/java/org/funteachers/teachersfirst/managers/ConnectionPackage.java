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

	public ConnectionPackage(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		if (this.request != null && this.response != null) this.security = new SecurityChecker(request, response, this);
	}

	public void initialize() throws ServletException {
		String initParams = DataManager.getInitParams();

		// Because one day, these might have different connections to different databases, they need to be initialized
		// independently -- though because they're passed the same connection in this case, they will initialize once
		// with the same connection starting only one time.

		String dbParameters = DataManager.getInitParams();
		this.connection = SQLUtils.connect(dbParameters);
		if (this.connection == null) throw new UnavailableException("Unable to initialize the database connection: " + dbParameters);

		memberDAO = new MemberSqlDAO(this.connection);
		//if (!memberDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the appointmentDAO.");
		allDAOs.add(memberDAO);

		appointmentDAO = new AppointmentSqlDAO(this.connection);
		//if (!appointmentDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the appointmentDAO.");
		allDAOs.add(appointmentDAO);

		openingDAO = new OpeningSqlDAO(this.connection);
		//if (!openingDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the openingDAO.");
		allDAOs.add(openingDAO);

		loggedEventDAO = new LoggedEventSqlDAO(this.connection);
		//if (!loggedEventDAO.initialize(initParams)) throw new UnavailableException("Unable to initialize the loggedEventDAO.");
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
		// memberDAO.terminate();
		SQLUtils.disconnect(this.connection);
		this.connection = null;

/*		int i = 0;
		for (DAO<?> iDAO : this.allDAOs) {
			try {
				logger.debug("Terminating Appointment SQL DAO...");
			} catch (NullPointerException ex) {
				// This is a test-only catch, should not throw in normal use
				System.out.println("=============================================== Error");
				System.out.println("| DAO #" + i + " TRIED TO TERMINATE WHEN SET TO NULL! | Error");
				System.out.println("=============================================== Error");
			}
			i++;
		}*/
	}

	public boolean reset() {

		this.terminate();

		//this.allDAOs.clear();

		try {
			this.initialize();
			return true;
		} catch (ServletException ex) {
			System.out.println("===================================== Error");
			System.out.println("| ERROR CONNECTING TO SQL DATABASE! | Error");
			System.out.println("===================================== Error");
			return false;
		}
	}

	public boolean validate() {
		if (this.connection == null) {
			logger.warn("Attempted to validate non-initialized SQL connections. Force initializing!");
			try {
				this.initialize();
			} catch (ServletException e) {
				logger.warn("Failed to initialize SQL connection(s)!");
				return false;
			}
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
