package edu.lwtech.csd297.teachersfirst.actions;

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
		String name = getPostValue("name", "");
		String gender = getPostValue("gender", "");
		String phone1 = getPostValue("phone1", "");
		String phone2 = getPostValue("phone2", "");
		String email = getPostValue("email", "");

		//TODO: Must check to make sure string input does not exceed database lengths
		if (loginName == null || loginName == "") {
			this.SendRedirectToPage("/register?message=Please provide a valid login name.");
			return;
		}
		if (password1 == null || password1 == "") {
			this.SendRedirectToPage("/register?message=Please provide a valid password.");
			return;
		}
		if (name == null || name == "") {
			this.SendRedirectToPage("/register?message=Please provide a valid display name.");
			return;
		}
		if (password2 == null || password2 == "" || !password2.equals(password1)) {
			this.SendRedirectToPage("/register?message=Passwords do not match!");
			return;
		}
		if (gender == null)
			this.SendRedirectToPage("/register?message=Please provide a valid gender (m/f/blank).");
		gender = gender.toLowerCase().substring(0, 1);
		if (!gender.equals("m") && !gender.equals("f") && !gender.equals("")) {
			this.SendRedirectToPage("/register?message=Please provide a valid gender (m/f/blank).");
			return;
		}
		if (phone1 == null) phone1 = "";
		if (phone2 == null) phone2 = "";
		if (email == null) email = "";

		logger.debug(name + " attempting to register in with password: " + password1);
		if (Security.checkPassword(1, "Password01")) {
			//TODO: Hash password either in JS or here
			Member member = new Member(loginName, password1, name, null, gender, "", phone1, phone2, email, true, false, false);
			DataManager.getMemberDAO().insert(member);
			logger.info(DataManager.getMemberDAO().size() + " records total");
			logger.debug("Registered new member: [{}]", member);
			request.getSession().setAttribute("USER_ID", 1);
			request.getSession().setAttribute("USER_NAME", name);
			this.SendRedirectToPage("/appointments");
			return;
		} else {
			this.SendRedirectToPage("/login?name=" + name + "&message=Could not log you in.");
			return;
		}
	}
	
}
