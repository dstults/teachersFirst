package edu.lwtech.csd297.teachersfirst;

import java.net.UnknownHostException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import jdk.jfr.Timestamp;

class SecurityTests {

	Member apple;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testWhitelist() {
		Security.populateWhitelist();
		String greatIp = Security.nsLookup("dstults.net");
		assertTrue(Security.isWhitelisted(greatIp));
		assertFalse(Security.isWhitelisted("255.255.255.255"));
		
		//String result = Security.nsLookup("3j;k5l45k;43@#!@#$/123][]43kl54");
		//assertSame(result, "");
	}

	@Test
	void testCheckPasswordAndLoginAndId() throws ServletException {
		DataManager.initializeDAOs();
		Member failedLogin = Security.checkPassword("testMember01", "asd14234123432dsafdsfdsafdas");
		assertNull(failedLogin);
		Member goodLogin = Security.checkPassword("mcman", "asdf");
		
		//TODO: Get install Mock Http Module
		assertThrows(NullPointerException.class, () -> Security.login(null, goodLogin));

		//TODO: Get install Mock Http Module
		assertThrows(NullPointerException.class, () -> Security.getUserId(null));
	}

}
