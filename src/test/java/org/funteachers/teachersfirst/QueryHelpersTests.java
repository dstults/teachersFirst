package org.funteachers.teachersfirst;

import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryHelpersTests {

	Member apple;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testSanitization() {
		String s1 = QueryHelpers.sanitizeForLog("\tLove{[<war>]}\n!@#$%^&*()_+-=");
		assertEquals("_Love{[<war>]}_!@#$%^&*()_+-=", s1);
		assertThrows(IllegalArgumentException.class, () -> QueryHelpers.sanitizeForWeb("\tLove{[<war>]}\n!@#$%^&*()_+-="));
		String s2 = QueryHelpers.sanitizeForWeb("\tLove{[<war>]}\n!@#$^&*()_+-=");
		assertEquals("Love{[&amp;lt;war&amp;gt;]}\n!@#$^&amp;*()_ -=", s2);		
	}

}
