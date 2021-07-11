package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class MissOrCompleteAppointmentAction extends ActionRunner {

	String subAction;

	public MissOrCompleteAppointmentAction(HttpServletRequest request, HttpServletResponse response, String subAction) {
		super(request, response);
		if (subAction == null) throw new IllegalArgumentException("Invalid argument: null subAction");
		if (!subAction.equals("miss") && !subAction.equals("complete")) throw new IllegalArgumentException("Invalid argument: subAction must be 'complete' or 'miss'");
		
		this.subAction = subAction;
	}

	@Override
	public void RunAction() {

		// This should not be possible for anyone not logged in.
		if (uid <= 0) {
			this.sendPostReply("/services", "", "Please sign in or register to use this feature!");
			return;
		}

		final String appointmentIdString = QueryHelpers.getPost(request, "appointmentId");
		int appointmentIdInt;
		try {
			appointmentIdInt = Integer.parseInt(appointmentIdString);
		} catch (NumberFormatException e) {
			appointmentIdInt = 0;
		}
		final Appointment appointment = DataManager.getAppointmentDAO().retrieveByID(appointmentIdInt);
		if (appointment == null) {
			this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " not found!");
			return;
		}

		// Must be in past
		if (!DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
			this.sendPostReply("/appointments", "", "Cannot " + subAction + " appointments that haven't happened yet.");
			return;
		}

		// Make sure is admin or instructor
		if (!isAdmin && !isInstructor) {
			this.sendPostReply("/appointments", "", "Appointment can only be marked as completed or missed by an admin or an instructor.");
			return;
		}

		// Make sure can write to others (admin) or write to own (teachers)
		if (!isAdmin && !appointment.getIsMyAppointment(uid)) {
			this.sendPostReply("/appointments", "", "Cannot " + subAction + " others' appointments.");
			return;
		}

		logger.debug("Attempting to mark appointment " + appointment.getRecID() + " as '" + subAction + "' ...");
		
		appointment.setCompletionState(subAction);
		logger.debug("Deleted appointment ID: [{}]", appointmentIdString);
		
		this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " updated!");
		return;
	}
	
}
