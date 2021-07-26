package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.obj.*;

public class DeleteAppointmentAction extends ActionRunner {

	public DeleteAppointmentAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void runAction() {

		// --------------------------------------------------------------------------------------------
		// INPUT VALIDATION
		// --------------------------------------------------------------------------------------------

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

		// Check connection to database
		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		if (appointmentDAO == null) {
			this.sendJsonReply("Could not connect to database, please try again.");
			return;
		}

		// Check that appointment exists
		final Appointment appointment = appointmentDAO.retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendJsonReply("Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Must be admin or appointment must be cancelled
		if (!isAdmin && !appointment.canBeDeleted()) {
			this.sendJsonReply("Not enough privileges / Cannot delete appointment of this state.");
			return;
		}

		if (!isAdmin) {

			// Make sure deleting party is the instructor for the class
			if (appointment.getInstructorID() != uid) {
				this.sendJsonReply("Not appointment instructor, cannot delete.");
				return;
			}

			// Only admin can delete past appointments
			if (DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
				this.sendJsonReply("Need admin privileges to delete past appointments.");
				return;
			}

		}
		
		// --------------------------------------------------------------------------------------------
		// PERFORM ACTION
		// --------------------------------------------------------------------------------------------

		// Check whether to refund after appointment deletion
		final boolean giveRefund = appointment.hasRefundableValue();

		// Delete the appointment
		logger.debug("Attempting to delete appointment " + appointment.toString() + " ...");
		appointmentDAO.delete(appointmentIdInt);
		//logger.info(DataManager.getAppointmentDAO().size() + " records total");
		logger.debug("Deleted appointment ID: [{}]", appointmentIdString);
		
		// Update credits for student when applicable
		if (giveRefund) {
			final DAO<Member> memberDAO = DataManager.getMemberDAO();
			final Member student = memberDAO.retrieveByID(appointment.getStudentID());
			final float length = appointment.getLength();
			final float credits = student.getCredits() + length;
			final String opName = QueryHelpers.getSessionValue(request, "USER_NAME", "Stranger");
			student.setCredits(uid, opName, "delete appointment[" + appointmentIdInt + "] len=" + appointment.getLength() + " hrs", credits);
		}

		// Send response
		String refundText = giveRefund ? " Credit(s) refunded!" : " No credits refunded.";
		this.sendJsonReply("Appointment " + appointmentIdString + " deleted!" + refundText);
		return;
	}
	
}
