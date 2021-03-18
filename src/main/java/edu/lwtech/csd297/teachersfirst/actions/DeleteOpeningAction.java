package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DeleteOpeningAction extends ActionRunner {

	public DeleteOpeningAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.SendPostReply("/openings", "", "Please sign in or register to use this feature!");
			return;
		}

		final String openingIdString = QueryHelpers.getPost(request, "openingId");
		int openingIdInt;
		try {
			openingIdInt = Integer.parseInt(openingIdString);
		} catch (NumberFormatException e) {
			openingIdInt = 0;
		}
		final Opening opening = DataManager.getOpeningDAO().retrieveByID(openingIdInt);
		if (opening == null) {
			this.SendPostReply("/openings", "", "Opening %5B" + openingIdString + "%5D not found!");
			return;
		}

		// Make sure the person has the authority
		if (!isAdmin && opening.getInstructorID() != uid) {
			this.SendPostReply("/openings", "", "Not your opening, cannot delete.");
			return;
		}

		logger.debug("Attempting to delete opening " + opening.toString() + " ...");
		
		DataManager.getOpeningDAO().delete(openingIdInt);
		//logger.info(DataManager.getOpeningDAO().size() + " records total");
		logger.debug("Deleted opening ID: [{}]", openingIdInt);
		
		this.SendPostReply("/openings", "", "Opening %5B" + openingIdString + "%5D deleted!");
		return;
	}
	
}
