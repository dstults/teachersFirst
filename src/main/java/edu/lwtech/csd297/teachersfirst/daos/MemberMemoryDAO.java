package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

// Memory-based DAO class - stores objects in a List.  No persistance.

public class MemberMemoryDAO implements DAO<Member> {

	private static final Logger logger = LogManager.getLogger(MemberMemoryDAO.class.getName());

	private AtomicInteger nextListRecID;
	private List<Member> studentDB;

	// ----------------------------------------------------------------

	public MemberMemoryDAO() {
		this.nextListRecID = new AtomicInteger(1000);
		this.studentDB = new ArrayList<>();
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
		studentDB = null;
	}

	public int insert(Member pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("insert: cannot insert null object");
		if (pojo.getRecID() != -1)
			throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
		logger.debug("Inserting " + pojo + "...");

		pojo.setRecID(nextListRecID.incrementAndGet());
		studentDB.add(pojo);

		logger.debug("Item successfully inserted!");
		return pojo.getRecID();
	}

	public Member retrieveByID(int id) {
		if (id < 0)
			throw new IllegalArgumentException("retrieveByID: id cannot be negative");
		logger.debug("Getting object with ID: {} ...", id);

		Member foundObject = null;
		for (Member pojo : studentDB) {
			if (pojo.getRecID() == id) {
				foundObject = pojo;
				break;
			}
		}
		return foundObject;
	}

	public Member retrieveByIndex(int index) {
		// Note: indexes are zero-based
		if (index < 0)
			throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
		logger.debug("Getting object with index: {} ...", index);

		return studentDB.get(index);
	}

	public List<Member> retrieveAll() {
		logger.debug("Getting all POJOs ...");
		return new ArrayList<>(studentDB); // Return copy of DB collection
	}

	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all IDs...");

		List<Integer> listIDs = new ArrayList<>();
		for (Member pojo : studentDB) {
			listIDs.add(pojo.getRecID());
		}
		return listIDs;
	}

	public List<Member> search(String keyword) {
		if (keyword == null)
			throw new IllegalArgumentException("search: keyword cannot be null");
		logger.debug("Searching for objects containing: '{}'", keyword);

		keyword = keyword.toLowerCase();
		List<Member> pojosFound = new ArrayList<>();
		for (Member pojo : studentDB) {
			if (pojo.getName().toLowerCase().contains(keyword)) {
				pojosFound.add(pojo);
				break;
			}
		}
		logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
		return pojosFound;
	}

	public int size() {
		return studentDB.size();
	}

	public boolean update(Member pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("update: cannot update null object");
		logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

		for (int i = 0; i < studentDB.size(); i++) {
			if (studentDB.get(i).getRecID() == pojo.getRecID()) {
				studentDB.set(i, pojo);
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

		Member pojoFound = null;
		for (Member pojo : studentDB) {
			if (pojo.getRecID() == id) {
				pojoFound = pojo;
				break;
			}
		}
		if (pojoFound != null) {
			studentDB.remove(pojoFound);
			logger.debug("Successfully deleted object with ID: {}", id);
		} else {
			logger.debug("Unable to delete object with ID: {}. List not found.", id);
		}
	}

	// =================================================================

	private void addDemoData() {
		logger.debug("Creating demo data...");

		insert(new Member("Fred", 33, "female", "maroon", "rabbit meat", false, false, true));
		insert(new Member("Darren", 66, "male", "orange", "macaroni & cheese", false, true, true));
		insert(new Member("Tanya", 43, "female", "black", "pizza", true, true, false));
		insert(new Member("Edmund", 22, "male", "blue", "cheeseburgers", true, false, true));

		logger.info(size() + " records inserted");
	}

}
