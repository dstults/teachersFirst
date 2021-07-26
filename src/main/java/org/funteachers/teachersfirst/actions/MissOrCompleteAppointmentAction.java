package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;
import javax.ws.rs.NotAcceptableException;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class MissOrCompleteAppointmentAction extends ActionRunner {

	public MissOrCompleteAppointmentAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void runAction() {
		throw new NotAcceptableException("Use RunAction(int) with sub-action as int value.");
	}

	public void runAction(final int newState) {

		// Ensure valid action
		if (newState != Appointment.STATE_COMPLETED && newState != Appointment.STATE_MISSED && newState != Appointment.STATE_MISSED_REFUNDED) {
			throw new IllegalArgumentException("Invalid argument: subAction must be 'complete' or 'miss' or 'refund'");
		}

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}

		// Check for a valid appointment ID
		final String appointmentIdString = QueryHelpers.getPost(request, "appointmentId");
		int appointmentIdInt;
		try {
			appointmentIdInt = Integer.parseInt(appointmentIdString);
		} catch (NumberFormatException e) {
			this.sendPostReply("/appointments", "", "Not a valid appointment ID.");
			return;
		}

		// Verify connection to DB
		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		if (appointmentDAO == null) {
			this.sendPostReply("/appointments", "", "Couldn't connect to database, please try again.");
			return;
		}

		// Verify appointment exists
		final Appointment appointment = appointmentDAO.retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Appointment must be in past
		if (!DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
			this.sendPostReply("/appointments", "", "Cannot update state of appointments that haven't happened yet.");
			return;
		}

		// Make sure is admin or instructor modifying own class
		if (!isAdmin && !(isInstructor && appointment.getIsMyAppointment(uid))) {
			this.sendPostReply("/appointments", "", "Appointment state can only be modified by admin or instructor of class.");
			return;
		}

		// IF IS A REFUND
		if (newState == Appointment.STATE_MISSED_REFUNDED) {
			
			// Must be marked as missed
			if (appointment.getWasCompleted()) {
				this.sendPostReply("/appointments", "", "Cannot refund appointments that weren't missed.");
				return;
			}
	
			// Must not be marked as already refunded if refunding
			if (appointment.getWasRefunded()) {
				this.sendPostReply("/appointments", "", "Cannot double-refund appointments that were already refunded.");
				return;
			}

		} else {

			// Cannot update state of appointment that was refunded
			// Note: This is here because I want a different error message than above
			if (appointment.getWasRefunded()) {
				this.sendPostReply("/appointments", "", "Cannot update appointment state for appointments that were refunded.");
				return;
			}

		}

		logger.debug("Attempting to set appointment [{}] state to [{}] ...", appointmentIdString, newState);
		appointment.setCompletionState(newState);
		logger.debug("Updated appointment ID: [{}]", appointmentIdString);
		
		this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " updated!");
		return;
	}
	
}
