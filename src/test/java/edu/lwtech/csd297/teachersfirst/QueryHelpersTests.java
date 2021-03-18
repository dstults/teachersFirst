package edu.lwtech.csd297.teachersfirst;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

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

	@Test
	void testGets() {
		//TODO: Get Mock httprequest object
	}

}
