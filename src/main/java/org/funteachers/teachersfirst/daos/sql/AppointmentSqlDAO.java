package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.managers.DateHelpers;
import org.funteachers.teachersfirst.obj.*;

public class AppointmentSqlDAO implements DAO<Appointment> {
	
	private static final Logger logger = LogManager.getLogger();

	private Connection conn;

	public AppointmentSqlDAO(Connection conn) {
		if (conn == null) throw new IllegalArgumentException("DAO instantiated without connection.");

		this.conn = conn;
	}

	public int insert(Appointment appointment) {
		logger.debug("Appointment INSERT [DT: '{}' ] ...", appointment.getDateFormatted() + " " + appointment.getStartTimeFormatted());

		if (appointment.getRecID() != -1) {
			logger.error("Error: Cannot add previously added Appointment: " + appointment);
			return -1;
		}

		String query = "INSERT INTO appointments (studentID, instructorID, startTime, endTime, schedulingVerified, completionState) VALUES (?,?,?,?,?,?);";

		int recID = SQLUtils.executeSqlAppointmentInsert(conn, query, appointment.getRecID(), appointment.getStudentID(), appointment.getInstructorID(), appointment.getStartTime(), appointment.getEndTime(), appointment.getSchedulingVerified(), appointment.getCompletionState());    
		
		logger.debug("Appointment INSERT ... [ID: {} ]", recID);
		return recID;
	}

	public boolean update(Appointment appointment) {
		if (appointment.getRecID() <= 0) throw new IllegalArgumentException("Illegal Argument: cannot update appointment with recID <= 0");

		String query = "UPDATE appointments SET schedulingVerified = ?, completionState = ? WHERE recID = " + appointment.getRecID() + ";";

		boolean success = SQLUtils.executeSqlAppointmentUpdate(conn, query, appointment.getSchedulingVerified(), appointment.getCompletionState());    

		if (success)
			logger.debug("Appointment " + appointment.getRecID() + " successfully updated");
		else
			logger.error("!! Appointment " + appointment.getRecID() + " failed to updated !!");
		
		return success;
	}

	public Appointment retrieveByID(int recID) {
		//logger.debug("Trying to get Appointment with ID: " + recID);
		
		String query = "SELECT * FROM appointments WHERE recID=" + recID + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find appointment.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Appointment appointment = convertRowToAppointment(row);
		return appointment;
	}
	
	public Appointment retrieveByIndex(int index) {
		logger.debug("Trying to get Appointment with index: " + index);
		
		if (index < 0) {
			logger.error("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM appointments ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find appointment.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Appointment appointment = convertRowToAppointment(row);
		return appointment;
	}
	
	public List<Appointment> retrieveAll() {
		logger.debug("Appointments SELECT [ * ] ...");
		
		String query = "SELECT * FROM appointments ORDER BY startTime, instructorID, endTime;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
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
	
	public List<Appointment> retrieveAllBetweenDatetimeAndDatetime(LocalDateTime start, LocalDateTime end) {
		final String startStringSql = DateHelpers.toSqlDatetimeString(start);
		final String endStringSql = DateHelpers.toSqlDatetimeString(end);
		logger.debug("Appointments SELECT [{}]-[{}] ...", startStringSql, endStringSql);
		
		String query = "SELECT * FROM appointments WHERE startTime >= ? AND endTime <= ? ORDER BY startTime, instructorID, endTime;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, startStringSql, endStringSql);
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
	
	public List<Appointment> getConflictsBetweenDatetimes(int uid1, int uid2, String start, String end) {
		logger.debug("App LJ Mem2 SELECT [ * @ ID-ID @ Dt-Dt ] ...");
		String u1s = String.valueOf(uid1);
		String u2s = String.valueOf(uid2);

		// Returning values of zero are a security measure, this is to prevent leaking information
		// as conflict returns are not associated with names, yet can be seen by people who otherwise
		// wouldn't be able to see relevant appointment information.
		String query = "SELECT recID, 0 AS 'studentID', 0 AS 'instructorID', startTime, endTime, 0 AS 'schedulingVerified', 0 AS 'completionState' " +
							"FROM appointments " +
							"WHERE (startTime >= ? AND startTime < ? OR endTime > ? AND endTime <= ? " +
								"OR startTime < ? AND endTime > ?) " + // Catches case where an appointment completely envelopes the range
								"AND (instructorID = ? OR instructorID = ? OR studentID = ? OR studentID = ?) " +
								"AND completionState <> 2 " + // Cancelled appointments don't conflict
							"ORDER BY startTime, endTime;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, start, end, start, start, start, end, u1s, u2s, u1s, u2s);
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
		logger.debug("Appointments SELECT [*:id] ...");

		String query = "SELECT recID FROM appointments ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
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

		String query = "SELECT * FROM appointments WHERE userName LIKE ? ORDER BY recID;";
		keyword = "%" + keyword + "%";
		List<SQLRow> rows = SQLUtils.executeSql(conn, query, keyword);
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

	public void delete(int recID) {
		logger.debug("Trying to delete Appointment with ID: " + recID);

		String query = "DELETE FROM appointments WHERE recID=" + recID;
		SQLUtils.executeSql(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT COUNT(*) AS cnt FROM appointments;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No appointments found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}

	// =====================================================================

	private Appointment convertRowToAppointment(SQLRow row) {
		//logger.debug("Converting " + row + " to Appointment...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int studentID = Integer.parseInt(row.getItem("studentID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		Boolean schedulingVerified = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("schedulingVerified")));
		int completionState = Integer.parseInt(row.getItem("completionState"));
		return new Appointment(recID, studentID, instructorID, startTime, endTime, schedulingVerified, completionState);
	}
}
