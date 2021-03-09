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
		String loginName = getPostValue("loginName", "");
		String password1 = getPostValue("password", "");
		String password2 = getPostValue("confirm_password", "");

		String displayName = getPostValue("name", "");

		String gender = getPostValue("gender", "");

		/* String birthYear = getPostValue("b_year", "");
		String birthMonth = getPostValue("b_month", "");
		String birthDay = getPostValue("b_day", ""); */

		String phone1 = getPostValue("phone1", "");
		String phone2 = getPostValue("phone2", "");
		String email = getPostValue("email", "");

		//TODO: Must check to make sure string input does not exceed database lengths
		if (loginName.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid login name.");
			return;
		}
		if (password1.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid password.");
			return;
		}
		if (displayName.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid display name.");
			return;
		}
		if (password2.isEmpty() || !password2.equals(password1)) {
			this.SendRedirectToPage("/register?message=Passwords do not match!");
			return;
		}
		gender = gender.toLowerCase().substring(0, 1);
		if (!gender.equals("m") && !gender.equals("f") && !gender.equals("")) {
			this.SendRedirectToPage("/register?message=Please provide a valid gender (m/f/blank).");
			return;
		}
		if (phone1 == null) phone1 = "";
		if (phone2 == null) phone2 = "";
		if (email == null) email = "";

		logger.debug(displayName + " attempting to register...");
		
		// Making sure unique login name
		List<Member> members = DataManager.getMemberDAO().retrieveAll();
		for (Member member : members) {
			if (member.getLoginName() == loginName) {
				this.SendRedirectToPage("/register?message=Login name '" + loginName + "' already taken, please try another.");
				return;
			}
		}

		//TODO: Hash password either in JS or here
		Member member = new Member(loginName, password1, displayName, null, gender, "", phone1, phone2, email, true, false, false);
		DataManager.getMemberDAO().insert(member);
		logger.info(DataManager.getMemberDAO().size() + " records total");
		logger.debug("Registered new member: [{}]", member);
		
		// Log user into session
		Security.login(request, member);
		this.SendRedirectToPage("/appointments");
		return;
	}
	
}
