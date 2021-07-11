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
		final String newText = text.replaceAll("[^\\+\\-\\=\\@\\.\\'\\\"\\ \\:\\!\\?\\,\\:\\;\\_a-zA-Z\\d]", "");
		if (text != newText)
			logger.debug(text + " => changed to => " + newText);
		return newText;
	}

	@Override
	public void RunAction() {

		if (uid <= 0) {
			this.sendJsonReply("You must be signed in to do this!");
			return;
		}

		final String memberIdRaw = QueryHelpers.getPost(request, "memberId");
		final int memberId;
		try {
			memberId = Integer.parseInt(memberIdRaw);
		} catch (NumberFormatException e) {
			this.sendJsonReply("Couldn't parse the memberId!");
			return;
		}
		if (memberId <= 0) {
			this.sendJsonReply("Invalid memberId!");
			return;
		}
		Member member = DataManager.getMemberDAO().retrieveByID(memberId);
		if (member == null) {
			this.sendJsonReply("Invalid memberId!");
			return;
		}
		final boolean memberIsStudent = member.getIsStudent();

		// Get string elements from post
		final String creditsRaw = removeInvalidText(QueryHelpers.getPost(request, "credits", String.valueOf(member.getCredits())));
		final String phone1 = removeInvalidText(QueryHelpers.getPost(request, "phone1", member.getPhone1()));
		final String phone2 = removeInvalidText(QueryHelpers.getPost(request, "phone2", member.getPhone2()));
		final String email = removeInvalidText(QueryHelpers.getPost(request, "email", member.getEmail()));
		final String selfIntroduction = removeInvalidText(QueryHelpers.getPost(request, "selfIntroduction", member.getSelfIntroduction()));
		final String instructorNotes = removeInvalidText(QueryHelpers.getPost(request, "instructorNotes", member.getInstructorNotes()));

		// Parse integer elements from post
		final float credits;
		try {
			credits = Float.parseFloat(creditsRaw);
		} catch (NumberFormatException e) {
			this.sendJsonReply("Couldn't parse credits: [ " + creditsRaw + " ]!");
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
			this.sendJsonReply("Operation not allowed.");
			return;
		}

		boolean changesMade = false;
		if (credits != member.getCredits()) {
			member.setCredits(credits);
			changesMade = true;
		}
		if (phone1 != member.getPhone1()) {
			member.setPhone1(phone1);
			changesMade = true;
		}
		if (phone2 != member.getPhone2()) {
			member.setPhone2(phone2);
			changesMade = true;
		}
		if (email != member.getEmail()) {
			member.setEmail(email);
			changesMade = true;
		}
		if (selfIntroduction != member.getSelfIntroduction()) {
			member.setSelfIntroduction(selfIntroduction);
			changesMade = true;
		}
		if (instructorNotes != member.getInstructorNotes()) {
			member.setInstructorNotes(instructorNotes);
			changesMade = true;
		}
		if (!changesMade) {
			this.sendJsonReply("No changes detected, aborting!");
			return;
		}

		//Member member2 = DataManager.getMemberDAO().retrieveByID(memberId);
		//this.sendJsonReply("Success!//" + member2.toString() + "//Changed to://" + member.toString());

		member.update();
		logger.debug("Updated: [{}]", member);
		
		// Log user into session
		this.sendJsonReply("Success!");
		return;
	}
	
}
