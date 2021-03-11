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

		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		final List<Appointment> appointmentDAO = DataManager.getAppointmentDAO().retrieveAll();
		final List<PrettifiedAppointment> appointments = new ArrayList<PrettifiedAppointment>();
		
		if (uid > 0) {
			
			boolean isAdmin = memberDAO.retrieveByID(uid).getIsAdmin();
			String instructorName;
			String studentName;
			String date;
			String startTime;
			String endTime;
			for (Appointment appointment : appointmentDAO) {
				if (isAdmin || appointment.getStudentID() == uid || appointment.getInstructorID() == uid) {
					instructorName = memberDAO.retrieveByID(appointment.getInstructorID()).getDisplayName();
					studentName = memberDAO.retrieveByID(appointment.getStudentID()).getDisplayName();
					date = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					startTime = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					endTime = appointment.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					appointments.add(new PrettifiedAppointment(appointment.getRecID(), instructorName, studentName, date, startTime, endTime));
				}
			}
	
		}

		// FreeMarker
		templateDataMap.put("appointments", appointments);
		templateName = "appointments.ftl";

		// Go
		trySendResponse();
	}

}