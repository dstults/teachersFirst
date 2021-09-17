package org.funteachers.teachersfirst.actions;

import java.util.List;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.sql.MemberSqlDAO;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class AddMemberAction extends ActionRunner {

	public AddMemberAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {
		final boolean registerSelf = this.uid <= 0;
		final boolean canRegisterOther = this.isInstructor || this.isAdmin;
		if (!registerSelf && !canRegisterOther) {
			this.sendPostReply("/logout", "", "Please sign out before trying to register a new account!");
			return;
		}
		if (registerSelf && !GlobalConfig.enableOpenRegistration) {
			this.sendPostReply("/services", "", "Open registration is disabled!");
			return;
		}

		final String retryPage = registerSelf ? "/register" : "/create_user";
		final String loginName = QueryHelpers.getPost(request, "loginName");
		final String password1 = QueryHelpers.getPost(request, "password1");
		final String password2 = QueryHelpers.getPost(request, "password2");
		final String displayName = QueryHelpers.getPost(request, "displayName");
		String gender = QueryHelpers.getPost(request, "gender");
		/* String birthYear = QueryHelpers.getPost(request, "b_year");
		String birthMonth = QueryHelpers.getPost(request, "b_month");
		String birthDay = QueryHelpers.getPost(request, "b_day"); */
		final String phone1 = QueryHelpers.getPost(request, "phone1");
		final String phone2 = QueryHelpers.getPost(request, "phone2");
		final String email = QueryHelpers.getPost(request, "email");

		final String retryString = "loginName=" + loginName + "&displayName=" + displayName + "&gender=" + gender + "&phone1=" + phone1 + "&phone2=" + phone2 + "&email=" + email;

		// TODO: Should check clientside to make sure string input does not exceed database lengths
		if (loginName.isEmpty()) {
			this.sendPostReply(retryPage, retryString, "Please provide a valid login name.");
			return;
		}
		if (password1.isEmpty()) {
			this.sendPostReply(retryPage, retryString, "Please provide a valid password.");
			return;
		}
		if (displayName.isEmpty()) {
			this.sendPostReply(retryPage, retryString, "Please provide a valid display name.");
			return;
		}
		if (password2.isEmpty() || !password2.equals(password1)) {
			this.sendPostReply(retryPage, retryString, "Passwords do not match!");
			return;
		}
		// trim and lcase gender string -- if it's not empty, which is valid
		if (gender.length() > 0) gender = gender.toLowerCase().substring(0, 1);
		if (!gender.equals("m") && !gender.equals("f") && !gender.equals("")) {
			this.sendPostReply(retryPage, retryString, "Please provide a valid gender (m/f/blank).");
			return;
		}

		// Check if database is connected:
		final MemberSqlDAO memberDAO = (MemberSqlDAO) this.connectionPackage.getMemberDAO(this.getClass().getSimpleName());
		if (memberDAO == null) {
			this.sendPostReply(retryPage, retryString, "Sorry, there's been a catastrophic database failure. Please try again later.");
			return;
		}

		// All checks passed, start the action...
		logger.debug(" Attempting to register [ {} ]...", displayName);
		
		// Making sure unique login name
		List<Member> members = memberDAO.retrieveAll();
		for (Member member : members) {
			if (member.getLoginName() == loginName) {
				this.sendPostReply(retryPage, retryString, "Login name '" + loginName + "' already taken, please try another.");
				return;
			}
		}

		// TODO: Birthdates

		final Member member = new Member(loginName, displayName, 0, gender, "", "", phone1, phone2, email, false, false, true);
		final int newMemberRecordId = memberDAO.insert(member);
		member.setRecID(newMemberRecordId);
		logger.info(memberDAO.size() + " records total");
		logger.debug("Registered new member: [{}]", member);

		// Add password hash to database
		memberDAO.updatePassword(member, password1);

		// Finalize as self-register or reigster-other
		if (registerSelf) {
			// Log new user in, giving token cookie
			security.login(member);
			this.sendPostReply("/appointments", "", "Welcome new user!");
		} else {
			this.sendPostReply("/members", "", "New user (" + member.getDisplayName() + ") registered with ID: " + newMemberRecordId);
		}
		return;
	}
	
}
