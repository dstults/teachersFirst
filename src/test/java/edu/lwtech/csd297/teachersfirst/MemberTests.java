package edu.lwtech.csd297.teachersfirst;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

class MemberTests {

	Member fred;
	Member amy;
	Member juan;

	@BeforeEach
	void setUp() {
		fred = new Member("fred", "Password01", "Fred", DateHelpers.ToTimestamp("1976/06/05 00:00:00"), "m", "", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		amy = new Member("amy", "Password01", "Amy", DateHelpers.ToTimestamp("1987/10/15 00:00:00"), "f", "", "222-222-222", "", "amy@lwtech.edu", false, true, false);
		juan = new Member("juan", "password01", "Juan", DateHelpers.ToTimestamp("1992/02/25 00:00:00"), "m", "", "111-111-1111", "", "juan@lwtech.edu", true, false, false);
	}

	@Test
	void testConstructor() {
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(-666, "fred", "Password01", "Fred", DateHelpers.ToTimestamp("1976/06/05 00:00:00"), "m", "", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("recID"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Password01", null, DateHelpers.ToTimestamp("1976/06/05 00:00:00"), "m", "", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("displayName is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Password01", "", DateHelpers.ToTimestamp("1976/06/05 00:00:00"), "m", "", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("displayName is empty"));
	}

	@Test
	void testGetId() {
		assertEquals(-1, fred.getRecID());
		assertEquals(-1, amy.getRecID());
		assertEquals(-1, juan.getRecID());
	}

	@Test
	void testSetRecID() {
		Exception ex = null;

		fred.setRecID(123);
		assertEquals(123, fred.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setRecID(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setRecID(666);
		});
		assertTrue(ex.getMessage().contains("already been added"));
	}

	@Test
	void testGetDisplayName() {
		assertEquals("Fred", fred.getDisplayName());
		assertEquals("Amy", amy.getDisplayName());
		assertEquals("Juan", juan.getDisplayName());
	}

	@Test
	void testGetAge() {
		//TODO: Fix Get Age
		assertEquals(44, fred.getAge());
		assertEquals(33, amy.getAge());
		assertEquals(29, juan.getAge());
	}

	@Test
	void testSetDisplayName() {
		Exception ex = null;

		fred.setDisplayName("Freddie");
		assertEquals("Freddie", fred.getDisplayName());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setDisplayName("");
		});
		assertTrue(ex.getMessage().contains("empty"));
	}

	@Test
	void testToString() {
		assertTrue(fred.toString().contains("L:fred")); // login
		assertTrue(fred.toString().contains("Fred")); // name
		assertTrue(fred.toString().contains("G:m")); // gender
		assertTrue(fred.toString().contains("A:44")); // age
		assertTrue(fred.toString().contains("P:0-0-1")); // permissions
	}

	@Test
	void testEquals() {
		Member amy2 = new Member("amy", "Password01", "Amy", DateHelpers.ToTimestamp("1987/10/15 00:00:00"), "f", "", "222-222-222", "", "amy@lwtech.edu", false, true, false);
		assertTrue(amy.equals(amy2));
		assertEquals(amy, amy2);
		assertFalse(amy.equals(fred));
		assertNotEquals(amy2, fred);
	}

}
