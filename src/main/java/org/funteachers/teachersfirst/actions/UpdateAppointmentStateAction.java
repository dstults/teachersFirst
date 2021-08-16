package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class UpdateAppointmentStateAction extends ActionRunner {

	final int newState;

	public UpdateAppointmentStateAction(HttpServletRequest request, HttpServletResponse response, Security security, int newState) {
		super(request, response, security);

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
			this.sendJsonMessage("Please sign in.");
			return;
		}

		// Attempt to parse appointment ID
		final String appointmentIdString = QueryHelpers.getPost(request, "appointmentId");
		int appointmentIdInt;
		try {
			appointmentIdInt = Integer.parseInt(appointmentIdString);
		} catch (NumberFormatException e) {
			this.sendJsonMessage("Not a valid appointment ID.");
			return;
		}

		// Verify connection to DB
		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		if (appointmentDAO == null) {
			this.sendJsonMessage("Could not connect to database, please try again.");
			return;
		}

		// Verify appointment exists
		final Appointment appointment = appointmentDAO.retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendJsonMessage("Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Appointment must be in past unless we are cancelling
		if (newState != Appointment.STATE_CANCELLED) {
			if (!DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
				this.sendJsonMessage("Cannot update state of appointments that haven't happened yet.");
				return;
			}
		}

		// Make sure is admin or instructor modifying own class
		if (!isAdmin && !(isInstructor && appointment.getIsMyAppointment(uid))) {
			this.sendJsonMessage("Appointment state can only be modified by admin or instructor of class.");
			return;
		}

		// IF IS A REFUND
		if (newState == Appointment.STATE_MISSED_REFUNDED) {
			
			// Must be marked as missed
			if (appointment.getWasCompleted()) {
				this.sendJsonMessage("Cannot refund appointments that weren't missed.");
				return;
			}
	
			// Must not be marked as already refunded if refunding
			if (!appointment.hasRefundableValue()) {
				this.sendJsonMessage("Cannot double-refund appointments that were already refunded.");
				return;
			}

		} else {

			// Cannot update state of appointment that was refunded
			// Note: This is here because I want a different error message than above
			if (!appointment.hasRefundableValue()) {
				this.sendJsonMessage("Cannot update appointment state for appointments that were refunded.");
				return;
			}

		}

		// --------------------------------------------------------------------------------------------
		// PERFORM ACTION
		// --------------------------------------------------------------------------------------------

		logger.debug("Attempting to set appointment [{}] state to [{}] ...", appointmentIdString, newState);
		appointment.setCompletionState(newState, uid, operator.getLoginName());
		logger.debug("Updated appointment ID: [{}]", appointmentIdString);

		final String operationWord;
		switch (newState) {
			case Appointment.STATE_CANCELLED:
				operationWord = "cancelled";
				break;
			case Appointment.STATE_COMPLETED:
				operationWord = "completed";
				break;
			case Appointment.STATE_MISSED:
				operationWord = "missed";
				break;
			case Appointment.STATE_MISSED_REFUNDED:
				operationWord = "refunded";
				break;
			default:
				operationWord = "marked as state [" + newState + "]";
				break;
		}
		this.sendJsonMessage("Appointment " + appointmentIdString + " " + operationWord + "!");
		return;
	}
	
}
