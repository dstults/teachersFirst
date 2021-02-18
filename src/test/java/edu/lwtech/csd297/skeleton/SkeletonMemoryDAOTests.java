package edu.lwtech.csd297.skeleton;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.skeleton.daos.*;
import edu.lwtech.csd297.skeleton.pojos.*;

class SkeletonMemoryDAOTests {

    private static final int FIRST_REC_ID = 1001;

    private DAO<Skeleton> skeletonDAO;

    private Skeleton john;

    @BeforeEach
    void setUp() {
        john = new Skeleton("John", 55);

        skeletonDAO = new SkeletonMemoryDAO();
        skeletonDAO.initialize("");  // Params ignored for memory DAO
    }

    @AfterEach
    void tearDown() {
        skeletonDAO.terminate();
    }

    @Test
    void testInitialize() {
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.initialize(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testInsert() {
        Exception ex = null;

        assertEquals(3, skeletonDAO.size());
        int listID = skeletonDAO.insert(john);        // Add a second copy of the roman list
        assertTrue(listID > 0);
        assertEquals(4, skeletonDAO.size());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.insert(null); }
        );
        assertTrue(ex.getMessage().contains("null"));

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.insert(john); }
        );
        assertTrue(ex.getMessage().contains("already"));

    }

    @Test
    void testRetrieveByID() {
        Exception ex = null;

        Skeleton list = skeletonDAO.retrieveByID(FIRST_REC_ID);
        assertEquals(1001, list.getRecID());
        list = skeletonDAO.retrieveByID(FIRST_REC_ID+1);
        assertEquals(1002, list.getRecID());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.retrieveByID(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void testRetrieveByIndex() {
        Exception ex = null;

        Skeleton list = skeletonDAO.retrieveByIndex(0);
        assertEquals(FIRST_REC_ID, list.getRecID());
        list = skeletonDAO.retrieveByIndex(1);
        assertEquals(FIRST_REC_ID+1, list.getRecID());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.retrieveByIndex(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void testRetrieveAll() {
        List<Skeleton> allLists = new ArrayList<>();
        allLists = skeletonDAO.retrieveAll();
        assertEquals(3, allLists.size());
    }

    @Test
    void testRetrieveAllIDs() {
        List<Integer> ids = skeletonDAO.retrieveAllIDs();
        assertEquals(3, ids.size());
    }

    @Test
    void testSearch() {
        Exception ex = null;

        List<Skeleton> lists = skeletonDAO.search("Fred");
        assertEquals(1, lists.size());
        lists = skeletonDAO.search("NotHere");
        assertEquals(0, lists.size());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.search(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testUpdate() {
        Exception ex = null;

        Skeleton skeleton = skeletonDAO.retrieveByID(FIRST_REC_ID);
        skeleton.setAge(1);
        skeletonDAO.update(skeleton);
        skeleton = skeletonDAO.retrieveByID(FIRST_REC_ID);
        assertEquals(1, skeleton.getAge());

        assertFalse(skeletonDAO.update(john));
        
        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.update(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testDelete() {
        Exception ex = null;

        int fredID = skeletonDAO.search("Fred").get(0).getRecID();
        skeletonDAO.delete(fredID);
        assertNull(skeletonDAO.retrieveByID(fredID));
        skeletonDAO.delete(666);

        ex = assertThrows(IllegalArgumentException.class,
            () -> { skeletonDAO.delete(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

}
