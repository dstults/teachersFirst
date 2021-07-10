package edu.lwtech.csd297.teachersfirst.daos;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class MemberSqlDAO implements DAO<Member> {
	
	private static final Logger logger = LogManager.getLogger(MemberSqlDAO.class.getName());

	private Connection conn = null;

	public MemberSqlDAO() {
		this.conn = null;                                   // conn must be created during init()
	}

	public boolean initialize(String initParams) {
		logger.info("Connecting to the database...");

		conn = SQLUtils.connect(initParams);
		if (conn == null) {
			logger.error("Unable to connect to SQL Database: " + initParams);
			return false;
		}
		logger.info("...connected!");

		return true;
	}

	public void terminate() {
		logger.debug("Terminating Member SQL DAO...");
		SQLUtils.disconnect(conn);
		conn = null;
	}

	public int insert(Member member) {
		logger.debug("Inserting " + member + "...");

		if (member.getRecID() != -1) {
			logger.error("Error: Cannot add previously added Member: " + member);
			return -1;
		}

		String query = "INSERT INTO members (loginName, passwordHash, displayName, credits, birthdate, gender, selfIntroduction, instructorNotes, phone1, phone2, email, isStudent, isInstructor, isAdmin) VALUES (?,SHA1(?),?,?,?,?,?,?,?,?,?,?,?,?);";

		int recID = SQLUtils.executeSqlMemberInsert(conn, query, member.getRecID(), member.getLoginName(), member.getPasswordHash(), member.getDisplayName(), member.getCredits(), member.getBirthdate(), member.getGender(), member.getSelfIntroduction(), member.getInstructorNotes(), member.getPhone1(), member.getPhone2(), member.getEmail(), member.getIsStudent(), member.getIsInstructor(), member.getIsAdmin());
		
		logger.debug("Member successfully inserted with ID = " + recID);
		return recID;
	}

	public Member retrieveByID(int recID) {
		//logger.debug("Trying to get Member with ID: " + recID);
		
		String query = "SELECT * FROM members WHERE recID=" + recID + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Member member = convertRowToMember(row);
		return member;
	}

	public Member retrieveByLoginName(String loginName) {
		//logger.debug("Trying to get Member with login name: " + loginName);
		
		String query = "SELECT * FROM members WHERE loginName='" + loginName +"';";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Member member = convertRowToMember(row);
		return member;
	}

	public Member retrieveByLoginNameAndPassword(String loginName, String passwordHash) {
		//logger.debug("Trying to get Member with login name and password: " + loginName + " " + passwordHash);
		
		String query = "SELECT * FROM members WHERE loginName='" + loginName +"' AND passwordHash=SHA1('"+passwordHash+"');";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Member member = convertRowToMember(row);
		return member;
	}
	
	public Member retrieveByIndex(int index) {
		logger.debug("Trying to get Member with index: " + index);
		logger.warn("This will eventually be deprecated. Don't use this.");

		if (index < 0) {
			logger.debug("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM members ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1);
		Member member = convertRowToMember(row);
		return member;
	}
	
	public List<Member> retrieveAll() {
		logger.debug("Getting all members...");
		
		String query = "SELECT * FROM members ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No members found!");
			return null;
		}

		List<Member> members = new ArrayList<>();
		for (SQLRow row : rows) {
			Member member = convertRowToMember(row);
			members.add(member);
		}
		return members;
	}
	
	public List<Integer> retrieveAllIDs() {
		logger.debug("Getting all Member IDs...");

		String query = "SELECT recID FROM members ORDER BY recID;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("No members found!");
			return null;
		}
		
		List<Integer> recIDs = new ArrayList<>();
		for (SQLRow row : rows) {
			String value = row.getItem("recID");
			int i = Integer.parseInt(value);
			recIDs.add(i);
		}
		return recIDs;
	}

	public List<Member> search(String keyword) {
		logger.debug("Searching for member with '" + keyword + "'");

		String query = "SELECT * FROM members WHERE userName LIKE ? ORDER BY recID;";

		keyword = "%" + keyword + "%";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query, keyword);
		if (rows == null || rows.size() == 0) {
			logger.debug("No members found!");
			return null;
		}

		List<Member> members = new ArrayList<>();
		for (SQLRow row : rows) {
			Member member = convertRowToMember(row);
			members.add(member);
		}
		return members;
	}

	public boolean update(Member member) {
		throw new UnsupportedOperationException("Unable to update existing member in database.");
	}

	public void delete(int recID) {
		logger.debug("Trying to delete Member with ID: " + recID);

		String query = "DELETE FROM members WHERE recID=" + recID;
		SQLUtils.executeSql(conn, query);
	}
	
	public int size() {
		logger.debug("Getting the number of rows...");

		String query = "SELECT COUNT(*) AS cnt FROM members;";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.error("No members found!");
			return 0;
		}

		String value = rows.get(0).getItem("cnt");
		return Integer.parseInt(value);
	}    

	// =====================================================================

	private Member convertRowToMember(SQLRow row) {
		logger.debug("Converting " + row + " to Member...");
		int recID = Integer.parseInt(row.getItem("recID"));

		String loginName = row.getItem("loginName");
		String passwordHash = row.getItem("passwordHash");

		String displayName = row.getItem("displayName");
		float credits = Float.parseFloat(row.getItem("credits"));

		Timestamp birthdate = DateHelpers.fromSqlDateToTimestamp(row.getItem("birthdate"));

		String gender = row.getItem("gender");
		String selfIntroduction = row.getItem("selfIntroduction");
		String instructorNotes = row.getItem("instructorNotes");
		String phone1 = row.getItem("phone1");
		String phone2 = row.getItem("phone2");
		String email = row.getItem("email");

		Boolean isStudent = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("isStudent")));
		Boolean isInstructor = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("isInstructor")));
		Boolean isAdmin = SQLUtils.integerToBoolean(Integer.parseInt(row.getItem("isAdmin")));

		return new Member(recID, loginName, passwordHash, displayName, credits, birthdate, gender, selfIntroduction, instructorNotes, phone1, phone2, email, isStudent, isInstructor, isAdmin);
	}

}
