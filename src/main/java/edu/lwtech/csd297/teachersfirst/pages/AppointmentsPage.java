package edu.lwtech.csd297.teachersfirst.pages;

import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
import edu.lwtech.csd297.teachersfirst.obj.*;
public class AppointmentsPage extends PageLoader {

	// Constructor
	public AppointmentsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Appointments");

		// key variables
		final List<Appointment> futureAppointments = new ArrayList<Appointment>();		
		final List<Appointment> pastAppointments= new ArrayList<Appointment>();
		final boolean jsonMode = QueryHelpers.getGetBool(request, "json");
		if (jsonMode) {
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
			
			// make sure we're logged in
			if (uid > 0) {
				final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
				if (appointmentDAO == null || appointmentDAO.retrieveByIndex(0) == null) {
					if (appointmentDAO == null)
						templateDataMap.put("message", "Failed to contact database, try again later.");
					else
						templateDataMap.put("message", "There isn't any appointment data to display yet.");
					templateName = "messageOnly.ftl";
					trySendResponse();
					DataManager.resetDAOs();
					return;
				}
				final List<Member> allMembers = DataManager.getMemberDAO().retrieveAll();
				final List<Appointment> allAppointments = appointmentDAO.retrieveAll();
				
				// check all DAOs
				for (Appointment appointment : allAppointments) {
					// make sure we're either an admin (sees everything) or in one of the appointments
					if ((isAdmin && filterMemberId == -1) || appointment.getIsMyAppointment(uid)) {
						appointment.setInstructorName(MemberHelpers.FindNameByID(allMembers, appointment.getInstructorID()));
						appointment.setStudentName(MemberHelpers.FindNameByID(allMembers, appointment.getStudentID()));
						appointment.setIsMyAppointment(appointment.getIsMyAppointment(uid));
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
			templateDataMap.put("pastAppointments", pastAppointments);
			templateDataMap.put("futureAppointments", futureAppointments);
			templateName = "appointments.ftl";

			trySendResponse();
		}
	}

}