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

	// Packaged for convenience
	private HttpServletRequest request;
	private HttpServletResponse response;
	private SecurityChecker security;
	
	// Database-related
	final private List<DAO<?>> allDAOs = new ArrayList<>();
	private DAO<Member> memberDAO;
	private DAO<Appointment> appointmentDAO;
	private DAO<Opening> openingDAO;
	private DAO<LoggedEvent> loggedEventDAO;

	private Connection connection;
	private String connectionStatusMessage = "";
	private boolean isConnectionHealthy;
	
	// ================ CONSTRUCTOR ================

	public ConnectionPackage(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		if (this.request != null && this.response != null) this.security = new SecurityChecker(request, response, this);
		this.initialize();
	}

	public void initialize() {
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
		allDAOs.add(memberDAO);
		appointmentDAO = new AppointmentSqlDAO(this.connection);
		allDAOs.add(appointmentDAO);
		openingDAO = new OpeningSqlDAO(this.connection);
		allDAOs.add(openingDAO);
		loggedEventDAO = new LoggedEventSqlDAO(this.connection);
		allDAOs.add(loggedEventDAO);

		this.isConnectionHealthy = true;
		this.connectionStatusMessage = "good";
		
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

	public boolean reset() {
		this.terminate();
		//this.allDAOs.clear();
		this.initialize();
		return this.connection != null;
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
			comma = ",";
			// If this error is thrown, all of the following must be thrown -- they're just redundant.
			return this.isConnectionHealthy;
		}
		if (this.memberDAO == null) {
			this.connectionStatusMessage += comma + "memberDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (memberDAO == null).");
			this.isConnectionHealthy = false;
			comma = ",";
		}
		if (this.appointmentDAO == null) {
			this.connectionStatusMessage += comma + "appointmentDAO == null";
			logger.warn("WARNING: Database connection validation FAILED (appointmentDAO == null).");
			this.isConnectionHealthy = false;
			comma = ",";
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
			comma = ",";
		}
		if (this.memberDAO != null && this.memberDAO.retrieveByIndex(0) == null) {
			this.connectionStatusMessage += comma + "memberDAO.retrieveByIndex(0) == null";
			logger.error("ERROR: Database connection validation FAILED (memberDAO.retrieveByIndex(0) == null).");
			this.isConnectionHealthy = false;
			comma = ",";
		}

		if (this.isConnectionHealthy) this.connectionStatusMessage = "good";

		return this.isConnectionHealthy;
	}

}
