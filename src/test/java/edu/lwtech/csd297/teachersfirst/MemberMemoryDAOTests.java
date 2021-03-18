package edu.lwtech.csd297.teachersfirst;

import java.applet.Applet;
import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

class MemberMemoryDAOTests {

	private static final int FIRST_REC_ID = 1001;

	private DAO<Member> memberDAO;

	private Member john;
	private Member fred;

	@BeforeEach
	void setUp() {
		john = new Member("john", "Password01", "John", DateHelpers.toTimestamp("1992/02/25 00:00:00"), "m", "", "111-111-1111", "", "john@lwtech.edu", true, false, false);
		fred = new Member("fred", "Password01", "Fred", DateHelpers.toTimestamp("1957/04/15 00:00:00"), "m", "", "123-123-1234", "", "fred@lwtech.edu", false, false, true);

		memberDAO = new MemberMemoryDAO();
		memberDAO.initialize("");
	}

	@AfterEach
	void tearDown() {
		memberDAO.terminate();
	}

	@Test
	void testInitialize() {
		Exception ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.initialize(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testInsert() {
		Exception ex = null;

		assertEquals(8, memberDAO.size());
		int listID = memberDAO.insert(john);
		assertTrue(listID > 0);
		assertEquals(9, memberDAO.size());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.insert(null);
		});
		assertTrue(ex.getMessage().contains("null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.insert(john);
		});
		assertTrue(ex.getMessage().contains("already"));

	}

	@Test
	void testRetrieveByID() {
		Exception ex = null;

		Member list = memberDAO.retrieveByID(FIRST_REC_ID);
		assertEquals(1001, list.getRecID());
		list = memberDAO.retrieveByID(FIRST_REC_ID + 1);
		assertEquals(1002, list.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.retrieveByID(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

	@Test
	void testRetrieveByIndex() {
		Exception ex = null;

		Member list = memberDAO.retrieveByIndex(0);
		assertEquals(FIRST_REC_ID, list.getRecID());
		list = memberDAO.retrieveByIndex(1);
		assertEquals(FIRST_REC_ID + 1, list.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.retrieveByIndex(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

	@Test
	void testRetrieveAll() {
		List<Member> allLists = new ArrayList<>();
		allLists = memberDAO.retrieveAll();
		assertEquals(8, allLists.size());
	}

	@Test
	void testRetrieveAllIDs() {
		List<Integer> ids = memberDAO.retrieveAllIDs();
		assertEquals(8, ids.size());
	}

	@Test
	void testSearch() {
		Exception ex = null;

		List<Member> lists = memberDAO.search("Fred");
		assertEquals(1, lists.size());
		lists = memberDAO.search("NotHere");
		assertEquals(0, lists.size());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.search(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testUpdate() {
		Exception ex = null;

		Member teachersFirst = memberDAO.retrieveByID(FIRST_REC_ID);
		teachersFirst.setDisplayName("Apple");
		memberDAO.update(teachersFirst);
		teachersFirst = memberDAO.retrieveByID(FIRST_REC_ID);
		assertEquals("Apple", teachersFirst.getDisplayName());

		assertFalse(memberDAO.update(john));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.update(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testDelete() {
		Exception ex = null;

		int fredID = memberDAO.search("Fred").get(0).getRecID();
		memberDAO.delete(fredID);
		assertNull(memberDAO.retrieveByID(fredID));
		memberDAO.delete(666);

		ex = assertThrows(IllegalArgumentException.class, () -> {
			memberDAO.delete(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));
	}

}
