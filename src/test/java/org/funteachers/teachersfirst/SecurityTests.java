package org.funteachers.teachersfirst;

import javax.servlet.ServletException;

import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTests {

	Member apple;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testWhitelist() {
		SecurityChecker.populateWhitelist();
		//String greatIp = Security.nsLookup("dstults.net");
		//assertTrue(Security.isWhitelisted(greatIp));
		assertFalse(SecurityChecker.isWhitelisted("255.255.255.255"));
		
		//String result = Security.nsLookup("3j;k5l45k;43@#!@#$/123][]43kl54");
		//assertSame(result, "");
	}

	@Test
	void testCheckPasswordAndLoginAndId() throws ServletException {
		/* Disabled during server transfer
		DataManager.initializeDAOs();
		Member failedLogin = Security.checkPassword("testMember01", "asd14234123432dsafdsfdsafdas");
		assertNull(failedLogin);
		Member goodLogin = Security.checkPassword("mcman", "asdf");
		*/
	}

}
