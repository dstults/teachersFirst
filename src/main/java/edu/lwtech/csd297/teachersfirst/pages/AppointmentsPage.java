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
		public String toJson() {
			return "{\"id\":\"" + this.id +
					"\",\"student\":\"" + this.student +
					"\",\"date\":\"" + this.date +
					"\",\"startTime\":\"" + this.startTime +
					"\",\"endTime\":\"" + this.endTime +
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
		logger.debug("Json Mode: " + (jsonMode ? "true" : "false"));
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
			final List<Appointment> appointmentDAO = DataManager.getAppointmentDAO().retrieveAll();;

			// temp vars for more readable code below
			String instructorName;
			String studentName;
			String date;
			String startTime;
			String endTime;
			
			// check all DAOs
			for (Appointment appointment : appointmentDAO) {
				// make sure we're either an admin (sees everything) or in one of the appointments
				if ((isAdmin && filterMemberId == -1) || appointment.getStudentID() == filterMemberId || appointment.getInstructorID() == filterMemberId) {
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

			Collections.reverse(pastAppointments);
		}

		// Go
		if (jsonMode) {
			int i = 0;
			//TODO: 2 JSON arrays -- can't cast to List<IJsonnable> and use JSONUtils so we do it manually for now:
			StringBuilder sb = new StringBuilder("{");
			for (PrettifiedAppointment appointment : futureAppointments) {
				if (i > 0) sb.append(",");
				sb.append(appointment.toJson());
				i++;
			}
			sb.append("},{");
			i = 0;
			for (PrettifiedAppointment appointment : pastAppointments) {
				if (i > 0) sb.append(",");
				sb.append(appointment.toJson());
				i++;
			}
			sb.append("}");
			logger.debug("Json: " + sb.toString());
			trySendJson(sb.toString());
		} else {

			// FreeMarker
			templateDataMap.put("pastAppointments", pastAppointments);
			templateDataMap.put("futureAppointments", futureAppointments);
			templateName = "appointments.ftl";

			trySendResponse();
		}
	}

}