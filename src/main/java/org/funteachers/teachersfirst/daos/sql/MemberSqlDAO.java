package org.funteachers.teachersfirst.daos.sql;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

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

		String query = "INSERT INTO members (loginName, passwordHash, token, displayName, credits, birthdate, gender, selfIntroduction, instructorNotes, phone1, phone2, email, isAdmin, isInstructor, isStudent) VALUES (?,SHA1(?),SHA1(?),?,?,?,?,?,?,?,?,?,?,?,?);";

		int recID = SQLUtils.executeSqlMemberInsert(conn, query, member.getRecID(), member.getLoginName(), "", "", member.getDisplayName(), member.getCredits(), member.getBirthdate(), member.getGender(), member.getSelfIntroduction(), member.getInstructorNotes(), member.getPhone1(), member.getPhone2(), member.getEmail(), member.getIsAdmin(), member.getIsInstructor(), member.getIsStudent());
		logger.debug("Member successfully inserted with ID = " + recID);
		return recID;
	}

	private Member getMemberQuery(String query) {
		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(0);
		Member member = convertRowToMember(row);
		return member;
	}

	public Member retrieveByID(int recID) {
		//logger.debug("Trying to get Member with ID: " + recID);
		
		String query = "SELECT * FROM members WHERE recID=" + recID + ";";

		return getMemberQuery(query);
	}

	public Member retrieveByLoginName(String loginName) {
		//logger.debug("Trying to get Member with login name: " + loginName);
		
		String query = "SELECT * FROM members WHERE loginName='" + loginName +"';";

		return getMemberQuery(query);
	}

	public Member retrieveByLoginNameAndPassword(String loginName, String passwordHash) {
		//logger.debug("Trying to get Member with login name and password: " + loginName + " " + passwordHash);
		
		String query = "SELECT * FROM members WHERE loginName='" + loginName +"' AND passwordHash=SHA1('" + passwordHash + "');";

		return getMemberQuery(query);
	}

	public Member retrieveByIdAndToken(int recID, String token) {
		
		String query = "SELECT * FROM members WHERE recID='" + recID +"' AND token='" + token + "';";

		return getMemberQuery(query);
	}

	public Member retrieveByIndex(int index) {
		logger.debug("Trying to get Member with index: " + index);

		if (index < 0) {
			logger.error("retrieveByIndex: index cannot be negative");
			return null;
		}

		int limiter = index + 1;

		String query = "SELECT * FROM members ORDER BY recID LIMIT " + limiter + ";";

		List<SQLRow> rows = SQLUtils.executeSql(conn, query);
		if (rows == null || rows.size() == 0) {
			logger.debug("Did not find member.");
			return null;
		}
		
		SQLRow row = rows.get(rows.size() - 1); // This is different from the rest, frankly I want to delete this entire method.
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
		if (member.getRecID() <= 0) throw new IllegalArgumentException("Illegal Argument: cannot update member with recID <= 0");

		String query = "UPDATE members SET credits = ?, phone1 = ?, phone2 = ?, email = ?, selfIntroduction = ?, instructorNotes = ? WHERE recID = " + member.getRecID() + ";";

		boolean success = SQLUtils.executeSqlUpdate(conn, query, String.valueOf(member.getCredits()), member.getPhone1(), member.getPhone2(), member.getEmail(), member.getSelfIntroduction(), member.getInstructorNotes());

		if (success)
			logger.debug("DATA for member [ {} ] successfully updated.", member.getRecID());
		else
			logger.error("!! DATA for member [ {} ] failed to update !!", member.getRecID());
		
		return success;
	}

	public boolean updateToken(Member member, String token) {
		if (member.getRecID() <= 0) throw new IllegalArgumentException("Illegal Argument: cannot update member with recID <= 0");

		String query = "UPDATE members SET token = ? WHERE recID = " + member.getRecID() + ";";

		boolean success = SQLUtils.executeSqlUpdate(conn, query, token);

		if (success)
			logger.debug("TOKEN for member [ {} ] successfully updated.", member.getRecID());
		else
			logger.error("!! TOKEN for member [ {} ] failed to update !!", member.getRecID());
		
		return success;
	}

	public boolean updatePassword(Member member, String password) {
		if (member.getRecID() <= 0) throw new IllegalArgumentException("Illegal Argument: cannot update member with recID <= 0");

		String query = "UPDATE members SET token = SHA1(?) WHERE recID = " + member.getRecID() + ";";

		boolean success = SQLUtils.executeSqlUpdate(conn, query, password);

		if (success)
			logger.debug("PASSWORD for member [ {} ] successfully updated.", member.getRecID());
		else
			logger.error("!! PASSWORD for member [ {} ] failed to update !!", member.getRecID());
		
		return success;
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
		//logger.debug("Converting " + row + " to Member...");
		int recID = Integer.parseInt(row.getItem("recID"));

		String loginName = row.getItem("loginName");

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

		return new Member(recID, loginName, displayName, credits, birthdate, gender, selfIntroduction, instructorNotes, phone1, phone2, email, isStudent, isInstructor, isAdmin);
	}

}
