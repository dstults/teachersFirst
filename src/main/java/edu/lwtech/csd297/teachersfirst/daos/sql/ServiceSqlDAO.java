package edu.lwtech.csd297.teachersfirst.daos.sql;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.DAO;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class ServiceSqlDAO implements DAO<Service> {
	
	private static final Logger logger = LogManager.getLogger(ServiceSqlDAO.class.getName());

	private Connection conn = null;

	public ServiceSqlDAO() {
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
		logger.debug("Terminating Service SQL DAO...");
		SQLUtils.disconnect(conn);
		conn = null;
	}

	public int insert(Service service) {
		logger.debug("Inserting " + service + "...");

		if (service.getRecID() != -1) {
			logger.error("Error: Cannot add previously added Service: " + service);
			return -1;
		}

		String query = "INSERT INTO services (name, description, instructors) VALUES (?,?,?);";

		int recID = SQLUtils.executeSqlInsert(conn, query, String.valueOf(service.getRecID()), service.getName(), service.getDescription(), service.getInstructors());
		
		logger.debug("Service successfully inserted with ID = " + recID);
		return recID;
	}

	public Service retrieveByID(int recID) {
		//logger.debug("Trying to get Service with ID: " + recID);
		
		String query = "SELECT * FROM services WHERE recID=" + recID + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find service.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Service service = convertRowToService(row);
		return service;
	}

	public Service retrieveByIndex(int index) {
		logger.debug("Trying to get Service with index: " + index);
		logger.warn("This will eventually be deprecated. Don't use this.");

		if (index < 0) {
			logger.debug("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM services ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find service.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Service service = convertRowToService(row);
		return service;
	}
	
	public List<Service> retrieveAll() {
		logger.debug("Getting all services...");
		
		String query = "SELECT * FROM services ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No services found!");
			return null;
		}

		List<Service> services = new ArrayList<>();
		for (SQLRow row : rows) {
			Service service = convertRowToService(row);
			services.add(service);
		}
		return services;
	}
	
	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all Service IDs...");

		String query = "SELECT recID FROM services ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No services found!");
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

	public List<Service> search(String keyword) {
		logger.debug("Searching for service with '" + keyword + "'");

		String query = "SELECT * WHERE name LIKE ? OR description LIKE ? OR instructors LIKE ? ORDER BY recID;";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, keyword);
		if (rows == null || rows.size() == 0) {
			logger.debug("No services found!");
			return null;
		}

		List<Service> services = new ArrayList<>();
		for (SQLRow row : rows) {
			Service service = convertRowToService(row);
			services.add(service);
		}
		return services;
	}

	public boolean update(Service service) {
		throw new UnsupportedOperationException("Unable to update existing service in database.");
	}

	public void delete(int recID) {
		logger.debug("Trying to delete Service with ID: " + recID);

		String query = "DELETE FROM services WHERE recID=" + recID;
		SQLUtils.executeSql(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT COUNT(*) AS cnt FROM services;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No services found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}    

	// =====================================================================

	private Service convertRowToService(SQLRow row) {
		logger.debug("Converting " + row + " to Service...");
		int recID = Integer.parseInt(row.getItem("recID"));
		String name = row.getItem("name");
		String description = row.getItem("description");
		String instructors = row.getItem("instructors");
		return new Service(recID, name, description, instructors);
	}

	private boolean integerToBoolean(int x){
		if(x == 0){
			return false;
		}else{
			return true;
		}
	}

}
