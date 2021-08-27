package org.funteachers.teachersfirst;

import org.funteachers.teachersfirst.managers.DateHelpers;
import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MemberTests {

	Member fred;
	Member amy;
	Member juan;

	@BeforeEach
	void setUp() {
		fred = new Member("fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro1", "notes1", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		amy = new Member("amy", "Amy", 10, DateHelpers.toTimestamp("1987/10/15 00:00:00"), "f", "intro2", "notes2", "222-222-222", "", "amy@lwtech.edu", false, true, false);
		juan = new Member("juan", "Juan", 100, DateHelpers.toTimestamp("1992/02/25 00:00:00"), "", "intro2", "notes2", "111-111-1111", "999-999-9999", "juan@lwtech.edu", true, false, false);
	}

	@Test
	void testConstructor() {
		Exception ex = null;

		assertEquals(1976, fred.getBirthDate().getYear());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(-666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: recID < -1"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", null, 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: displayName is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: displayName is empty"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, null, "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: loginName is null"));
	
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: loginName is empty"));
	
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, null, "m", "", "444-444-4444", "intro", "notes", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: birthdate is null"));
	
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), null, "intro", "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: gender is null"));

		/* This is no longer an error so much as a valid way of disabling login ability:
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "", "Fred", DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: passwordHash is empty"));
		*/

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", null, "notes", "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: selfIntroduction is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", null, "444-444-4444", "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: instructorNotes is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", null, "", "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: phone1 is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", null, "fred@lwtech.edu", false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: phone2 is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Member(666, "fred", "Fred", 0, DateHelpers.toTimestamp("1976/06/05 00:00:00"), "m", "intro", "notes", "444-444-4444", "", null, false, false, true);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: email is null"));
	}

	@Test
	void testGetRecID() {
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
	void testGetLoginName() {
		assertEquals("fred", fred.getLoginName());
		assertEquals("amy", amy.getLoginName());
		assertEquals("juan", juan.getLoginName());
	}

	@Test
	void testGetDisplayName() {
		assertEquals("Fred", fred.getDisplayName());
		assertEquals("Amy", amy.getDisplayName());
		assertEquals("Juan", juan.getDisplayName());
	}

	@Test
	void testGetBirthDate() {
		assertEquals("1992-02-25 00:00:00.0", juan.getBirthdate().toString());
	}

	@Test
	void testGetAge() {
		assertEquals(45, fred.getAge());
		assertEquals(33, amy.getAge());
		assertEquals(29, juan.getAge());
	}

	@Test
	void testGetGender() {
		assertEquals("m", fred.getGender());
	}

	@Test
	void testGetInstructorNotes() {
		assertEquals("notes2", amy.getInstructorNotes());
	}

	@Test
	void testGetPhone1() {
		assertEquals("111-111-1111", juan.getPhone1());
	}

	@Test
	void testGetPhone2() {
		assertEquals("999-999-9999", juan.getPhone2());
	}

	@Test 
	void testGetEmail() {
		assertEquals("amy@lwtech.edu", amy.getEmail());
	}

	@Test
	void testGetIsStudent() {
		assertTrue(juan.getIsStudent());
	}

	@Test
	void testGetIsInstructor() {
		assertTrue(amy.getIsInstructor());
	}

	@Test
	void testGetIsAdmin() {
		assertTrue(fred.getIsAdmin());
	}

	@Test
	void testSetLoginName() {
		Exception ex = null;

		fred.setLoginName("Freddinator");
		assertEquals("Freddinator", fred.getLoginName());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setLoginName(null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: loginName is null"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setLoginName("");
		});
		assertTrue(ex.getMessage().contains("Invalid argument: loginName is empty"));
	}

	@Test
	void testSetDisplayName() {
		Exception ex = null;

		fred.setDisplayName("Freddie");
		assertEquals("Freddie", fred.getDisplayName());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setDisplayName("");
		});
		assertTrue(ex.getMessage().contains("Invalid argument: name is empty"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			fred.setDisplayName(null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: name is null"));
	}

	@Test
	void testSetBirthdate() {

		amy.setBirthdate(2000, 7, 15,6,30,0);
		assertEquals(DateHelpers.toTimestamp(2000,7,15,6,30,0), amy.getBirthdate());
		
		Exception ex = assertThrows(IllegalArgumentException.class, () -> {
			amy.setBirthdate(null);	
		});
		assertTrue(ex.getMessage().contains("Invalid argument: birthdate is null"));
	}

	@Test
	void testSetGender() {

		fred.setGender("other");
		assertEquals("other", fred.getGender());

		Exception ex = assertThrows(IllegalArgumentException.class, ()->{
			fred.setGender(null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: gender is null"));
	}

	@Test
	void testSetInstructorNotes() {
		
		juan.setInstructorNotes("Weird bus schedule");
		assertEquals("Weird bus schedule", juan.getInstructorNotes());
		
		Exception ex = assertThrows(IllegalArgumentException.class, ()->{
			juan.setInstructorNotes(null);
		});
		assertTrue(ex.getMessage().contains("Invalid argument: instructorNotes is null"));
	}

	@Test
	void testSetPhone1() {

		amy.setPhone1("123-456-7890");
		assertEquals("123-456-7890", amy.getPhone1());
	}

	@Test
	void testSetPhone2() {

		amy.setPhone2("123-456-7890");
		assertEquals("123-456-7890", amy.getPhone2());
	}

	@Test
	void testSetEmail() {

		fred.setEmail("supercoolemail@mail.com");
		assertEquals("supercoolemail@mail.com", fred.getEmail());
	}

	@Test
	void testSetIsStudent() {
		fred.setIsStudent(true);
		assertTrue(fred.getIsStudent());
	}

	@Test
	void testSetInstructor() {
		juan.setIsInstructor(true);
		assertTrue(juan.getIsInstructor());
	}

	@Test
	void testSetAdmin() {
		amy.setIsAdmin(true);
		assertTrue(amy.getIsAdmin());
	}

	@Test
	void testToString() {
		assertTrue(fred.toString().contains("L:fred")); // login
		assertTrue(fred.toString().contains("Fred")); // name
		assertTrue(fred.toString().contains("G:m")); // gender
		assertTrue(fred.toString().contains("A:45")); // age
		assertTrue(fred.toString().contains("P:0-0-1")); // permissions
	}

	@Test
	void testEquals() {
		
		Member matt = new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false);
		
		assertFalse(matt.equals(null));
		assertTrue(matt.equals(matt));
		assertFalse(matt.equals(fred));
		
		assertFalse(matt.equals(new Member(12, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11,"maty", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2020/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "f", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a lame bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs moar practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "333-333-333", "", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "444-444-444", "mat@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "MAT@lwtech.edu", false, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", true, true, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, false, false)));
		assertFalse(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, true)));
	
		assertTrue(matt.equals(new Member(11, "matt", "Matt", 0, DateHelpers.toTimestamp("2000/08/15 00:00:00"), "m", "Has a sick bike", "Needs more practice", "222-222-222", "", "mat@lwtech.edu", false, true, false)));
	}

}
