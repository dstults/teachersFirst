package org.funteachers.teachersfirst.pages;

import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class AppointmentsPage extends PageLoader {

	// Constructor
	public AppointmentsPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		if (jsonMode) {
			
			// key variables
			final List<Appointment> futureAppointments = new ArrayList<Appointment>();		
			final List<Appointment> pastAppointments= new ArrayList<Appointment>();

			String filterMemberIdString = QueryHelpers.getGet(request, "memberId");
			int filterMemberId;
			if (!filterMemberIdString.isEmpty() && isAdmin) {
				try {
					filterMemberId = Integer.parseInt(filterMemberIdString);
				} catch (NumberFormatException e) {
					filterMemberId = -1;
				}
			} else if (filterMemberIdString.isEmpty() && isAdmin) {
				filterMemberId = -1;
			} else {
				filterMemberId = uid;
			}
			
			// TODO: I can delete these on the various pages now that it doesn't even load things without DAO connection!
			// Check DAO connection
			if (uid > 0) {
				// TODO: This is json mode now! This should probably say something like:
				// {"message":"Unable to contact database!"}
				final DAO<Appointment> appointmentDAO = this.connectionPackage.getAppointmentDAO();
				if (appointmentDAO == null || appointmentDAO.retrieveByIndex(0) == null) {
					if (appointmentDAO == null)
						templateDataMap.put("message", "Failed to contact database, try again later.");
					else
						templateDataMap.put("message", "There isn't any appointment data to display yet.");
					templateName = "messageOnly.ftl";
					trySendResponse();
					this.connectionPackage.resetDAOs();
					return;
				}
				// Get data from DAO
				final List<Member> allMembers = this.connectionPackage.getMemberDAO().retrieveAll();
				final List<Appointment> allAppointments = appointmentDAO.retrieveAll();
				
				// check all DAOs
				for (Appointment appointment : allAppointments) {
					// make sure we're either an admin (sees everything) or in one of the appointments
					if ((isAdmin && filterMemberId == -1) || appointment.getIsMyAppointment(uid)) {
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
			}

			String json = JsonUtils.BuildArrays(futureAppointments, pastAppointments);
			//logger.debug("Json: " + json);
			trySendJson(json);

		} else {

			// FreeMarker
			templateDataMap.put("title", "Appointments");
			templateName = "appointments.ftl";

			trySendResponse();
		}
	}

}