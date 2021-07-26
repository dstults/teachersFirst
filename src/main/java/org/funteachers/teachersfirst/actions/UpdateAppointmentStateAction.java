package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;
import javax.ws.rs.NotAcceptableException;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class UpdateAppointmentStateAction extends ActionRunner {

	final int newState;

	public UpdateAppointmentStateAction(HttpServletRequest request, HttpServletResponse response, int newState) {
		super(request, response);

		this.newState = newState;
	}

	public void runAction() {

		// --------------------------------------------------------------------------------------------
		// INPUT VALIDATION
		// --------------------------------------------------------------------------------------------

		// Ensure valid action
		if (newState != Appointment.STATE_COMPLETED &&
			newState != Appointment.STATE_MISSED &&
			newState != Appointment.STATE_MISSED_REFUNDED &&
			newState != Appointment.STATE_CANCELLED) {

				throw new IllegalArgumentException("Unhandled newState for update appointment to.");
		}

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendJsonReply("Please sign in.");
			return;
		}

		// Attempt to parse appointment ID
		final String appointmentIdString = QueryHelpers.getPost(request, "appointmentId");
		int appointmentIdInt;
		try {
			appointmentIdInt = Integer.parseInt(appointmentIdString);
		} catch (NumberFormatException e) {
			this.sendJsonReply("Not a valid appointment ID.");
			return;
		}

		// Verify connection to DB
		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		if (appointmentDAO == null) {
			this.sendJsonReply("Could not connect to database, please try again.");
			return;
		}

		// Verify appointment exists
		final Appointment appointment = appointmentDAO.retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendJsonReply("Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Appointment must be in past unless we are cancelling
		if (newState != Appointment.STATE_CANCELLED) {
			if (!DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
				this.sendJsonReply("Cannot update state of appointments that haven't happened yet.");
				return;
			}
		}

		// Make sure is admin or instructor modifying own class
		if (!isAdmin && !(isInstructor && appointment.getIsMyAppointment(uid))) {
			this.sendJsonReply("Appointment state can only be modified by admin or instructor of class.");
			return;
		}

		// IF IS A REFUND
		if (newState == Appointment.STATE_MISSED_REFUNDED) {
			
			// Must be marked as missed
			if (appointment.getWasCompleted()) {
				this.sendJsonReply("Cannot refund appointments that weren't missed.");
				return;
			}
	
			// Must not be marked as already refunded if refunding
			if (!appointment.hasRefundableValue()) {
				this.sendJsonReply("Cannot double-refund appointments that were already refunded.");
				return;
			}

		} else {

			// Cannot update state of appointment that was refunded
			// Note: This is here because I want a different error message than above
			if (!appointment.hasRefundableValue()) {
				this.sendJsonReply("Cannot update appointment state for appointments that were refunded.");
				return;
			}

		}

		// --------------------------------------------------------------------------------------------
		// PERFORM ACTION
		// --------------------------------------------------------------------------------------------

		logger.debug("Attempting to set appointment [{}] state to [{}] ...", appointmentIdString, newState);
		String userName = QueryHelpers.getSessionValue(request, "USER_NAME", "Stranger");
		appointment.setCompletionState(newState, uid, userName);
		logger.debug("Updated appointment ID: [{}]", appointmentIdString);

		this.sendJsonReply("Appointment " + appointmentIdString + " updated!");
		return;
	}
	
}
