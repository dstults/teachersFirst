package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class UpdateMemberAction extends ActionRunner {

	public UpdateMemberAction(ConnectionPackage cp) { super(cp); }

	private String removeInvalidText(int maxLength, String text) {
		text = text.trim();
		String newText = text.replaceAll("[^\\+\\-\\=\\@\\.\\'\\\"\\ \\:\\!\\?\\,\\:\\;\\_a-zA-Z\\d]", "");
		if (text != newText) {
			logger.debug("Invalid input detected! {} => changed to => {}", text, newText);
		}
		if (newText.length() > maxLength) {
			logger.debug("Input too long! Shortening to [{}] characters!", maxLength);
			newText = newText.substring(0, maxLength);
		}
		return newText;
	}

	@Override
	public void runAction() {

		// Check if logged in
		if (uid <= 0) {
			this.sendJsonMessage("You must be signed in to do this!", false);
			return;
		}

		// Check whether member ID is parseable
		final String memberIdRaw = QueryHelpers.getPost(request, "memberId");
		final int memberId;
		try {
			memberId = Integer.parseInt(memberIdRaw);
		} catch (NumberFormatException e) {
			this.sendJsonMessage("Couldn't parse the memberId!", false);
			return;
		}
		if (memberId <= 0) {
			this.sendJsonMessage("Invalid memberId!", false);
			return;
		}
		
		// Check connection to database
		DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().toString());
		if (memberDAO == null) {
			this.sendJsonMessage("Error connecting to database, try again!", false);
			return;
		}

		// Ensure member exists
		Member member = memberDAO.retrieveByID(memberId);
		if (member == null) {
			this.sendJsonMessage("Invalid memberId!", false);
			return;
		}
		final boolean memberIsStudent = member.getIsStudent();

		// Get string elements from post
		final String creditsRaw = removeInvalidText(12, QueryHelpers.getPost(request, "credits", String.valueOf(member.getCredits())));
		final String phone1 = removeInvalidText(20, QueryHelpers.getPost(request, "phone1", member.getPhone1()));
		final String phone2 = removeInvalidText(20, QueryHelpers.getPost(request, "phone2", member.getPhone2()));
		final String email = removeInvalidText(40, QueryHelpers.getPost(request, "email", member.getEmail()));
		final String selfIntroduction = removeInvalidText(800, QueryHelpers.getPost(request, "selfIntroduction", member.getSelfIntroduction()));
		final String instructorNotes = removeInvalidText(800, QueryHelpers.getPost(request, "instructorNotes", member.getInstructorNotes()));

		// Parse integer elements from post
		final float credits;
		try {
			credits = Float.parseFloat(creditsRaw);
		} catch (NumberFormatException e) {
			this.sendJsonMessage("Couldn't parse credits: [ " + creditsRaw + " ]!", false);
			return;
		}

		// To make changes, must be an admin, an instructor, or self.
		boolean actionAllowed = isAdmin || isInstructor || isStudent && memberId == uid;
		// To make changes to credits: (1) self is admin or instructor, (2) target not self, (3) target is a student
		if (credits != member.getCredits()) {
			actionAllowed = (isAdmin || isInstructor) && memberId != uid && memberIsStudent;
		}
		// To make changes to instructor notes: (1) self is admin or instructor
		if (instructorNotes != member.getInstructorNotes()) {
			actionAllowed = isAdmin || isInstructor;
		}
		// Abort operation if not allowed.
		if (!actionAllowed) {
			this.sendJsonMessage("Operation not allowed.", false);
			return;
		}

		// Check to see if any changes made
		boolean changesMade = false;
		boolean useGeneralUpdate = false;
		if (credits != member.getCredits()) {
			logger.debug("Changed member credits from [ {} ] to [ {} ]", member.getCredits(), credits);
			member.setCredits(this.connectionPackage, uid, operator.getLoginName(), "manual update", credits);
			changesMade = true;
			//useGeneralUpdate = useGeneralUpdate;
		}
		if (!phone1.equals(member.getPhone1())) {
			logger.debug("Changed member phone1 from [ {} ] to [ {} ]", member.getPhone1(), phone1);
			member.setPhone1(phone1);
			changesMade = true;
			useGeneralUpdate = true;
		}
		if (!phone2.equals(member.getPhone2())) {
			logger.debug("Changed member phone2 from [ {} ] to [ {} ]", member.getPhone2(), phone2);
			member.setPhone2(phone2);
			changesMade = true;
			useGeneralUpdate = true;
		}
		if (!email.equals(member.getEmail())) {
			logger.debug("Changed member email from [ {} ] to [ {} ]", member.getEmail(), email);
			member.setEmail(email);
			changesMade = true;
			useGeneralUpdate = true;
		}
		if (!selfIntroduction.equals(member.getSelfIntroduction())) {
			logger.debug("Changed member self-introduction!"); // too long to log fully
			member.setSelfIntroduction(selfIntroduction);
			changesMade = true;
			useGeneralUpdate = true;
		}
		if (!instructorNotes.equals(member.getInstructorNotes())) {
			logger.debug("Changed member instructor notes!"); // too long to log fully
			member.setInstructorNotes(instructorNotes);
			changesMade = true;
			useGeneralUpdate = true;
		}
		if (!changesMade) {
			this.sendJsonMessage("No changes detected, aborting!", false);
			return;
		}

		//Member member2 = DataManager.getMemberDAO().retrieveByID(memberId);
		//this.sendJsonReply("Success!//" + member2.toString() + "//Changed to://" + member.toString());

		if (useGeneralUpdate) {
			member.update(this.connectionPackage);
			logger.debug("Updated: [{}]", member);
		}
		
		// Log user in
		this.sendJsonMessage("Success!", true);
		return;
	}
	
}
