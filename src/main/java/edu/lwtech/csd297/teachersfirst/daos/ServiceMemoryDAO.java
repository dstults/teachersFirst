package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

// Memory-based DAO class - stores objects in a List.  No persistance.

public class ServiceMemoryDAO implements DAO<Service> {

	private static final Logger logger = LogManager.getLogger(ServiceMemoryDAO.class.getName());

	private AtomicInteger nextListRecID;
	private List<Service> serviceDB;

	// ----------------------------------------------------------------

	public ServiceMemoryDAO() {
		this.nextListRecID = new AtomicInteger(1000);
		this.serviceDB = new ArrayList<>();
	}

	// ----------------------------------------------------------------

	public boolean initialize(String initParams) {
		if (initParams == null)
			throw new IllegalArgumentException("init: initParams cannot be null");
		logger.info("Initializing MemoryDAO with: '{}'", initParams);

		addDemoData();
		return true;
	}

	public void terminate() {
		logger.debug("Terminating Service Memory DAO...");
		this.serviceDB = null;
	}

	public int insert(Service pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("insert: cannot insert null object");
		if (pojo.getRecID() != -1)
			throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
		//logger.debug("Inserting " + pojo + "...");

		pojo.setRecID(this.nextListRecID.incrementAndGet());
		this.serviceDB.add(pojo);

		//logger.debug("Item successfully inserted!");
		return pojo.getRecID();
	}

	public Service retrieveByID(int id) {
		if (id < 0)
			throw new IllegalArgumentException("retrieveByID: id cannot be negative");
		logger.debug("Getting object with ID: {} ...", id);

		Service foundObject = null;
		for (Service pojo : this.serviceDB) {
			if (pojo.getRecID() == id) {
				foundObject = pojo;
				break;
			}
		}
		return foundObject;
	}

	public Service retrieveByIndex(int index) {
		// Note: indexes are zero-based
		if (index < 0)
			throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
		logger.debug("Getting object with index: {} ...", index);

		return this.serviceDB.get(index);
	}

	public List<Service> retrieveAll() {
		logger.debug("Getting all POJOs ...");
		return new ArrayList<>(this.serviceDB); // Return copy of DB collection
	}

	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all IDs...");

		List<Integer> listIDs = new ArrayList<>();
		for (Service pojo : this.serviceDB) {
			listIDs.add(pojo.getRecID());
		}
		return listIDs;
	}

	public List<Service> search(String keyword) {
		if (keyword == null)
			throw new IllegalArgumentException("search: keyword cannot be null");
		logger.debug("Searching for objects containing: '{}'", keyword);

		keyword = keyword.toLowerCase();
		List<Service> pojosFound = new ArrayList<>();
		for (Service pojo : this.serviceDB) {
			if (pojo.getName().toLowerCase().contains(keyword)) {
				pojosFound.add(pojo);
				break;
			}
		}
		//logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
		return pojosFound;
	}

	public int size() {
		return this.serviceDB.size();
	}

	public boolean update(Service pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("update: cannot update null object");
		logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

		for (int i = 0; i < this.serviceDB.size(); i++) {
			if (this.serviceDB.get(i).getRecID() == pojo.getRecID()) {
				this.serviceDB.set(i, pojo);
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

		Service pojoFound = null;
		for (Service pojo : serviceDB) {
			if (pojo.getRecID() == id) {
				pojoFound = pojo;
				break;
			}
		}
		if (pojoFound != null) {
			this.serviceDB.remove(pojoFound);
			logger.debug("Successfully deleted object with ID: {}", id);
		} else {
			logger.debug("Unable to delete object with ID: {}. List not found.", id);
		}
	}

	// =================================================================

	private void addDemoData() {
		//logger.debug("Creating demo data...");

		this.insert(new Service("Pilates", "Pilates is a method of exercise that consists of low-impact flexibility and muscular strength and endurance movements. Use it to stay super trim!", "Fred, Edmund"));
		this.insert(new Service("Phonetics", "Learn how to make all the sounds with your mouth and talk like a charismatic expert! IPA is your friend!", "Darren"));
		this.insert(new Service("Database Repair", "Fix all the errors in your SQL, learn how to avoid them in the future, never get escaped again!", "Edmund, Tanya"));
		this.insert(new Service("Extreme E-Biking", "Pretend you're biking when you're really not! Hear someone yell at you in a crowded room from the spacious comfort of your home!", "Fred, Darren"));

		logger.info(this.size() + " records inserted");
	}

}
