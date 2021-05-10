package edu.lwtech.csd297.teachersfirst.daos;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;

class SQLUtils {

	private static final Logger logger = LogManager.getLogger(SQLUtils.class.getName());

	private SQLUtils() { }                                          // Hide the implicit public constructor

	public static Connection connect(String initParams) {
		logger.debug("Connecting to " + initParams + "...");

		String driverClass = "org.mariadb.jdbc.Driver";
		try {
			Class.forName(driverClass);                             // Dynamically loads the driver from the WAR file
		} catch (ClassNotFoundException e) {
			logger.error("Unable to find JDBC driver on classpath: " + driverClass , e);
			return null;
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(initParams);
		} catch (SQLException e) {
			logger.error("Unable to connect to SQL Database with: " + initParams, e);
			return null;
		}

		logger.debug("Connected!");
		return conn;
	}

	public static List<SQLRow> executeSql(Connection conn, String query, String... arguments) {
		//logger.debug("Executing SQL statement: " + query);

		try {
			// Create the new statement object
			if (conn == null) {
				logger.error("Lost connection to SQL database, attempted query: {}", query);
				return null;
			}
			PreparedStatement stmt = conn.prepareStatement(query);

			// Substitute in the argument values for the question marks
			int position = 1;
			for (String arg : arguments) {
				stmt.setString(position++, arg);
			}

			query = query.toLowerCase();
			if (query.contains("update ") || query.contains("delete ")) {

				int numRows = stmt.executeUpdate();
				return results(numRows);

			} else if (query.contains("select ")) {

				// Execute the SELECT query
				ResultSet sqlResults = stmt.executeQuery();

				// Get the column names
				ResultSetMetaData md = sqlResults.getMetaData();
				List<String> columns = new ArrayList<>();
				for (int i=0; i < md.getColumnCount(); i++) {
					columns.add(md.getColumnName(i+1));
				}

				// Store each row in a List
				List<SQLRow> rows = new ArrayList<>();
				while (sqlResults.next()) {
					SQLRow row = new SQLRow(columns, sqlResults);
					//logger.debug(row.toString());
					rows.add(row);
				}

				return rows;
			}
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSql: " + query, e);
			return null;
		}
		return null;
	}

	// Default Insert
	public static int executeSqlInsert(Connection conn, String query, String recID, String... arguments) {
		//logger.debug("Executing SQL Insert: " + query);

		int newID = -1;
		String[] returnColumns = new String[] { recID };

		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);

			int position = 1;
			for (String arg : arguments)
				stmt.setString(position++, arg);

			stmt.executeUpdate();
			
			// Get the new recID value from the query results and return it to the caller
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			newID = keys.getInt(1);
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlInsert: " + query, e);
			return -1;
		}

		return newID;
	}


	// Member Insert
	public static int executeSqlMemberInsert(Connection conn,
			String query,
			int recID, String loginName, String passwordHash,
			String displayName, float credits,
			Timestamp birthdate, String gender,
			String selfIntroduction, String instructorNotes,
			String phone1, String phone2, String email,
			boolean isStudent, boolean isInstructor, boolean isAdmin) {

		//logger.debug("Executing SQL Insert: " + query);

		int newID = -1;
		String[] returnColumns = new String[] { String.valueOf(recID) };
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);
			
			stmt.setString(1, loginName);
			stmt.setString(2, passwordHash);
			stmt.setString(3, displayName);
			stmt.setFloat(4, credits);
			stmt.setTimestamp(5, birthdate);
			stmt.setString(6, gender);
			stmt.setString(7, selfIntroduction);
			stmt.setString(8, instructorNotes);
			stmt.setString(9, phone1);
			stmt.setString(10, phone2);
			stmt.setString(11, email);
			stmt.setInt(12, isStudent ? 1 : 0);
			stmt.setInt(13, isInstructor ? 1 : 0);
			stmt.setInt(14, isAdmin ? 1 : 0);
			
			stmt.executeUpdate();
			
			// Get the new recID value from the query results and return it to the caller
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			newID = keys.getInt(1);
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlInsert: " + query, e);
			return -1;
		}

		return newID;
	}


	//Opening Insert
	public static int executeSqlOpeningInsert(Connection conn, String query, int recID, int instructorID,
			Timestamp startTime, Timestamp endTime) {
		//logger.debug("Executing SQL Insert: " + query);

		int newID = -1;
		String[] returnColumns = new String[] { String.valueOf(recID) };
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);

			stmt.setInt(1, instructorID);
			stmt.setTimestamp(2, startTime);
			stmt.setTimestamp(3, endTime);
			
			stmt.executeUpdate();
			
			// Get the new recID value from the query results and return it to the caller
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			newID = keys.getInt(1);
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlInsert: " + query, e);
			return -1;
		}

		return newID;
	}

	// Appointment Insert
	public static int executeSqlAppointmentInsert(Connection conn, String query, int recID, int studentID, int instructorID,
			Timestamp startTime, Timestamp endTime, boolean schedulingVerified, int completionState) {
		//logger.debug("Executing SQL Insert: " + query);
		
		int newID = -1;
		String[] returnColumns = new String[] { String.valueOf(recID) };
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);
			
			stmt.setInt(1, studentID);
			stmt.setInt(2, instructorID);
			stmt.setTimestamp(3, startTime);
			stmt.setTimestamp(4, endTime);
			stmt.setBoolean(5, schedulingVerified);
			stmt.setInt(6, completionState);
			
			stmt.executeUpdate();
			
			// Get the new recID value from the query results and return it to the caller
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			newID = keys.getInt(1);
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlInsert: " + query, e);
			return -1;
		}

		return newID;
	}

	// Appointment Insert
	public static boolean executeSqlAppointmentUpdate(Connection conn, String query, boolean schedulingVerified, int completionState) {
		logger.debug("Executing SQL Update to Appointment: " + query);
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			
			stmt.setBoolean(1, schedulingVerified);
			stmt.setInt(2, completionState);

			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlInsert: " + query, e);
			return false;
		}
	}

	public static void disconnect(Connection conn) {
		if (conn == null) {
			// This happens during test runs and that's a good thing. But there won't be a logger to use.
			// Also we REALLY want to log this when it happens when an actual servlet is running.
			if (logger != null) logger.error("CONNECTION WAS NULL WHEN ATTEMPTING TO DISCONNECT.");
			return;
		}

		boolean wasClosedOiginally = false;
		try {
			if (conn.isClosed()) {
				wasClosedOiginally = true;
				String warningMessage = "Warning: Connection was already disconnected while attempting to disconnect.";
				System.out.println(warningMessage);
				if (logger != null) logger.error(warningMessage);
				return;
			}
			conn.close();
		} catch (SQLException e) {
			if (!wasClosedOiginally) {
				// In case happens during test builds or when logger is null:
				System.out.println("================================================ Error");
				System.out.println("| SQL EXCEPTION WHILE ATTEMPTING TO DISCONNECT | Error" + e.getStackTrace());
				System.out.println("================================================ Error");
				// Normal logging might not always work here:
				if (logger != null) logger.error("Exception thrown while trying to close SQL Connection", e);
			}
		}
}

	// ===============================================================================================

	private static List<SQLRow> results(int i) {
		List<SQLRow> rows = new ArrayList<>();
		rows.add(new SQLRow("Value", i));
		return rows;
	}

	
}