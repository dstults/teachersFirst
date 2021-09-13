package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

public class SQLUtils {

	private static final Logger logger = LogManager.getLogger();

	public static Connection connect(String initParams) {
		// This has been turned off because it's a security risk (the password is in the initParams)
		//logger.debug("Connecting to " + initParams + "...");
		logger.debug("Connecting to database...");

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
			// Security risk (the password is in the initParams)
			//logger.error("Unable to connect to SQL Database with: " + initParams, e);
			logger.error("Unable to connect to SQL Database."); // , e);  // This is way too verbose, esp. when it's such a clear error.
			return null;
		}

		//logger.debug("Connected to database!");
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
	public static int executeSqlInsert(Connection conn, String query, String recID, String... args) {
		if (conn == null) {
			logger.error("No connection passed to executeSqlInsert: " + query + " -- recID: " + recID);
			return -1;
		}
		//logger.debug("Executing SQL Insert: " + query);

		int newID = -1;
		String[] returnColumns = new String[] { recID };

		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);

			int position = 1;
			for (String arg : args)
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
			int recID, String loginName,
			String displayName, float credits,
			Timestamp birthdate, String gender,
			String selfIntroduction, String instructorNotes,
			String phone1, String phone2, String email,
			boolean isAdmin, boolean isInstructor, boolean isStudent) {

		//logger.debug("Executing SQL Insert: " + query);

		int newID = -1;
		String[] returnColumns = new String[] { String.valueOf(recID) };
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query, returnColumns);
			
			stmt.setString(1, loginName);
			stmt.setString(2, displayName);
			stmt.setFloat(3, credits);
			stmt.setTimestamp(4, birthdate);
			stmt.setString(5, gender);
			stmt.setString(6, selfIntroduction);
			stmt.setString(7, instructorNotes);
			stmt.setString(8, phone1);
			stmt.setString(9, phone2);
			stmt.setString(10, email);
			stmt.setInt(11, isAdmin ? 1 : 0);         // SetBoolean doesn't work
			stmt.setInt(12, isInstructor ? 1 : 0);    // SetBoolean doesn't work
			stmt.setInt(13, isStudent ? 1 : 0);       // SetBoolean doesn't work
			
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
			stmt.setInt(5, schedulingVerified ? 1 : 0); // SetBoolean doesn't work
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

	// Generic Update
	public static boolean executeSqlUpdate(Connection conn, String query, String... args) {
		logger.debug("Executing SQL Update: " + query);
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			
			for (int i = 0; i <= args.length - 1; i++) {
				if (args[i] == null) {
					stmt.setNull(i + 1, Types.CHAR);
				} else {
					stmt.setString(i + 1, args[i]);
				}
			}
			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlUpdate: " + query, e);
			return false;
		}
	}

	//TODO: Switch to generic update
	// Appointment Update
	public static boolean executeSqlAppointmentUpdate(Connection conn, String query, boolean schedulingVerified, int completionState) {
		logger.debug("Executing SQL Update to Appointment: " + query);
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			
			stmt.setBoolean(1, schedulingVerified);
			stmt.setInt(2, completionState);

			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			logger.error("SQL Exception caught in executeSqlAppointmentUpdate: " + query, e);
			return false;
		}
	}

	public static void disconnect(Connection conn) {
		if (conn == null) return;

		boolean wasClosedOriginally = false;
		try {
			if (conn.isClosed()) {
				wasClosedOriginally = true;
				String warningMessage = "Warning: Connection was already disconnected while attempting to disconnect.";
				System.out.println(warningMessage);
				if (logger != null) logger.error(warningMessage);
				return;
			}
			conn.close();
		} catch (SQLException e) {
			if (!wasClosedOriginally) {
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

	public static boolean integerToBoolean(int value){
		if(value == 0){
			return false;
		}else{
			return true;
		}
	}
	
}