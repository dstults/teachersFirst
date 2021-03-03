package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class NewMemberAction extends ActionRunner {

	public NewMemberAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {
		String name = getPostValue("name", "");
		String password1 = getPostValue("password", "");
		String password2 = getPostValue("confirm_password", "");
		String age = getPostValue("age", "");
		int ageVal = Integer.parseInt(age);
		String gender = getPostValue("gender", "");
		String food = getPostValue("food", "");
		String color = getPostValue("color", "");

		if (name == null || name == "" || password1 == null || password1 == "") {
			this.SendRedirectToPage("/register?message=Please enter a valid user name and password.");
			return;
		}
		if (password2 == null || password2 == "" || !password2.equals(password1)) {
			this.SendRedirectToPage("/register?message=Passwords do not match!");
			return;
		}
		if (ageVal < 4 || ageVal > 130) {
			this.SendRedirectToPage("/register?message=Age must be between 4 and 130.");
			return;
		}
		if (gender == null || (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Unset"))) {
			this.SendRedirectToPage("/register?message=Please enter a valid gender.");
			return;
		}
		if (food == null || food == "" || color == null || color == "") {
			this.SendRedirectToPage("/register?message=Please enter a favorite food and color.");
			return;
		}

		logger.debug(name + " attempting to register in with password: " + password1);
		if (Security.checkPassword(1, "Password01")) {
			Member member = new Member(name, ageVal, gender, color, food, true, false, false);
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
