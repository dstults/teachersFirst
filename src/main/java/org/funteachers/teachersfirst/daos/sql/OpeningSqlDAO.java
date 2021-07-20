package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.DateHelpers;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class OpeningSqlDAO implements DAO<Opening> {
	
	private static final Logger logger = LogManager.getLogger(OpeningSqlDAO.class.getName());

	private Connection conn = null;

	public OpeningSqlDAO() {
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
		logger.debug("Terminating Opening SQL DAO...");
		SQLUtils.disconnect(conn);
		conn = null;
	}

	public int insert(Opening opening) {
		logger.debug("Inserting " + opening + "...");

		if (opening.getRecID() != -1) {
			logger.error("Error: Cannot add previously added Opening: " + opening);
			return -1;
		}

		String query = "INSERT INTO openings (instructorID, startTime, endTime) VALUES (?,?,?);";

		int recID = SQLUtils.executeSqlOpeningInsert(conn, query, opening.getRecID(), opening.getInstructorID(), opening.getStartTime(), opening.getEndTime());    
		
		logger.debug("Opening successfully inserted with ID = " + recID);
		return recID;
	}

	public Opening retrieveByID(int recID) {
		//logger.debug("Trying to get Opening with ID: " + recID);
		
		String query = "SELECT * FROM openings WHERE recID=" + recID + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find opening.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Opening opening = convertRowToOpening(row);
		return opening;
	}
	
	public Opening retrieveByIndex(int index) {
		logger.debug("Trying to get Opening with index: " + index);
		logger.warn("This will eventually be deprecated. Don't use this.");

		if (index < 0) {
			logger.debug("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM openings ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find opening.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Opening opening = convertRowToOpening(row);
		return opening;
	}
	
	public List<Opening> retrieveAll() {
		logger.debug("Getting all openings...");
		
		String query = "SELECT * FROM openings ORDER BY startTime;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No openings found!");
			return null;
		}

		List<Opening> openings = new ArrayList<>();
		for (SQLRow row : rows) {
			Opening opening = convertRowToOpening(row);
			openings.add(opening);
		}
		return openings;
	}
	
	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all Opening IDs...");

		String query = "SELECT recID FROM openings ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No openings found!");
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

	public List<Opening> search(String keyword) {
		logger.debug("Searching for opening with '" + keyword + "'");

		String query = "SELECT * FROM openings WHERE userName LIKE ? ORDER BY recID;";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, keyword);
		if (rows == null || rows.size() == 0) {
			logger.debug("No openings found!");
			return null;
		}

		List<Opening> openings = new ArrayList<>();
		for (SQLRow row : rows) {
			Opening opening = convertRowToOpening(row);
			openings.add(opening);
		}
		return openings;
	}

	public boolean update(Opening opening) {
		throw new UnsupportedOperationException("Unable to update existing opening in database.");
	}

	public void delete(int recID) {
		logger.debug("Trying to delete Opening with ID: " + recID);

		String query = "DELETE FROM openings WHERE recID=" + recID;
		SQLUtils.executeSql(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT COUNT(*) AS cnt FROM openings;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No openings found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}    

	// =====================================================================

	private Opening convertRowToOpening(SQLRow row) {
		//logger.debug("Converting " + row + " to Opening...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		return new Opening(recID, instructorID, startTime, endTime);
	}

}