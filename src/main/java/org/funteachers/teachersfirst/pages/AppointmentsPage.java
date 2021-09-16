package org.funteachers.teachersfirst.pages;

import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class AppointmentsPage extends PageLoader {

	// Constructor
	public AppointmentsPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		if (this.jsonMode) {

			// JSON
			getAppointmentJson();

		} else {

			// FreeMarker
			templateDataMap.put("title", "Appointments");
			templateName = "appointments.ftl";

			trySendResponse();
		}
	}

	private void getAppointmentJson() {
		boolean userIsLoggedIn = this.uid > 0;
		if (!userIsLoggedIn) {
			sendJsonMessage("Error: You are not logged in.");
			return;
		}
		boolean connectedToDatabase = this.connectionPackage.getConnection(this.getClass().toString()) != null;
		if (!connectedToDatabase) {
			sendJsonMessage("Error: Failed to contact database, please try again.");
			return;
		}

		// Get data from DAOs
		final List<Member> allMembers = this.connectionPackage.getMemberDAO(this.getClass().toString()).retrieveAll();
		final List<Appointment> allAppointments = this.connectionPackage.getAppointmentDAO(this.getClass().toString()).retrieveAll();

		// Filter into these categories
		final List<Appointment> futureAppointments = new ArrayList<Appointment>();		
		final List<Appointment> pastAppointments= new ArrayList<Appointment>();

		// TODO: Simplify this process into two queries
		for (Appointment appointment : allAppointments) {
			// Make sure we're either an admin (sees everything) or one of the members of the appointment
			if (this.isAdmin || appointment.getIsMyAppointment(uid)) {
				appointment.setInstructorName(MemberHelpers.FindNameByID(allMembers, appointment.getInstructorID()));
				appointment.setStudentName(MemberHelpers.FindNameByID(allMembers, appointment.getStudentID()));
				if (DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
					pastAppointments.add(appointment);
				} else {
					futureAppointments.add(appointment);
				}
			}
		}

		Collections.reverse(pastAppointments);

		String json = JsonUtils.BuildArrays(futureAppointments, pastAppointments);
		//logger.debug("Json: " + json);
		trySendJson(json);
	}

}