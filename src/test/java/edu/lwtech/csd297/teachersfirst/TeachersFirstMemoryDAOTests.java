package edu.lwtech.csd297.teachersfirst;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

class TeachersFirstMemoryDAOTests {

    private static final int FIRST_REC_ID = 1001;

    private DAO<TeachersFirst> teachersFirstDAO;

    private TeachersFirst john;

    @BeforeEach
    void setUp() {
        john = new TeachersFirst("John", 55);

        teachersFirstDAO = new TeachersFirstMemoryDAO();
        teachersFirstDAO.initialize("");  // Params ignored for memory DAO
    }

    @AfterEach
    void tearDown() {
        teachersFirstDAO.terminate();
    }

    @Test
    void testInitialize() {
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.initialize(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testInsert() {
        Exception ex = null;

        assertEquals(3, teachersFirstDAO.size());
        int listID = teachersFirstDAO.insert(john);        // Add a second copy of the roman list
        assertTrue(listID > 0);
        assertEquals(4, teachersFirstDAO.size());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.insert(null); }
        );
        assertTrue(ex.getMessage().contains("null"));

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.insert(john); }
        );
        assertTrue(ex.getMessage().contains("already"));

    }

    @Test
    void testRetrieveByID() {
        Exception ex = null;

        TeachersFirst list = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
        assertEquals(1001, list.getRecID());
        list = teachersFirstDAO.retrieveByID(FIRST_REC_ID+1);
        assertEquals(1002, list.getRecID());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.retrieveByID(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void testRetrieveByIndex() {
        Exception ex = null;

        TeachersFirst list = teachersFirstDAO.retrieveByIndex(0);
        assertEquals(FIRST_REC_ID, list.getRecID());
        list = teachersFirstDAO.retrieveByIndex(1);
        assertEquals(FIRST_REC_ID+1, list.getRecID());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.retrieveByIndex(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void testRetrieveAll() {
        List<TeachersFirst> allLists = new ArrayList<>();
        allLists = teachersFirstDAO.retrieveAll();
        assertEquals(3, allLists.size());
    }

    @Test
    void testRetrieveAllIDs() {
        List<Integer> ids = teachersFirstDAO.retrieveAllIDs();
        assertEquals(3, ids.size());
    }

    @Test
    void testSearch() {
        Exception ex = null;

        List<TeachersFirst> lists = teachersFirstDAO.search("Fred");
        assertEquals(1, lists.size());
        lists = teachersFirstDAO.search("NotHere");
        assertEquals(0, lists.size());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.search(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testUpdate() {
        Exception ex = null;

        TeachersFirst teachersFirst = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
        teachersFirst.setAge(1);
        teachersFirstDAO.update(teachersFirst);
        teachersFirst = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
        assertEquals(1, teachersFirst.getAge());

        assertFalse(teachersFirstDAO.update(john));
        
        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.update(null); }
        );
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testDelete() {
        Exception ex = null;

        int fredID = teachersFirstDAO.search("Fred").get(0).getRecID();
        teachersFirstDAO.delete(fredID);
        assertNull(teachersFirstDAO.retrieveByID(fredID));
        teachersFirstDAO.delete(666);

        ex = assertThrows(IllegalArgumentException.class,
            () -> { teachersFirstDAO.delete(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

}
