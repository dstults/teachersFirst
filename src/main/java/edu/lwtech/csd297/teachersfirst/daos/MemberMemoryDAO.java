package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.DateHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

// Memory-based DAO class - stores objects in a List.  No persistance.

public class MemberMemoryDAO implements DAO<Member> {

	private static final Logger logger = LogManager.getLogger(MemberMemoryDAO.class.getName());

	private AtomicInteger nextListRecID;
	private List<Member> memberDB;

	// ----------------------------------------------------------------

	public MemberMemoryDAO() {
		this.nextListRecID = new AtomicInteger(1000);
		this.memberDB = new ArrayList<>();
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
		logger.debug("Terminating Member Memory DAO...");
		memberDB = null;
	}

	public int insert(Member pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("insert: cannot insert null object");
		if (pojo.getRecID() != -1)
			throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
		logger.debug("Inserting " + pojo + "...");

		pojo.setRecID(nextListRecID.incrementAndGet());
		memberDB.add(pojo);

		logger.debug("Item successfully inserted!");
		return pojo.getRecID();
	}

	public Member retrieveByID(int id) {
		if (id < 0)
			throw new IllegalArgumentException("retrieveByID: id cannot be negative");
		logger.debug("Getting object with ID: {} ...", id);

		Member foundObject = null;
		for (Member pojo : memberDB) {
			if (pojo.getRecID() == id) {
				foundObject = pojo;
				break;
			}
		}
		return foundObject;
	}

	public Member retrieveByIndex(int index) {
		if (index < 0)
			throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
		logger.debug("Getting object with index: {} ...", index);

		return memberDB.get(index);
	}

	public List<Member> retrieveAll() {
		logger.debug("Getting all POJOs ...");
		return new ArrayList<>(memberDB); // Return copy of DB collection
	}

	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all IDs...");

		List<Integer> listIDs = new ArrayList<>();
		for (Member pojo : memberDB) {
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
		for (Member pojo : memberDB) {
			if (pojo.getName().toLowerCase().contains(keyword)) {
				pojosFound.add(pojo);
				break;
			}
		}
		//logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
		return pojosFound;
	}

	public int size() {
		return memberDB.size();
	}

	public boolean update(Member pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("update: cannot update null object");
		logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

		for (int i = 0; i < memberDB.size(); i++) {
			if (memberDB.get(i).getRecID() == pojo.getRecID()) {
				memberDB.set(i, pojo);
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
		for (Member pojo : memberDB) {
			if (pojo.getRecID() == id) {
				pojoFound = pojo;
				break;
			}
		}
		if (pojoFound != null) {
			memberDB.remove(pojoFound);
			logger.debug("Successfully deleted object with ID: {}", id);
		} else {
			logger.debug("Unable to delete object with ID: {}. List not found.", id);
		}
	}

	// =================================================================

	private void addDemoData() {
		logger.debug("Creating demo data...");

		insert(new Member("debug", "Password01", "Null User", DateHelpers.toTimestamp("1950/05/05 00:00:00"), "f", "", "111-111-1111", "", "debug@lwtech.edu", false, false, false));
		insert(new Member("darren", "Password01", "Darren S.", DateHelpers.toTimestamp("1960/06/06 00:00:00"), "m", "", "222-111-1111", "", "darren@lwtech.edu", false, true, true));
		insert(new Member("tanya", "Password01", "Tanya F.", DateHelpers.toTimestamp("1970/07/07 00:00:00"), "f", "", "333-111-1111", "", "tanya@lwtech.edu", false, true, false));
		insert(new Member("edmund", "Password01", "Edmund P.", DateHelpers.toTimestamp("1980/08/08 00:00:00"), "m", "", "444-111-1111", "", "edmund@lwtech.edu", false, true, false));
		insert(new Member("fred", "Password01", "Fred A.", DateHelpers.toTimestamp("1990/09/09 00:00:00"), "m", "", "555-111-1111", "", "fred@lwtech.edu", true, true, false));
		insert(new Member("susan", "Password01", "Susan I.", DateHelpers.toTimestamp("2000/10/10 00:00:00"), "f", "", "666-111-1111", "", "susan@lwtech.edu", false, false, true));
		insert(new Member("ben", "Password01", "Ben S.", "m", "", "777-111-1111", "", "ben@lwtech.edu", true, false, false));
		insert(new Member("betty", "Password01", "Betty S.", "f", "", "888-111-1111", "", "betty@lwtech.edu", true, false, false));

		logger.info(size() + " records inserted");
	}

}
