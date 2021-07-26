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
		
		// Update credits for student
		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		final Member student = memberDAO.retrieveByID(appointment.getStudentID());
		float credits = student.getCredits();
		float length = appointment.getLength();
		credits += length;
		String opName = QueryHelpers.getSessionValue(request, "USER_NAME", "Stranger");
		student.setCredits(uid, opName, "delete appointment[" + appointmentIdInt + "] len=" + appointment.getLength() + " hrs", credits);
		memberDAO.update(student);
		
		// Send response
		this.sendPostReply("/appointments", "", "Appointment " + appointmentIdString + " deleted!");
		return;
	}
	
}
