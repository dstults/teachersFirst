package edu.lwtech.csd297.teachersfirst.pages;

import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;
import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.*;
public class AppointmentsPage extends PageLoader {

	public class PrettifiedAppointment implements IJsonnable {

		private int id;
		private int instructorId;
		private String instructorName;
		private int studentId;
		private String studentName;
		private String date;
		private String startTime;
		private String endTime;
		private boolean isMyAppointment;

		public PrettifiedAppointment(int id, int instructorId, String instructor, int studentId, String student, String date, String startTime, String endTime, boolean isMyAppointment) {
			this.id = id;
			this.instructorId = instructorId;
			this.instructorName = instructor;
			this.studentId = studentId;
			this.studentName = student;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
			this.isMyAppointment = isMyAppointment;
		}

		public int getId() { return id; }
		public int getInstructorId() { return instructorId; }
		public String getInstructorName() { return instructorName; }
		public int getStudentId() { return studentId; }
		public String getStudentName() { return studentName; }
		public String getDate() { return date; }
		public String getStartTime() { return startTime; }
		public String getEndTime() { return endTime; }
		public boolean getIsMyAppointment() { return isMyAppointment; }
		@Override public String toJson() {
			return "{\"id\":\"" + this.id +
					"\",\"instructorId\":\"" + this.instructorId +
					"\",\"instructorName\":\"" + this.instructorName +
					"\",\"studentId\":\"" + this.studentId +
					"\",\"studentName\":\"" + this.studentName +
					"\",\"date\":\"" + this.date +
					"\",\"startTime\":\"" + this.startTime +
					"\",\"endTime\":\"" + this.endTime +
					"\",\"isMyAppointment\":\"" + this.isMyAppointment +
					"\"}";
		}
	}

	// Constructor
	public AppointmentsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Appointments");

		// key variables
		final List<PrettifiedAppointment> futureAppointments = new ArrayList<PrettifiedAppointment>();		
		final List<PrettifiedAppointment> pastAppointments= new ArrayList<PrettifiedAppointment>();
		boolean jsonMode = QueryHelpers.getGetBool(request, "json");
		//logger.debug("Json Mode: " + (jsonMode ? "true" : "false"));
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
			final DAO<Member> memberDAO = DataManager.getMemberDAO();
			final DAO<Appointment> appointmentDAO = DataManager.getAppointmentDAO();
			if (appointmentDAO == null || appointmentDAO.retrieveByIndex(0) == null) {
				// Failed to contact SQL Server or simply no data
				templateDataMap.put("message", "Failed to contact database/no data, try again later.");
				templateName = "messageOnly.ftl";
				trySendResponse();
				DataManager.resetDAOs();
				return;
			}
			final List<Appointment> allAppointments = appointmentDAO.retrieveAll();
			// temp vars for more readable code below
			String instructorName;
			String studentName;
			String date;
			String startTime;
			String endTime;
			
			// check all DAOs
			for (Appointment appointment : allAppointments) {
				// make sure we're either an admin (sees everything) or in one of the appointments
				if ((isAdmin && filterMemberId == -1) || appointment.getIsMyAppointment(uid)) {
					instructorName = memberDAO.retrieveByID(appointment.getInstructorID()).getDisplayName();
					studentName = memberDAO.retrieveByID(appointment.getStudentID()).getDisplayName();
					date = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					startTime = appointment.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					endTime = appointment.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
					if (DateHelpers.isInThePast(appointment.getEndTime().toLocalDateTime())) {
						pastAppointments.add(
							new PrettifiedAppointment(
								appointment.getRecID(),
								appointment.getInstructorID(),
								instructorName,
								appointment.getStudentID(),
								studentName, date, startTime, endTime,
								appointment.getIsMyAppointment(uid)
							)
						);
					} else {
						futureAppointments.add(
							new PrettifiedAppointment(
								appointment.getRecID(),
								appointment.getInstructorID(),
								instructorName,
								appointment.getStudentID(),
								studentName, date, startTime, endTime,
								appointment.getIsMyAppointment(uid)
							)
						);
					}
				}
			}

			Collections.reverse(pastAppointments);
		}

		// Go
		if (jsonMode) {
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