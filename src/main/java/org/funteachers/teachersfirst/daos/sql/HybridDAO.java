package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.managers.DateHelpers;
import org.funteachers.teachersfirst.obj.*;

public class HybridDAO {
		
	private static final Logger logger = LogManager.getLogger();

	private Connection conn;

	public HybridDAO(Connection conn) {
		if (conn == null) throw new IllegalArgumentException("DAO instantiated without connection.");

		this.conn = conn;
	}
	
	public List<Appointment> getAppointmentsWithMemberNames() {
		logger.debug("Appointments SELECT [ * ] ...");
		
		String query = "SELECT a.recID, a.studentID, m1.displayName AS 'studentName', a.instructorID, m2.displayName AS 'instructorName', " +
						"a.startTime, a.endTime, a.schedulingVerified, a.completionState " +
						"FROM appointments a " +
						"LEFT JOIN members m1 ON a.studentID = m1.recID " +
						"LEFT JOIN members m2 ON a.instructorID = m2.recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No appointments found!");
			return null;
		}

		List<Appointment> appointments = new ArrayList<>();
		for (SQLRow row : rows) {
			Appointment appointment = convertRowToPrettifiedAppointment(row);
			appointments.add(appointment);
		}
		return appointments;
	}

	public List<Appointment> getAppointmentsWithMemberNamesBetweenDatetimes(LocalDateTime start, LocalDateTime end) {
		final String startStringSql = DateHelpers.toSqlDatetimeString(start);
		final String endStringSql = DateHelpers.toSqlDatetimeString(end);
		return getAppointmentsWithMemberNamesBetweenDatetimes(startStringSql, endStringSql);
	}

	public List<Appointment> getAppointmentsWithMemberNamesBetweenDatetimes(String startStringSql, String endStringSql) {
		logger.debug("Appointments SELECT [ * ] ...");
		
		String query = "SELECT a.recID, a.studentID, m1.displayName AS 'studentName', a.instructorID, m2.displayName AS 'instructorName', " +
						"a.startTime, a.endTime, a.schedulingVerified, a.completionState " +
						"FROM appointments a " +
						"LEFT JOIN members m1 ON a.studentID = m1.recID " +
						"LEFT JOIN members m2 ON a.instructorID = m2.recID " +
						"WHERE startTime >= ? AND endTime <= ? " +
						"ORDER BY startTime, instructorID, endTime;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, startStringSql, endStringSql);
		if (rows == null || rows.size() == 0) {
			logger.debug("No appointments found!");
			return null;
		}

		List<Appointment> appointments = new ArrayList<>();
		for (SQLRow row : rows) {
			Appointment appointment = convertRowToPrettifiedAppointment(row);
			appointments.add(appointment);
		}
		return appointments;
	}

	private Appointment convertRowToPrettifiedAppointment(SQLRow row) {
		//logger.debug("Converting " + row + " to Appointment...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int studentID = Integer.parseInt(row.getItem("studentID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		Boolean schedulingVerified = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("schedulingVerified")));
		int completionState = Integer.parseInt(row.getItem("completionState"));
		Appointment appointment = new Appointment(recID, studentID, instructorID, startTime, endTime, schedulingVerified, completionState);
		appointment.setInstructorName(row.getItem("instructorName"));
		appointment.setStudentName(row.getItem("studentName"));
		return appointment;
	}
}
