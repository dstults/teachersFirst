package org.funteachers.teachersfirst.actions;

import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.daos.sql.*;
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
			this.sendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}

		// Attempt to parse appointment ID
		final String appointmentIdString = QueryHelpers.getPost(request, "appointmentId");
		int appointmentIdInt;
		try {
			appointmentIdInt = Integer.parseInt(appointmentIdString);
		} catch (NumberFormatException e) {
			appointmentIdInt = 0;
		}

		// Check connection to database
		final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
		if (appointmentDAO == null) {
			this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Check that appointment exists
		final Appointment appointment = appointmentDAO.retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Can only delete cancelled appointments
		if (appointment.getCompletionState() != Appointment.STATE_CANCELLED) {
			this.sendPostReply("/appointments", "", "Can only delete cancelled appointments.");
			return;
		}

		if (!isAdmin) {

			// Make sure is at least an instructor
			if (!isInstructor) {
				this.sendPostReply("/appointments", "", "Not enough privileges to delete appointments.");
				return;
			}

			// Make sure deleting party is the instructor for the class
			if (appointment.getInstructorID() != uid) {
				this.sendPostReply("/appointments", "", "Not appointment instructor, cannot delete.");
				return;
			}

			// Only admin can delete past appointments
			if (DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
				this.sendPostReply("/appointments", "", "Need admin privileges to delete past appointments.");
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
			float credits = student.getCredits();
			float length = appointment.getLength();
			credits += length;
			String opName = QueryHelpers.getSessionValue(request, "USER_NAME", "Stranger");
			student.setCredits(uid, opName, "delete appointment[" + appointmentIdInt + "] len=" + appointment.getLength() + " hrs", credits);
			memberDAO.update(student);
		}

		// Send response
		String refundText = giveRefund ? " Credit(s) refunded!" : " No credits refunded.";
		this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " deleted!" + refundText);
		return;
	}
	
}
