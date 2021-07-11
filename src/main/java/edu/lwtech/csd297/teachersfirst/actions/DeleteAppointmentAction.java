package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.obj.*;

public class DeleteAppointmentAction extends ActionRunner {

	public DeleteAppointmentAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

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

		// Make sure the person has the authority
		if (!isAdmin && !appointment.getIsMyAppointment(uid)) {
			this.sendPostReply("/appointments", "", "Not your appointment, cannot cancel.");
			return;
		}

		// Make sure even if they have the authority, whether they can delete things in the past
		if (!isAdmin && DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
			this.sendPostReply("/appointments", "", "Appointment has already happened, cannot delete.");
			return;
		}

		logger.debug("Attempting to delete appointment " + appointment.toString() + " ...");
		
		DataManager.getAppointmentDAO().delete(appointmentIdInt);
		//logger.info(DataManager.getAppointmentDAO().size() + " records total");
		logger.debug("Deleted appointment ID: [{}]", appointmentIdString);
		
		this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " deleted!");
		return;
	}
	
}
