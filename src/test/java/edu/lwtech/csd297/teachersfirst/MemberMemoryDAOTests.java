package edu.lwtech.csd297.teachersfirst;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

class MemberMemoryDAOTests {

	private static final int FIRST_REC_ID = 1001;

	private DAO<Member> teachersFirstDAO;

	private Member john;
	private Member fred;

	@BeforeEach
	void setUp() {
		john = new Member("john", "Password01", "John", 55, "m", "", true, false, false);
		fred = new Member("fred", "Password01", "Fred", 66, "m", "", false, false, true);

		teachersFirstDAO = new MemberMemoryDAO();
		teachersFirstDAO.initialize(""); // Params ignored for memory DAO
	}

	@AfterEach
	void tearDown() {
		teachersFirstDAO.terminate();
	}

	@Test
	void testInitialize() {
		Exception ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.initialize(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testInsert() {
		Exception ex = null;

		assertEquals(8, teachersFirstDAO.size());
		int listID = teachersFirstDAO.insert(john);
		assertTrue(listID > 0);
		assertEquals(9, teachersFirstDAO.size());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.insert(null);
		});
		assertTrue(ex.getMessage().contains("null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.insert(john);
		});
		assertTrue(ex.getMessage().contains("already"));

	}

	@Test
	void testRetrieveByID() {
		Exception ex = null;

		Member list = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
		assertEquals(1001, list.getRecID());
		list = teachersFirstDAO.retrieveByID(FIRST_REC_ID + 1);
		assertEquals(1002, list.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.retrieveByID(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

	@Test
	void testRetrieveByIndex() {
		Exception ex = null;

		Member list = teachersFirstDAO.retrieveByIndex(0);
		assertEquals(FIRST_REC_ID, list.getRecID());
		list = teachersFirstDAO.retrieveByIndex(1);
		assertEquals(FIRST_REC_ID + 1, list.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.retrieveByIndex(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

	@Test
	void testRetrieveAll() {
		List<Member> allLists = new ArrayList<>();
		allLists = teachersFirstDAO.retrieveAll();
		assertEquals(8, allLists.size());
	}

	@Test
	void testRetrieveAllIDs() {
		List<Integer> ids = teachersFirstDAO.retrieveAllIDs();
		assertEquals(8, ids.size());
	}

	@Test
	void testSearch() {
		Exception ex = null;

		List<Member> lists = teachersFirstDAO.search("Fred");
		assertEquals(1, lists.size());
		lists = teachersFirstDAO.search("NotHere");
		assertEquals(0, lists.size());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.search(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testUpdate() {
		Exception ex = null;

		Member teachersFirst = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
		teachersFirst.setAge(1);
		teachersFirstDAO.update(teachersFirst);
		teachersFirst = teachersFirstDAO.retrieveByID(FIRST_REC_ID);
		assertEquals(1, teachersFirst.getAge());

		assertFalse(teachersFirstDAO.update(john));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.update(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testDelete() {
		Exception ex = null;

		System.out.println("This is working! Look for me! ---------------------------------------------------------");
		System.out.println("Size: " + teachersFirstDAO.size());
		System.out.println("Fred Size: " + teachersFirstDAO.search("Fred").size());
		System.out.println("Get: " + teachersFirstDAO.search("Fred").get(0));
		System.out.println("RecID: " + teachersFirstDAO.search("Fred").get(0).getRecID());
		int fredID = teachersFirstDAO.search("Fred").get(0).getRecID();
		teachersFirstDAO.delete(fredID);
		assertNull(teachersFirstDAO.retrieveByID(fredID));
		teachersFirstDAO.delete(666);

		ex = assertThrows(IllegalArgumentException.class, () -> {
			teachersFirstDAO.delete(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

}
