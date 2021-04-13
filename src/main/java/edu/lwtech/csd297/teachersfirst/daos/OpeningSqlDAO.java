package edu.lwtech.csd297.teachersfirst.daos;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.DateHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

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
			logger.error("Attempting to add previously added Opening: " + opening);
			return -1;
		}

		String query = "INSERT INTO Openings (instructorID, startTime, endTime) VALUES (?,?,?)";

		int recID = SQLUtils.executeSqlOpeningInsert(conn, query, opening.getRecID(), opening.getInstructorID(), opening.getStartTime(), opening.getEndTime());    
		
		logger.debug("Opening successfully inserted with ID = " + recID);
		return recID;
	}

	public Opening retrieveByID(int recID) {
		//logger.debug("Trying to get Opening with ID: " + recID);
		
		String query = "SELECT recID, instructorID, startTime, endTime";
		query += " FROM Openings WHERE recID=" + recID;

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find opening.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Opening opening = convertRowToOpening(row);
		return opening;
	}
	
	public Opening retrieveByIndex(int index) {
		//logger.debug("Trying to get Opening with index: " + index);
		
		index++;

		if (index < 1) {
			logger.debug("retrieveByIndex: index cannot be negative");
			return null;
		}

		String query = "SELECT recID, instructorID, startTime, endTime";
		query += " FROM Openings ORDER BY recID LIMIT " + index;

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find opening.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Opening opening = convertRowToOpening(row);
		return opening;
	}
	
	public List<Opening> retrieveAll() {
		logger.debug("Getting all Openings...");
		
		String query = "SELECT recID, instructorID, startTime, endTime";
		query += " FROM Openings ORDER BY recID";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
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

		String query = "SELECT recID FROM Openings ORDER BY recID";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
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

		String query = "SELECT recID, instructorID, startTime, endTime FROM Openings WHERE";
		query += " username like ?";
		query += " ORDER BY recID";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query, keyword);
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

		String query = "DELETE FROM Openings WHERE recID=" + recID;
		SQLUtils.executeSQL(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT count(*) as cnt FROM Openings";

		List<SQLRow> rows = SQLUtils.executeSQL(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No openings found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}    

	// =====================================================================

	private Opening convertRowToOpening(SQLRow row) {
		logger.debug("Converting " + row + " to Opening...");
		int recID = Integer.parseInt(row.getItem("recID"));
		int instructorID = Integer.parseInt(row.getItem("instructorID"));
		Timestamp startTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("startTime"));
		Timestamp endTime = DateHelpers.fromSqlDatetimeToTimestamp(row.getItem("endTime"));
		return new Opening(recID, instructorID, startTime, endTime);
	}

}
