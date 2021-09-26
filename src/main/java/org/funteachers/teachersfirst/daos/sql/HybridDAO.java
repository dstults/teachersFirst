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
		
		String query = "SELECT * FROM appointments ORDER BY startTime, instructorID, endTime;";

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

	
	private Appointment convertRowToPrettifiedAppointment(SQLRow row) {
		//logger.debug("Converting " + row + " to Appointment...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int studentID = Integer.parseInt(row.getItem("studentID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		Boolean schedulingVerified = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("schedulingVerified")));
		int completionState = Integer.parseInt(row.getItem("completionState"));
		Appointment appointment = new Appointment(recID,studentID, instructorID, startTime, endTime, schedulingVerified, completionState);
		appointment.setInstructorName("Bobbery");
		appointment.setStudentName("Bobbery");
		return appointment;
	}
}
