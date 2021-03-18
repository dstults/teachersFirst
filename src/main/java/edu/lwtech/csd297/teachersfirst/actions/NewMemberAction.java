package edu.lwtech.csd297.teachersfirst.actions;

import java.util.List;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class NewMemberAction extends ActionRunner {

	public NewMemberAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// This version of this process requites that you're not signed in.
		if (uid > 0) {
			this.SendPostReply("/services", "", "Please sign out before trying to register a new account!");
			return;
		}

		String loginName = QueryHelpers.getPost(request, "loginName");
		String password1 = QueryHelpers.getPost(request, "password1");
		String password2 = QueryHelpers.getPost(request, "password2");
		String displayName = QueryHelpers.getPost(request, "displayName");
		String gender = QueryHelpers.getPost(request, "gender");
		/* String birthYear = QueryHelpers.getPost(request, "b_year");
		String birthMonth = QueryHelpers.getPost(request, "b_month");
		String birthDay = QueryHelpers.getPost(request, "b_day"); */
		String phone1 = QueryHelpers.getPost(request, "phone1");
		String phone2 = QueryHelpers.getPost(request, "phone2");
		String email = QueryHelpers.getPost(request, "email");

		final String retryString = "loginName=" + loginName + "&displayName=" + displayName + "&gender=" + gender + "&phone1=" + phone1 + "&phone2=" + phone2 + "&email=" + email;

		//TODO: Must check to make sure string input does not exceed database lengths
		if (loginName.isEmpty()) {
			this.SendPostReply("/register", retryString, "Please provide a valid login name.");
			return;
		}
		if (password1.isEmpty()) {
			this.SendPostReply("/register", retryString, "Please provide a valid password.");
			return;
		}
		if (displayName.isEmpty()) {
			this.SendPostReply("/register", retryString, "Please provide a valid display name.");
			return;
		}
		if (password2.isEmpty() || !password2.equals(password1)) {
			this.SendPostReply("/register", retryString, "Passwords do not match!");
			return;
		}
		// trim and lcase gender string -- if it's not empty, which is valid
		if (gender.length() > 0) gender = gender.toLowerCase().substring(0, 1);
		if (!gender.equals("m") && !gender.equals("f") && !gender.equals("")) {
			this.SendPostReply("/register", retryString, "Please provide a valid gender (m/f/blank).");
			return;
		}

		logger.debug(displayName + " attempting to register...");
		
		// Making sure unique login name
		List<Member> members = DataManager.getMemberDAO().retrieveAll();
		for (Member member : members) {
			if (member.getLoginName() == loginName) {
				this.SendPostReply("/register", retryString, "Login name '" + loginName + "' already taken, please try another.");
				return;
			}
		}

		//TODO: Hash password either in JS or here
		//TODO: Birthdates

		//Member member = new Member(loginName, password1, displayName, birthdate, gender, "", phone1, phone2, email, true, false, false);
		Member member = new Member(loginName, password1, displayName, gender, "", phone1, phone2, email, true, false, false);
		int newMemberRecordId = DataManager.getMemberDAO().insert(member);
		member.setRecID(newMemberRecordId);
		logger.info(DataManager.getMemberDAO().size() + " records total");
		logger.debug("Registered new member: [{}]", member);
		
		// Log user into session
		Security.login(request, member);
		this.SendPostReply("/appointments", "", "Welcome new user!");
		return;
	}
	
}
