package edu.lwtech.csd297.teachersfirst;

import java.util.*;

import javax.servlet.*;

import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DataManager {
	
	public static final List<DAO<?>> allDAOs = new ArrayList<>();
	private static DAO<Member> memberDAO = null;

	// Meta constructors and destructors

	public static void initializeDAOs() throws ServletException {
		memberDAO = new MemberMemoryDAO();
		allDAOs.add(memberDAO);
		if (!memberDAO.initialize(""))
			throw new UnavailableException("Unable to initialize the memberDAO.");
	}

	public static void terminateDAOs() {
		// memberDAO.terminate();
		for (DAO<?> iDAO : allDAOs) {
			iDAO.terminate();
		}
	}

	// methods

	public static DAO<Member> getMemberDAO() {
		return memberDAO;
	}

}
