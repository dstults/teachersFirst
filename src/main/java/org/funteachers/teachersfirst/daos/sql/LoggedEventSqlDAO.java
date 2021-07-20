package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class LoggedEventSqlDAO implements DAO<LoggedEvent> {
	
	private static final Logger logger = LogManager.getLogger(LoggedEventSqlDAO.class.getName());

	private Connection conn = null;

	public LoggedEventSqlDAO() {
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
		logger.debug("Terminating LoggedEvent SQL DAO...");
		SQLUtils.disconnect(conn);
		conn = null;
	}

	public int insert(LoggedEvent loggedEvent) {
		logger.debug("Inserting " + loggedEvent + "...");

		if (loggedEvent.getRecID() != -1) {
			logger.error("Error: Cannot add previously added logged event: " + loggedEvent);
			return -1;
		}

		String query = "INSERT INTO loggedEvents (operator, date, message) VALUES (?,?,?);";

		int recID = SQLUtils.executeSqlInsert(conn, query, String.valueOf(loggedEvent.getRecID()), String.valueOf(loggedEvent.getOperator()), loggedEvent.getDate().toString(), loggedEvent.getMessage());
		
		logger.debug("Logged event successfully inserted with ID = " + recID);
		return recID;
	}

	public LoggedEvent retrieveByID(int recID) {
		//logger.debug("Trying to get logged event with ID: " + recID);
		
		String query = "SELECT * FROM loggedEvents WHERE recID=" + recID + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find logged event [" + recID + "].");
			return null;
		}
		
		SQLRow row = rows.get(0);
		LoggedEvent loggedEvent = convertRowToLoggedEvent(row);
		return loggedEvent;
	}

	public LoggedEvent retrieveByIndex(int index) {
		logger.debug("Trying to get logged event with index: " + index);

		if (index < 0) {
			logger.error("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM loggedEvents ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find LoggedEvent.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		LoggedEvent loggedEvent = convertRowToLoggedEvent(row);
		return loggedEvent;
	}
	
	public List<LoggedEvent> retrieveAll() {
		logger.debug("Getting all logged events...");
		
		String query = "SELECT * FROM loggedEvents ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No logged events found!");
			return null;
		}

		List<LoggedEvent> loggedEvents = new ArrayList<>();
		for (SQLRow row : rows) {
			LoggedEvent loggedEvent = convertRowToLoggedEvent(row);
			loggedEvents.add(loggedEvent);
		}
		return loggedEvents;
	}
	
	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all LoggedEvent IDs...");

		String query = "SELECT recID FROM loggedEvents ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No logged events found!");
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

	public List<LoggedEvent> search(String keyword) {
		logger.debug("Searching for LoggedEvent with '" + keyword + "'");

		String query = "SELECT * WHERE name LIKE ? OR description LIKE ? OR instructors LIKE ? ORDER BY recID;";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, keyword);
		if (rows == null || rows.size() == 0) {
			logger.debug("No logged events found!");
			return null;
		}

		List<LoggedEvent> loggedEvents = new ArrayList<>();
		for (SQLRow row : rows) {
			LoggedEvent loggedEvent = convertRowToLoggedEvent(row);
			loggedEvents.add(loggedEvent);
		}
		return loggedEvents;
	}

	public boolean update(LoggedEvent loggedEvent) {
		throw new UnsupportedOperationException("LoggedEvent data cannot be modified.");
	}

	public void delete(int recID) {
		throw new UnsupportedOperationException("LoggedEvent data cannot be deleted.");
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT COUNT(*) AS cnt FROM loggedEvents;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No logged events found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}    

	// =====================================================================

	private LoggedEvent convertRowToLoggedEvent(SQLRow row) {
		//logger.debug("Converting " + row + " to Logged Event...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int operator = Integer.parseInt(row.getItem("operator"));
		Timestamp date = DateHelpers.fromSqlDateToTimestamp(row.getItem("date"));
		String message = row.getItem("message");
		return new LoggedEvent(recID, operator, date, message);
	}

}
