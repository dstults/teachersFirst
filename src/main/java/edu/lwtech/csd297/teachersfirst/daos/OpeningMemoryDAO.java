package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.DataManager;
import edu.lwtech.csd297.teachersfirst.DateHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class OpeningMemoryDAO implements DAO<Opening> {
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
		logger.debug("Terminating Opening Memory DAO...");
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
		//logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
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

		List<Member> members = DataManager.getMemberDAO().retrieveAll();

		insert(new Opening(members.get(1).getRecID(), DateHelpers.toTimestamp("2021/03/16 07:00:00"), DateHelpers.toTimestamp("2021/03/16 10:00:00")));
		insert(new Opening(members.get(3).getRecID(), DateHelpers.toTimestamp("2021/03/17 11:00:00"), DateHelpers.toTimestamp("2021/03/17 14:00:00")));
		insert(new Opening(members.get(0).getRecID(), DateHelpers.toTimestamp("2021/03/17 11:00:00"), DateHelpers.toTimestamp("2021/03/17 15:00:00")));
		insert(new Opening(members.get(0).getRecID(), DateHelpers.toTimestamp("2021/03/19 06:00:00"), DateHelpers.toTimestamp("2021/03/19 18:00:00")));
		insert(new Opening(members.get(1).getRecID(), DateHelpers.toTimestamp("2021/03/19 07:00:00"), DateHelpers.toTimestamp("2021/03/19 17:00:00")));
		insert(new Opening(members.get(2).getRecID(), DateHelpers.toTimestamp("2021/03/19 23:00:00"), DateHelpers.toTimestamp("2021/03/19 06:00:00")));
		insert(new Opening(members.get(2).getRecID(), DateHelpers.toTimestamp("2021/03/20 02:00:00"), DateHelpers.toTimestamp("2021/03/20 08:00:00")));
		insert(new Opening(members.get(3).getRecID(), DateHelpers.toTimestamp("2021/03/20 16:00:00"), DateHelpers.toTimestamp("2021/03/20 00:00:00")));
		insert(new Opening(members.get(2).getRecID(), DateHelpers.toTimestamp("2021/03/23 09:00:00"), DateHelpers.toTimestamp("2021/03/23 18:00:00")));
		insert(new Opening(members.get(1).getRecID(), DateHelpers.toTimestamp("2021/03/24 11:00:00"), DateHelpers.toTimestamp("2021/03/24 21:00:00")));
		insert(new Opening(members.get(3).getRecID(), DateHelpers.toTimestamp("2021/03/25 09:00:00"), DateHelpers.toTimestamp("2021/03/25 18:00:00")));
		insert(new Opening(members.get(0).getRecID(), DateHelpers.toTimestamp("2021/03/29 06:00:00"), DateHelpers.toTimestamp("2021/03/29 18:00:00")));
		insert(new Opening(members.get(0).getRecID(), DateHelpers.toTimestamp("2021/03/31 06:00:00"), DateHelpers.toTimestamp("2021/03/31 18:00:00")));
		insert(new Opening(members.get(0).getRecID(), DateHelpers.toTimestamp("2021/04/02 06:00:00"), DateHelpers.toTimestamp("2021/04/02 18:00:00")));
		insert(new Opening(members.get(2).getRecID(), DateHelpers.toTimestamp("2021/04/04 12:00:00"), DateHelpers.toTimestamp("2021/04/04 13:00:00")));
		insert(new Opening(members.get(1).getRecID(), DateHelpers.toTimestamp("2021/04/05 12:00:00"), DateHelpers.toTimestamp("2021/04/05 13:00:00")));
		insert(new Opening(members.get(3).getRecID(), DateHelpers.toTimestamp("2021/04/06 12:00:00"), DateHelpers.toTimestamp("2021/04/06 13:00:00")));
		insert(new Opening(members.get(2).getRecID(), DateHelpers.toTimestamp("2021/04/07 12:00:00"), DateHelpers.toTimestamp("2021/04/07 13:00:00")));
		insert(new Opening(members.get(1).getRecID(), DateHelpers.toTimestamp("2021/04/08 12:00:00"), DateHelpers.toTimestamp("2021/04/08 13:00:00")));
		insert(new Opening(members.get(3).getRecID(), DateHelpers.toTimestamp("2021/04/09 12:00:00"), DateHelpers.toTimestamp("2021/04/09 13:00:00")));
		insert(new Opening(members.get(4).getRecID(), 2021, 4, 13, 4, 30, 2021, 5, 15, 6, 30));
		insert(new Opening(members.get(4).getRecID(), 2021, 4, 15, 4, 30, 2021, 5, 15, 6, 30));

		logger.info(size() + " records inserted");
	}
}
