package org.funteachers.teachersfirst.managers;

import java.sql.*;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.daos.sql.*;
import org.funteachers.teachersfirst.obj.*;

import org.apache.logging.log4j.*;
	
public class ConnectionPackage {

	private static final Logger logger = LogManager.getLogger();

	// Packaged for convenience
	private HttpServletRequest request;
	private HttpServletResponse response;
	private SecurityChecker security;

	// For logging
	final private String initialCaller;
	
	// Database-related
	private DAO<Member> memberDAO;
	private DAO<Appointment> appointmentDAO;
	private DAO<Opening> openingDAO;
	private DAO<LoggedEvent> loggedEventDAO;
	private HybridDAO hybridDAO;

	private boolean attemptedConnection = false;
	private Connection connection;
	private String connectionStatusMessage = "";
	private boolean isConnectionHealthy;
	
	// ================ CONSTRUCTOR ================

	public ConnectionPackage(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		if (this.request != null && this.response != null) this.security = new SecurityChecker(request, response, this);
		this.initialCaller = request == null ? "SYS" : request.getPathInfo() == null ? "" : request.getPathInfo();
	}

	private void initialize() {
		if (this.attemptedConnection) {
			logger.warn("WARNING: Repeat connection attempted, aborting!");
			return;
		}

		this.attemptedConnection = true;
		this.connection = SQLUtils.connect(GlobalConfig.getInitParams());
		if (this.connection == null) {
			this.isConnectionHealthy = false;
			this.connection = SQLUtils.connect(GlobalConfig.getNewInitParams());
			if (this.connection != null) {
				logger.warn("====================================== Warning");
				logger.warn("|  WARNING, DATABASE NOT BUILT YET!  | Warning");
				logger.warn("|    Attempting auto-build......     | Warning");
				logger.warn("====================================== Warning");
				System.out.println("ConnectionPackage initialized() new connection but found no schema! Attempting auto-build...");
				if (!SQLUtils.buildDatabase(this.connection)) {
					this.connectionStatusMessage = "WARNING: Database not built yet, but unable to build.";
					return;	
				}
				// else: build successful, continue to link up DAOs
			} else {
				logger.error("===================================== Error");
				logger.error("| ERROR CONNECTING TO SQL DATABASE! | Error");
				logger.error("===================================== Error");
				System.out.println("ConnectionPackage failed to initialize() new connection!");
				this.connectionStatusMessage = "ERROR: Database cannot be contacted.";
				return;
			}
		}

		memberDAO = new MemberSqlDAO(this.connection);
		appointmentDAO = new AppointmentSqlDAO(this.connection);
		openingDAO = new OpeningSqlDAO(this.connection);
		loggedEventDAO = new LoggedEventSqlDAO(this.connection);
		hybridDAO = new HybridDAO(this.connection);

		this.isConnectionHealthy = true;
		this.connectionStatusMessage = "good";
		
	}

	// ================ GETTERS ================

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public SecurityChecker getSecurity() {
		return this.security;
	}

	public Connection getConnection(String reason) {
		reason = this.initialCaller + ":" + reason;
		logger.debug("DATABASE Get-Conn: [ {} ]", reason);
		if (this.connection == null) this.initialize();
		return this.connection;
	}

	public DAO<Member> getMemberDAO(String reason) {
		reason = this.initialCaller + ":" + reason;
		logger.debug("DATABASE Members: [ {} ]", reason);
		if (this.connection == null) this.initialize();
		return this.memberDAO;
	}

	public DAO<Appointment> getAppointmentDAO(String reason) {
		reason = this.initialCaller + ":" + reason;
		logger.debug("DATABASE Appointments: [ {} ]", reason);
		if (this.connection == null) this.initialize();
		return this.appointmentDAO;
	}

	public DAO<Opening> getOpeningDAO(String reason) {
		reason = this.initialCaller + ":" + reason;
		logger.debug("DATABASE Openings: [ {} ]", reason);
		if (this.connection == null) this.initialize();
		return this.openingDAO;
	}

	public DAO<LoggedEvent> getLoggedEventDAO(String reason) {
		reason = this.initialCaller + ":" + reason;
		logger.debug("DATABASE LoggedEvents: [ {} ]", reason);
		if (this.connection == null) this.initialize();
		return this.loggedEventDAO;
	}

	// ================ METHODS ================

	public void terminate() {
		if (this.connection == null) {
			// I should probably log something here but it doesn't break anything
		}
		SQLUtils.disconnect(this.connection);
		this.connection = null;

	}

	public String getConnectionStatusMessage() {
		return this.connectionStatusMessage;
	}

	public boolean getIsConnectionHealthy() {
		return this.isConnectionHealthy;
	}

	public boolean validate() {
		this.isConnectionHealthy = true;
		String comma = "";
		if (this.connection == null) {
			this.connectionStatusMessage += comma + "Cannot establish connection";
			logger.error("ERROR: Failed to establish database connection!");
			this.isConnectionHealthy = false;
			comma = ", ";
			// If this error is thrown, all of the following must be thrown -- they're just redundant.
			return this.isConnectionHealthy;
		}
		if (this.memberDAO == null) {
			this.connectionStatusMessage += comma + "memberDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (memberDAO == null).");
			this.isConnectionHealthy = false;
			comma = ", ";
		}
		if (this.appointmentDAO == null) {
			this.connectionStatusMessage += comma + "appointmentDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (appointmentDAO == null).");
			this.isConnectionHealthy = false;
			comma = ", ";
		} 
		if (this.openingDAO == null) {
			this.connectionStatusMessage += comma + "openingDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (openingDAO == null).");
			this.isConnectionHealthy = false;
			comma = ",";
		}
		if (this.loggedEventDAO == null) {
			this.connectionStatusMessage += comma + "loggedEventDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (loggedEventDAO == null).");
			this.isConnectionHealthy = false;
			comma = ", ";
		}
		if (this.memberDAO != null && this.memberDAO.retrieveByIndex(0) == null) {
			this.connectionStatusMessage += comma + "memberDAO.retrieveByIndex(0) == null";
			logger.error("ERROR: Database connection validation FAILED (memberDAO.retrieveByIndex(0) == null).");
			this.isConnectionHealthy = false;
			comma = ", ";
		}
		if (this.hybridDAO == null) {
			this.connectionStatusMessage += comma + "this.hybridDAO == null";
			logger.error("ERROR: Database connection validation FAILED (this.hybridDAO == null).");
			this.isConnectionHealthy = false;
			comma = ", ";
		}

		if (this.isConnectionHealthy) this.connectionStatusMessage = "good";

		return this.isConnectionHealthy;
	}

}
