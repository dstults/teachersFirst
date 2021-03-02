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
	public void LoadPage() {
		templateDataMap.put("title", "Appointments");

		// Should only get info for one member...
		//final Member member = memberDAO.retrieveAll();
		//templateDataMap.put("member", member);
		final List<Member> members = memberDAO.retrieveAll();

		// Should get all appointments for said member...
		//final List<Appointment> appointments = appointmentDAO.retrieveAll();

		// FreeMarker
		final List<DummyAppointment> appointments = new LinkedList<>();
		appointments.add(new DummyAppointment(5, DateHelpers.StringToTimestamp("2020-03-21 15:40"), DateHelpers.StringToTimestamp("2020-03-21 16:20"), "Darren, Tanya", "Edmund", "Practical Basketweaving"));
		appointments.add(new DummyAppointment(1025, DateHelpers.StringToTimestamp("2020-07-06 23:30"), DateHelpers.StringToTimestamp("2020-07-07 00:30"), "Darren, Tanya", "Edmund", "Midnight Basketweaving"));
		appointments.add(new DummyAppointment(1036, DateHelpers.StringToTimestamp("2020-08-01 12:00"), DateHelpers.StringToTimestamp("2020-08-01 13:00"), "Fred", "Edmund", "Underwater Basketweaving"));
		appointments.add(new DummyAppointment(2150, DateHelpers.StringToTimestamp("2021-01-15 15:30"), DateHelpers.StringToTimestamp("2021-01-15 16:10"), "Fred", "Edmund", "Bamboo Basketweaving"));
		templateDataMap.put("appointments", appointments);
		templateName = "appointments.ftl";

		// Go
		TrySendResponse();
	}

}