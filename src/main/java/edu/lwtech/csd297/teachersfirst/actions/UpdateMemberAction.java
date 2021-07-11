package edu.lwtech.csd297.teachersfirst.actions;

import java.util.List;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class UpdateMemberAction extends ActionRunner {

	public UpdateMemberAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	private String removeInvalidText(String text) {
		text = text.trim();
		final String newText = text.replaceAll("[^\\+\\-\\@\\.\\ \\:\\!\\?\\_a-zA-Z\\d]", "");
		if (text != newText)
			logger.debug(text + " => changed to => " + newText);
		return newText;
	}

	@Override
	public void RunAction() {

		if (uid <= 0) {
			this.SendPostReply("/message", "", "You must be signed in to do this!");
			return;
		}

		final String memberIdRaw = QueryHelpers.getPost(request, "memberId");
		final int memberId;
		try {
			memberId = Integer.parseInt(memberIdRaw);
		} catch (NumberFormatException e) {
			this.SendPostReply("/message", "", "Couldn't parse the memberId!");
			return;
		}
		if (memberId <= 0) {
			this.SendPostReply("/message", "", "Invalid memberId!");
			return;
		}
		Member member = DataManager.getMemberDAO().retrieveByID(memberId);
		if (member == null) {
			this.SendPostReply("/message", "", "Invalid memberId!");
			return;
		}
		final boolean memberIsStudent = member.getIsStudent();

		// Get string elements from post
		final String creditsRaw = removeInvalidText(QueryHelpers.getPost(request, "credits", String.valueOf(member.getCredits())));
		final String phone1 = removeInvalidText(QueryHelpers.getPost(request, "phone1", member.getPhone1()));
		final String phone2 = removeInvalidText(QueryHelpers.getPost(request, "phone2", member.getPhone2()));
		final String email = removeInvalidText(QueryHelpers.getPost(request, "email", member.getEmail()));
		final String selfIntroduction = removeInvalidText(QueryHelpers.getPost(request, "intro", member.getSelfIntroduction()));
		final String instructorNotes = removeInvalidText(QueryHelpers.getPost(request, "notes", member.getInstructorNotes()));
		if (creditsRaw == String.valueOf(member.getCredits()) &&
			phone1 == member.getPhone1() &&
			phone2 == member.getPhone2() &&
			email == member.getEmail() &&
			selfIntroduction == member.getSelfIntroduction() &&
			instructorNotes == member.getInstructorNotes()) {

			this.SendPostReply("/message", "", "No changes detected, aborting!");
			return;
		}

		// Parse integer elements from post
		final int credits;
		try {
			credits = Integer.parseInt(creditsRaw);
		} catch (NumberFormatException e) {
			this.SendPostReply("/message", "", "Couldn't parse the credits!");
			return;
		}

		// To make changes, must be an admin, an instructor, or self.
		boolean actionAllowed = isAdmin || isInstructor || isStudent && memberId == uid;
		// To make changes to credits: (1) self is admin or instructor, (2) target not self, (3) target is a student
		if (member.getCredits() != credits) {
			actionAllowed = (isAdmin || isInstructor) && memberId != uid && memberIsStudent;
		}
		// To make changes to instructor notes: (1) self is admin or instructor
		if (member.getCredits() != credits) {
			actionAllowed = isAdmin || isInstructor;
		}
		// Abort operation if not allowed.
		if (!actionAllowed) {
			this.SendPostReply("/message", "", "Operation not allowed.");
			return;
		}

		if (!isAdmin || !isInstructor || (isStudent)) {
			this.SendPostReply("/message", "", "Action not allowed.");
			return;
		}

		//Member member = new Member(loginName, password1, displayName, birthdate, gender, "", phone1, phone2, email, true, false, false);		
		member.update();
		logger.debug("Updated: [{}]", member);
		
		// Log user into session
		this.SendPostReply("/message", "", "Success!");
		return;
	}
	
}
