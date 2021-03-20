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

	public class PrettifiedDay implements IJsonnable {
		
		private final String name;
		private final String color;
		private final List<PrettifiedOpening> openings;
		
		public PrettifiedDay(String name, String color, List<PrettifiedOpening> openings) {
			this.name = name;
			this.color = color;
			this.openings = openings;
		}
		
		public String getName() { return this.name; }
		public String getColor() { return this.color; }
		public List<PrettifiedOpening> getOpenings() { return this.openings; }
		@Override public String toJson() {
			return "{\"name\":\"" + this.name +
					"\",\"color\":\"" + this.color +
					"\",\"openings\":" + JsonUtils.BuildArrays(openings) +
					"}";
		}
	}

	public class PrettifiedOpening implements IJsonnable {

		private int id;
		private int instructorId;
		private String instructorName;
		private String date;
		private String startTime;
		private String endTime;
		private boolean highlight;

		public PrettifiedOpening(int id, int instructorId, String instructorName, String date, String startTime, String endTime, boolean highlight) {
			this.id = id;
			this.instructorId = instructorId;
			this.instructorName = instructorName;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
			this.highlight = highlight;
		}

		public int getId() { return id; }
		public int getInstructorId() { return instructorId; }
		public String getInstructorName() { return instructorName; }
		public String getDate() { return date; }
		public String getStartTime() { return startTime; }
		public String getEndTime() { return endTime; }
		public String getHighlight() { return highlight ? "highlight" : ""; }
		@Override public String toJson() {
			return "{\"id\":\"" + this.id +
					"\",\"instructorId\":\"" + this.instructorId +
					"\",\"instructorName\":\"" + this.instructorName +
					"\",\"date\":\"" + this.date +
					"\",\"startTime\":\"" + this.startTime +
					"\",\"endTime\":\"" + this.endTime +
					"\",\"highlight\":\"" + this.highlight +
					"\"}";
		}
	}

	// Constructor
	public OpeningsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Openings");
		boolean jsonMode = QueryHelpers.getGetBool(request, "json");

		final String instructorName = QueryHelpers.getGet(request, "instructorName").toLowerCase();

		LocalDateTime sundayTime = DateHelpers.previousSunday();
		LocalDateTime saturdayTime = DateHelpers.nextSaturday();
		final int weekTotal = 5;
		saturdayTime = saturdayTime.plusWeeks(weekTotal - 1); // -1 because base-0
		String sundayString = sundayTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		String saturdayString = saturdayTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		//logger.debug(DateHelpers.getDateTimeString());
		//logger.debug(DateHelpers.getSystemTimeZone());
		//logger.debug(sundayTime.toString());
		//logger.debug(saturdayTime.toString());

		List<List<PrettifiedDay>> weeks = new LinkedList<>();
		final DAO<Opening> openingDAO = DataManager.getOpeningDAO();
		if (openingDAO == null || openingDAO.retrieveByIndex(0) == null) {
			// Failed to contact SQL Server or simply no data
			templateName = "openings.ftl";
			templateDataMap.put("startDate", sundayString);
			templateDataMap.put("endDate", saturdayString);
			templateDataMap.put("weeks", weeks);
			templateDataMap.put("message", "Failed to contact database/no data, try again later.");
			trySendResponse();
			DataManager.resetDAOs();
			return;
		}

		final List<Opening> allOpenings = openingDAO.retrieveAll();
		final DAO<Member> memberDAO = DataManager.getMemberDAO();
		List<PrettifiedDay> thisWeek = null;
		PrettifiedDay today;
		LocalDateTime startTime;
		LocalDateTime endTime;

		// This would really benefit from specific SQL query optimization
		for (int day = 0; day < 7 * weekTotal; day++) {
			// once every week
			if (day % 7 == 0) {
				thisWeek = new LinkedList<>();
				weeks.add(thisWeek);
			}
			// get specific start and end milliseconds of scanned day
			startTime = sundayTime.plusDays(day);
			endTime = startTime.plusDays(1).minusSeconds(1);
			//logger.debug(startTime.toString());
			//logger.debug(endTime.toString());
			String dateName = startTime.format(DateTimeFormatter.ofPattern("dd"));
			String dateColor = DateHelpers.isInThePast(endTime) ? "graybg" : "whitebg";
			String dateToday = startTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			List<PrettifiedOpening> openingsToday = new ArrayList<>();
			today = new PrettifiedDay(dateName, dateColor, openingsToday);
			thisWeek.add(today);

			// scan all openings for any that fall within the day
			for (Opening iOpening : allOpenings) {
				if (DateHelpers.timeIsBetweenTimeAndTime(iOpening.getStartTime().toLocalDateTime(), startTime, endTime)) {

					String iName = memberDAO.retrieveByID(iOpening.getInstructorID()).getDisplayName();
					boolean iHighlight = !instructorName.isEmpty() && iName.toLowerCase().contains(instructorName);
					//logger.debug(iName + " is " + (iHighlight ? "" : "not ") + "highlighted");

					openingsToday.add(new PrettifiedOpening(
						iOpening.getRecID(),
						iOpening.getInstructorID(), // Freemarker likes to add commmas, I could add ?c to it too
						iName,
						dateToday,
						iOpening.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
						iOpening.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
						iHighlight)
					);
				}
			}
		}
		

		// Go
		if (jsonMode) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			sb.append("[");
			for (List<? extends IJsonnable> week : weeks) {
				if (i > 0) sb.append(",");
				sb.append(JsonUtils.BuildArrays(week));
				i++;
			}
			sb.append("]");
			String json = sb.toString();
			//logger.debug("Json: " + json);
			trySendJson(json);
		} else {
			// FreeMarker
			templateName = "openings.ftl";
			templateDataMap.put("startDate", sundayString);
			templateDataMap.put("endDate", saturdayString);
			templateDataMap.put("weeks", weeks);
			trySendResponse();
		}
	}

}