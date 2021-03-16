package edu.lwtech.csd297.teachersfirst.actions;

import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DeleteAppointmentAction extends ActionRunner {

	public DeleteAppointmentAction(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	@Override
	public void RunAction() {

		// This should not be possible for anyone not logged in.
		final int uid = Security.getUserId(request);
		if (uid <= 0) {
			this.SendRedirectToPage("/services?message=Please sign in or register to use this feature!");
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
			this.SendRedirectToPage("/appointments?message=Appointment " + appointmentIdInt + " not found!");
			return;
		}
		logger.debug("Attempting to delete appointment " + appointment.toString() + " ...");
		
		DataManager.getAppointmentDAO().delete(appointmentIdInt);
		logger.info(DataManager.getAppointmentDAO().size() + " records total");
		logger.debug("Deleted appointment ID: [{}]", appointmentIdInt);
		
		this.SendRedirectToPage("/appointments?message=Appointment " + appointmentIdInt + " deleted!");
		return;
	}
	
}
