package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class OpeningMemoryDAO {
    private static final Logger logger = LogManager.getLogger(OpeningMemoryDAO.class.getName());

	private AtomicInteger nextListRecID;
	private List<Opening> openingDB;

	// ----------------------------------------------------------------

	public OpeningMemoryDAO() {
		this.nextListRecID = new AtomicInteger(1000);
		this.openingDB = new ArrayList<>();
	}

	// ----------------------------------------------------------------

	public boolean initialize(String initParams) {
		if (initParams == null)
			throw new IllegalArgumentException("init: initParams cannot be null");
		logger.debug("Initializing MemoryDAO with: '{}'", initParams);

		addDemoData();
		return true;
	}

	public void terminate() {
		logger.debug("Terminating MemoryDAO...");
		openingDB = null;
	}

	public int insert(Opening pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("insert: cannot insert null object");
		if (pojo.getRecID() != -1)
			throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
		logger.debug("Inserting " + pojo + "...");

		pojo.setRecID(nextListRecID.incrementAndGet());
		openingDB.add(pojo);

		logger.debug("Item successfully inserted!");
		return pojo.getRecID();
	}

	public Opening retrieveByID(int id) {
		if (id < 0)
			throw new IllegalArgumentException("retrieveByID: id cannot be negative");
		logger.debug("Getting object with ID: {} ...", id);

		Opening foundObject = null;
		for (Opening pojo : openingDB) {
			if (pojo.getRecID() == id) {
				foundObject = pojo;
				break;
			}
		}
		return foundObject;
	}

	public Opening retrieveByIndex(int index) {
		// Note: indexes are zero-based
		if (index < 0)
			throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
		logger.debug("Getting object with index: {} ...", index);

		return openingDB.get(index);
	}

	public List<Opening> retrieveAll() {
		logger.debug("Getting all POJOs ...");
		return new ArrayList<>(openingDB); // Return copy of DB collection
	}

	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all IDs...");

		List<Integer> listIDs = new ArrayList<>();
		for (Opening pojo : openingDB) {
			listIDs.add(pojo.getRecID());
		}
		return listIDs;
	}

	public List<Opening> search(String keyword) {
		if (keyword == null)
			throw new IllegalArgumentException("search: keyword cannot be null");
		logger.debug("Searching for objects containing: '{}'", keyword);

		keyword = keyword.toLowerCase();
		List<Opening> pojosFound = new ArrayList<>();
		for (Opening pojo : openingDB) {
			if (pojo.getName().toLowerCase().contains(keyword)) {
				pojosFound.add(pojo);
				break;
			}
		}
		logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
		return pojosFound;
	}

	public int size() {
		return openingDB.size();
	}

	public boolean update(Opening pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("update: cannot update null object");
		logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

		for (int i = 0; i < openingDB.size(); i++) {
			if (openingDB.get(i).getRecID() == pojo.getRecID()) {
				openingDB.set(i, pojo);
				logger.debug("Successfully updated: {} !", pojo.getRecID());
				return true;
			}
		}
		logger.debug("Unable to update object: {}.  RecID not found.", pojo.getRecID());
		return false;
	}

	public void delete(int id) {
		if (id < 0)
			throw new IllegalArgumentException("delete: id cannot be negative");
		logger.debug("Trying to delete object with ID: {} ...", id);

		Opening pojoFound = null;
		for (Opening pojo : openingDB) {
			if (pojo.getRecID() == id) {
				pojoFound = pojo;
				break;
			}
		}
		if (pojoFound != null) {
			openingDB.remove(pojoFound);
			logger.debug("Successfully deleted object with ID: {}", id);
		} else {
			logger.debug("Unable to delete object with ID: {}. List not found.", id);
		}
	}

	// =================================================================

	private void addDemoData() {
		logger.debug("Creating demo data...");

		insert(new Opening(2021,4,15,4,30,2021,5,15,6,30,44,"Michael"));

		logger.info(size() + " records inserted");
	}
}
