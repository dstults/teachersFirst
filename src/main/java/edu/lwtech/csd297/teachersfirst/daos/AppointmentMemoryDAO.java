package edu.lwtech.csd297.teachersfirst.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class AppointmentMemoryDAO implements DAO<Appointment> {
    private static final Logger logger = LogManager.getLogger(AppointmentMemoryDAO.class.getName());

	private AtomicInteger nextListRecID;
	private List<Appointment> appointmentDB;

	// ----------------------------------------------------------------

	public AppointmentMemoryDAO() {
		this.nextListRecID = new AtomicInteger(1000);
		this.appointmentDB = new ArrayList<>();
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
		logger.debug("Terminating Appointment Memory DAO...");
		appointmentDB = null;
	}

	public int insert(Appointment pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("insert: cannot insert null object");
		if (pojo.getRecID() != -1)
			throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
		logger.debug("Inserting " + pojo + "...");

		pojo.setRecID(nextListRecID.incrementAndGet());
		appointmentDB.add(pojo);

		logger.debug("Item successfully inserted!");
		return pojo.getRecID();
	}

	public Appointment retrieveByID(int id) {
		if (id < 0)
			throw new IllegalArgumentException("retrieveByID: id cannot be negative");
		logger.debug("Getting object with ID: {} ...", id);

		Appointment foundObject = null;
		for (Appointment pojo : appointmentDB) {
			if (pojo.getRecID() == id) {
				foundObject = pojo;
				break;
			}
		}
		return foundObject;
	}

	public Appointment retrieveByIndex(int index) {
		// Note: indexes are zero-based
		if (index < 0)
			throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
		logger.debug("Getting object with index: {} ...", index);

		return appointmentDB.get(index);
	}

	public List<Appointment> retrieveAll() {
		logger.debug("Getting all POJOs ...");
		return new ArrayList<>(appointmentDB); // Return copy of DB collection
	}

	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all IDs...");

		List<Integer> listIDs = new ArrayList<>();
		for (Appointment pojo : appointmentDB) {
			listIDs.add(pojo.getRecID());
		}
		return listIDs;
	}

	public List<Appointment> search(String keyword) {
		if (keyword == null) throw new IllegalArgumentException("search: keyword cannot be null");
		logger.debug("Searching for objects containing: '{}'", keyword);

		keyword = keyword.toLowerCase();
		List<Appointment> pojosFound = new ArrayList<>();
		for (Appointment pojo : appointmentDB) {
			if (pojo.getName().toLowerCase().contains(keyword)) {
				pojosFound.add(pojo);
				break;
			}
		}
		//logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
		return pojosFound;
	}

	public int size() {
		return appointmentDB.size();
	}

	public boolean update(Appointment pojo) {
		if (pojo == null)
			throw new IllegalArgumentException("update: cannot update null object");
		logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

		for (int i = 0; i < appointmentDB.size(); i++) {
			if (appointmentDB.get(i).getRecID() == pojo.getRecID()) {
				appointmentDB.set(i, pojo);
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

		Appointment pojoFound = null;
		for (Appointment pojo : appointmentDB) {
			if (pojo.getRecID() == id) {
				pojoFound = pojo;
				break;
			}
		}
		if (pojoFound != null) {
			appointmentDB.remove(pojoFound);
			logger.debug("Successfully deleted object with ID: {}", id);
		} else {
			logger.debug("Unable to delete object with ID: {}. List not found.", id);
		}
	}

	// =================================================================

	private void addDemoData() {
		logger.debug("Creating demo data...");
		List<Member> members = DataManager.getMemberDAO().retrieveAll();
		//error here
		
		insert(new Appointment(members.get(5).getRecID(), members.get(4).getRecID(), DateHelpers.toTimestamp("2020/03/21 15:40:00"), DateHelpers.toTimestamp("2020/03/21 16:20:00")));
		insert(new Appointment(members.get(6).getRecID(), members.get(3).getRecID(), DateHelpers.toTimestamp("2020/07/06 23:30:00"), DateHelpers.toTimestamp("2020/07/07 00:30:00")));
		insert(new Appointment(members.get(1).getRecID(), members.get(3).getRecID(), DateHelpers.toTimestamp("2020/08/01 12:00:00"), DateHelpers.toTimestamp("2020/08/01 13:00:00")));
		insert(new Appointment(members.get(5).getRecID(), members.get(2).getRecID(), DateHelpers.toTimestamp("2021/01/15 15:30:00"), DateHelpers.toTimestamp("2021/01/15 16:30:00")));
		insert(new Appointment(members.get(2).getRecID(), members.get(3).getRecID(), DateHelpers.toTimestamp("2021/02/15 15:30:00"), DateHelpers.toTimestamp("2021/02/15 16:30:00")));
		insert(new Appointment(members.get(3).getRecID(), members.get(2).getRecID(), DateHelpers.toTimestamp("2021/03/15 15:30:00"), DateHelpers.toTimestamp("2021/03/15 16:30:00")));
		insert(new Appointment(members.get(1).getRecID(), members.get(2).getRecID(), DateHelpers.toTimestamp("2021/02/22 15:30:00"), DateHelpers.toTimestamp("2021/02/22 16:30:00")));
		insert(new Appointment(members.get(7).getRecID(), members.get(3).getRecID(), DateHelpers.toTimestamp(2021, 2, 20, 4, 30, 0), DateHelpers.toTimestamp(2021, 2, 20, 6, 30, 0)));
		insert(new Appointment(members.get(7).getRecID(), members.get(1).getRecID(), DateHelpers.toTimestamp(2021, 2, 24, 2, 0, 0), DateHelpers.toTimestamp(2021, 2, 24, 3, 0, 0)));
		insert(new Appointment(members.get(2).getRecID(), members.get(1).getRecID(), DateHelpers.toTimestamp("2021/03/22 15:30:00"), DateHelpers.toTimestamp("2021/03/22 16:30:00")));
		insert(new Appointment(members.get(1).getRecID(), members.get(3).getRecID(), DateHelpers.toTimestamp("2021/04/15 15:30:00"), DateHelpers.toTimestamp("2021/04/15 16:30:00")));
		insert(new Appointment(members.get(3).getRecID(), members.get(1).getRecID(), DateHelpers.toTimestamp("2021/04/22 15:30:00"), DateHelpers.toTimestamp("2021/04/22 16:30:00")));

		logger.info(size() + " records inserted");
	}

}
