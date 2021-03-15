package edu.lwtech.csd297.teachersfirst.pages;

import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
public class AppointmentsPage extends PageLoader {

	public class PrettifiedAppointment {

		private int id;
		private String instructor;
		private String student;
		private String date;
		private String startTime;
		private String endTime;

		public PrettifiedAppointment(int id, String instructor, String student, String date, String startTime, String endTime) {
			this.id = id;
			this.instructor = instructor;
			this.student = student;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public int getId() { return id; }
		public String getInstructor() { return instructor; }
		public String getStudent() { return student; }
		public String getDate() { return date; }
		public String getStartTime() { return startTime; }
		public String getEndTime() { return endTime; }
	}

	// Constructor
	public AppointmentsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Appointments");

		// key variables
		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		final List<Appointment> appointmentDAO = DataManager.getAppointmentDAO().retrieveAll();
		final List<PrettifiedAppointment> futureAppointments = new ArrayList<PrettifiedAppointment>();
		final List<PrettifiedAppointment> pastAppointments = new ArrayList<PrettifiedAppointment>();
		
		// make sure we're logged in
		if (uid > 0) {
			
			// temp vars for more readable code below
			boolean isAdmin = memberDAO.retrieveByID(uid).getIsAdmin();
			String instructorName;
			String studentName;
			String date;
			String startTime;
			String endTime;
			
			// check all DAOs
			for (Appointment appointment : appointmentDAO) {
				// make sure we're either an admin (sees everything) or in one of the appointments
				if (isAdmin || appointment.getStudentID() == uid || appointment.getInstructorID() == uid) {
					instructorName = memberDAO.retrieveByID(appointment.getInstructorID()).getDisplayName();
					studentName = memberDAO.retrieveByID(appointment.getStudentID()).getDisplayName();
					date = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					startTime = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					endTime = appointment.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					if (DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
						pastAppointments.add(new PrettifiedAppointment(appointment.getRecID(), instructorName, studentName, date, startTime, endTime));
					} else {
						futureAppointments.add(new PrettifiedAppointment(appointment.getRecID(), instructorName, studentName, date, startTime, endTime));
					}
				}
			}
	
		}

		// FreeMarker
		templateDataMap.put("pastAppointments", pastAppointments);
		templateDataMap.put("futureAppointments", futureAppointments);
		templateName = "appointments.ftl";

		// Go
		trySendResponse();
	}

}