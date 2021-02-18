package edu.lwtech.csd297.skeleton.daos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.skeleton.pojos.*;

// Memory-based DAO class - stores objects in a List.  No persistance.

public class SkeletonMemoryDAO implements DAO<Skeleton> {

    private static final Logger logger = LogManager.getLogger(SkeletonMemoryDAO.class.getName());

    private AtomicInteger nextListRecID;
    private List<Skeleton> skeletonDB;      // Our "database" table

    // ----------------------------------------------------------------

    public SkeletonMemoryDAO() {
        this.nextListRecID = new AtomicInteger(1000);
        this.skeletonDB = new ArrayList<>();
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
        skeletonDB = null;
    }

    public int insert(Skeleton pojo) {
        if (pojo == null)
            throw new IllegalArgumentException("insert: cannot insert null object");
        if (pojo.getRecID() != -1)
            throw new IllegalArgumentException("insert: object is already in database (recID != -1)");
        logger.debug("Inserting " + pojo + "...");

        pojo.setRecID(nextListRecID.incrementAndGet());
        skeletonDB.add(pojo);

        logger.debug("Item successfully inserted!");
        return pojo.getRecID();
    }

    public Skeleton retrieveByID(int id) {
        if (id < 0)
            throw new IllegalArgumentException("retrieveByID: id cannot be negative");
        logger.debug("Getting object with ID: {} ...", id);

        Skeleton foundObject = null;
        for (Skeleton pojo : skeletonDB) {
            if (pojo.getRecID() == id) {
                foundObject = pojo;
                break;
            }
        }
        return foundObject;
    }

    public Skeleton retrieveByIndex(int index) {
        // Note: indexes are zero-based
        if (index < 0)
            throw new IllegalArgumentException("retrieveByIndex: index cannot be negative");
        logger.debug("Getting object with index: {} ...", index);

        return skeletonDB.get(index);
    }

    public List<Skeleton> retrieveAll() {
        logger.debug("Getting all POJOs ...");
        return new ArrayList<>(skeletonDB);       // Return copy of DB collection
    }

    public List<Integer> retrieveAllIDs() {
        logger.debug("Getting all IDs...");

        List<Integer> listIDs = new ArrayList<>();
        for (Skeleton pojo : skeletonDB) {
            listIDs.add(pojo.getRecID());
        }
        return listIDs;
    }

    public List<Skeleton> search(String keyword) {
        if (keyword == null)
            throw new IllegalArgumentException("search: keyword cannot be null");
        logger.debug("Searching for objects containing: '{}'", keyword);

        keyword = keyword.toLowerCase();
        List<Skeleton> pojosFound = new ArrayList<>();
        for (Skeleton pojo : skeletonDB) {
            if (pojo.getName().toLowerCase().contains(keyword)) {
                pojosFound.add(pojo);
                break;
            }
        }
        logger.debug("Found {} objects with the keyword '{}'!", pojosFound.size(), keyword);
        return pojosFound;
    }

    public int size() {
        return skeletonDB.size();
    }

    public boolean update(Skeleton pojo) {
        if (pojo == null)
            throw new IllegalArgumentException("update: cannot update null object");
        logger.debug("Trying to update object with ID: {} ...", pojo.getRecID());

        for (int i = 0; i < skeletonDB.size(); i++) {
            if (skeletonDB.get(i).getRecID() == pojo.getRecID()) {
                skeletonDB.set(i, pojo);
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

        Skeleton pojoFound = null;
        for (Skeleton pojo : skeletonDB) {
            if (pojo.getRecID() == id) {
                pojoFound = pojo;
                break;
            }
        }
        if (pojoFound != null) {
            skeletonDB.remove(pojoFound);
            logger.debug("Successfully deleted object with ID: {}", id);
        } else {
            logger.debug("Unable to delete object with ID: {}. List not found.", id);
        }
    }

    // =================================================================

    private void addDemoData() {
        logger.debug("Creating demo data...");

        insert(new Skeleton("Fred", 66));
        insert(new Skeleton("Amy", 43));
        insert(new Skeleton("Juan", 22));

        logger.info(size() + " records inserted");
    }

}
