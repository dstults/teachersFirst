package edu.lwtech.csd297.teachersfirst.actions;

import java.util.List;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class NewOpeningsAction extends ActionRunner {

	public NewOpeningsAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// This should not be possible for anyone not logged in.
		final int uid = Security.getUserId(request);
		if (uid <= 0) {
			this.SendRedirectToPage("/services?message=Please sign in or register to use those features!");
			return;
		}

		String instructorName = QueryHelpers.getPost(request, "instructor");
		String startDate = QueryHelpers.getPost(request, "startDate");
		String endDate = QueryHelpers.getPost(request, "startDate");
		String daysOfWeek = QueryHelpers.getPost(request, "daysOfWeek"); // SuMoTuWdThFrSa
		String startTime = QueryHelpers.getPost(request, "startTime");
		String endTime = QueryHelpers.getPost(request, "endTime");

		//TODO: Must check to make sure string input does not exceed database lengths
		if (instructorName.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid instructor name.");
			return;
		}
		if (startDate.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid start date.");
			return;
		}
		if (endDate.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid end date.");
			return;
		}
		if (daysOfWeek.isEmpty() ) {
			this.SendRedirectToPage("/register?message=Please provide days of the week.");
			return;
		}
		if (startTime.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid start time.");
			return;
		}
		if (endTime.isEmpty()) {
			this.SendRedirectToPage("/register?message=Please provide a valid end time.");
			return;
		}

		logger.debug("Attempting to create batch openings ...");
		

		return;
	}
	
}
