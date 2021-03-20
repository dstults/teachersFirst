package edu.lwtech.csd297.teachersfirst.daos;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.DateHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class AppointmentSqlDAO implements DAO<Appointment> {
	
	private static final Logger logger = LogManager.getLogger(AppointmentSqlDAO.class.getName());

	private Connection conn = null;

	public AppointmentSqlDAO() {
		this.conn = null;                                   // conn must be created during init()
	}

	public boolean initialize(String initParams) {
		logger.info("Connecting to the database...");

		conn = SQLUtils.connect(initParams);
		if (conn == null) {
			logger.error("Unable to connect to SQL Database: " + initParams);
			return false;
		}
		logger.info("...connected!");

		return true;
	}

	public void terminate() {
		logger.debug("Terminating Appointment SQL DAO...");
		SQLUtils.disconnect(conn);
		conn = null;
	}

	public int insert(Appointment appointment) {
		logger.debug("Inserting " + appointment + "...");

		if (appointment.getRecID() != -1) {
			logger.error("Attempting to add previously added Appointment: " + appointment);
			return -1;
		}

		String query = "INSERT INTO Appointments (studentID, instructorID, startTime, endTime) VALUES (?,?,?,?)";

		int recID = SQLUtils.executeSqlAppointmentInsert(conn, query, appointment.getRecID(), appointment.getStudentID(), appointment.getInstructorID(), appointment.getStartTime(), appointment.getEndTime());    
		
		logger.debug("Appointment successfully inserted with ID = " + recID);
		return recID;
	}

	public Appointment retrieveByID(int recID) {
		//logger.debug("Trying to get Appointment with ID: " + recID);
		
		String query = "SELECT recID, studentID, instructorID, startTime, endTime";
		query += " FROM Appointments WHERE recID=" + recID;

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find appointment.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Appointment appointment = convertRowToAppointment(row);
		return appointment;
	}
	
	public Appointment retrieveByIndex(int index) {
		//logger.debug("Trying to get Appointment with index: " + index);
		
		index++;

		if (index < 1) {
			logger.debug("retrieveByIndex: index cannot be negative");
			return null;
		}

		String query = "SELECT * FROM Appointments ORDER BY recID LIMIT " + index;

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find appointment.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Appointment appointment = convertRowToAppointment(row);
		return appointment;
	}
	
	public List<Appointment> retrieveAll() {
		logger.debug("Getting all Appointments...");
		
		String query = "SELECT recID, studentID, instructorID, startTime, endTime";
		query += " FROM Appointments ORDER BY recID";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No appointments found!");
			return null;
		}

		List<Appointment> appointments = new ArrayList<>();
		for (SQLRow row : rows) {
			Appointment appointment = convertRowToAppointment(row);
			appointments.add(appointment);
		}
		return appointments;
	}
	
	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all Appointment IDs...");

		String query = "SELECT recID FROM Appointments ORDER BY recID";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No appointments found!");
			return null;
		}
		
		List<Integer> recIDs = new ArrayList<>();
		for (SQLRow row : rows) {
			String value = row.getItem("recID");
			int i = Integer.parseInt(value);
			recIDs.add(i);
		}
		return recIDs;
	}

	public List<Appointment> search(String keyword) {
		logger.debug("Searching for appointment with '" + keyword + "'");

		String query = "SELECT recID, instructorID, startTime, endTime FROM Appointments WHERE";
		query += " username like ?";
		query += " ORDER BY recID";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query, keyword);
		if (rows == null || rows.size() == 0) {
			logger.debug("No appointments found!");
			return null;
		}

		List<Appointment> appointments = new ArrayList<>();
		for (SQLRow row : rows) {
			Appointment appointment = convertRowToAppointment(row);
			appointments.add(appointment);
		}
		return appointments;
	}

	public boolean update(Appointment appointment) {
		throw new UnsupportedOperationException("Unable to update existing appointment in database.");
	}

	public void delete(int recID) {
		logger.debug("Trying to delete Appointment with ID: " + recID);

		String query = "DELETE FROM Appointments WHERE recID=" + recID;
		SQLUtils.executeSQL(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT count(*) as cnt FROM Appointments";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No appointments found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}

	// =====================================================================

	private Appointment convertRowToAppointment(SQLRow row) {
		logger.debug("Converting " + row + " to Appointment...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int studentID = Integer.parseInt(row.getItem("studentID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		return new Appointment(recID,studentID, instructorID, startTime, endTime);
	}
}
