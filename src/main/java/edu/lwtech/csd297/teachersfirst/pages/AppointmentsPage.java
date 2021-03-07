package edu.lwtech.csd297.teachersfirst.pages;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import edu.lwtech.csd297.teachersfirst.*;
public class AppointmentsPage extends PageLoader {

	// Placeholder
	public class DummyAppointment {

		private int recID;
		private Timestamp startTime;
		private Timestamp endTime;
		private String attendees;
		private String instructors;
		private String service;

		public DummyAppointment(int recID, Timestamp startTime, Timestamp endTime, String attendees, String instructors, String service) {
			this.recID = recID;
			this.startTime = startTime;
			this.endTime = endTime;
			this.attendees = attendees;
			this.instructors = instructors;
			this.service = service;
		}
		public int getRecID() { return recID; }
		public Timestamp getStartTime() { return startTime; }
		public Timestamp getEndTime() { return endTime; }
		public String getAttendees() { return attendees; }
		public String getInstructors() { return instructors; }
		public String getService() { return service; }
	}

	// Constructor
	public AppointmentsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Appointments");

		// Should only get info for one member...
		final List<Appointment> appointments = DataManager.getAppointmentDAO().retrieveAll();

		// Should get all appointments for said member...
		//final List<Appointment> appointments = appointmentDAO.retrieveAll();

		// FreeMarker
		templateDataMap.put("appointments", appointments);
		templateName = "appointments.ftl";

		// Go
		trySendResponse();
	}

}