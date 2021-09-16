package org.funteachers.teachersfirst.actions;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class DeleteOpeningAction extends ActionRunner {

	public DeleteOpeningAction(ConnectionPackage cp) { super(cp); }

	@Override
	public void runAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendPostReply("/openings", "", "Please sign in or register to use this feature!");
			return;
		}

		final String openingIdString = QueryHelpers.getPost(request, "openingId");
		int openingIdInt;
		try {
			openingIdInt = Integer.parseInt(openingIdString);
		} catch (NumberFormatException e) {
			openingIdInt = 0;
		}
		final Opening opening = this.connectionPackage.getOpeningDAO(this.getClass().getSimpleName()).retrieveByID(openingIdInt);
		if (opening == null) {
			this.sendPostReply("/openings", "", "Opening %5B" + openingIdString + "%5D not found!");
			return;
		}

		// Make sure the person has the authority
		if (!Permissions.MemberCanDeleteOpening(this.operator, opening)) {
			this.sendPostReply("/openings", "", "Not your opening, cannot delete.");
			return;
		}

		logger.debug("Attempting to delete opening " + opening.toString() + " ...");
		
		DAO<Opening> openingDAO = this.connectionPackage.getOpeningDAO(this.getClass().getSimpleName());
		openingDAO.delete(openingIdInt);
		//logger.info(DataManager.getOpeningDAO().size() + " records total");
		logger.debug("Deleted opening ID: [{}]", openingIdInt);
		
		this.sendPostReply("/openings", "", "Opening %5B" + openingIdString + "%5D, deleted!");
		return;
	}
	
}
