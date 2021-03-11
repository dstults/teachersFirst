package edu.lwtech.csd297.teachersfirst.pages;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.daos.DAO;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class OpeningsPage extends PageLoader {

	// Helper class

	public class PrettifiedOpening {

		private String instructorName;
		private String instructorId;
		private String date;
		private String startTime;
		private String endTime;

		public PrettifiedOpening(String instructorName, String instructorId, String date, String startTime, String endTime) {
			this.instructorName = instructorName;
			this.instructorId = instructorId;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public String getInstructorName() { return instructorName; }
		public String getInstructorId() { return instructorId; }
		public String getDate() { return date; }
		public String getStartTime() { return startTime; }
		public String getEndTime() { return endTime; }
	}

	// Constructor
	public OpeningsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Openings");

		LocalDateTime sundayTime = DateHelpers.previousSunday();
		LocalDateTime saturdayTime = DateHelpers.nextSaturday();
		String sundayString = sundayTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		String saturdayString = saturdayTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		//logger.debug(DateHelpers.getDateTimeString());
		//logger.debug(DateHelpers.getSystemTimeZone());
		//logger.debug(sundayTime.toString());
		//logger.debug(saturdayTime.toString());

		// Should only show members that it should show based on who's querying...
		final List<Opening> allOpenings = DataManager.getOpeningDAO().retrieveAll();
		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		List<List<PrettifiedOpening>> days = new LinkedList<>();
		LocalDateTime startTime;
		LocalDateTime endTime;

		// This would really benefit from specific SQL query optimization
		for (int day = 0; day < 7; day++) {
			// make a new list for each day and add it to days list
			List<PrettifiedOpening> openings = new LinkedList<>();
			days.add(openings);
			// get specific start and end milliseconds of scanned day
			startTime = sundayTime.plusDays(day);
			endTime = startTime.plusDays(1).minusSeconds(1);
			logger.debug(startTime.toString());
			logger.debug(endTime.toString());
			String dateToday = startTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

			// scan all openings for any that fall within the day
			for (Opening iOpening : allOpenings) {
				if (iOpening.getStartTime().toLocalDateTime().compareTo(startTime) >= 0 && 
						iOpening.getStartTime().toLocalDateTime().compareTo(endTime) < 0) {

					openings.add(new PrettifiedOpening(
							memberDAO.retrieveByID(iOpening.getInstructorID()).getDisplayName(),
							Integer.toString(iOpening.getInstructorID()), // Freemarker likes to add commmas, I could add ?c to it too
							dateToday,
							iOpening.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
							iOpening.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
					);
				}
			}
		}
		
		// FreeMarker
		templateName = "openings.ftl";
		templateDataMap.put("startDate", sundayString);
		templateDataMap.put("endDate", saturdayString);
		templateDataMap.put("days", days);

		// Go
		trySendResponse();
	}

}