package org.funteachers.teachersfirst.pages;

import java.time.*;
import java.time.format.*;
import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.daos.sql.OpeningSqlDAO;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class OpeningsPage extends PageLoader {

	// Constructor
	public OpeningsPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Openings");
		final String instructorName = QueryHelpers.getGet(request, "instructorName").toLowerCase();

		// Get first and last days
		final int weeksToShow = 5;
		final LocalDateTime startDateTime = DateHelpers.previousSunday();
		// endDateTime => -1 because Sunday=0 and Saturday=0 is head and tail of same week
		final LocalDateTime endDateTime = DateHelpers.nextSaturday().plusWeeks(weeksToShow - 1);
		final String startString = startDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		final String endString = endDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		// Prepare "week" data for final or short-circuit response
		List<List<PrettifiedDay>> weeks = new LinkedList<>();

		// Test database connection
		final boolean noConnection = this.connectionPackage.getConnection(this.getClass().toString()) == null;
		final OpeningSqlDAO openingDAO = noConnection ? null : (OpeningSqlDAO) this.connectionPackage.getOpeningDAO(this.getClass().toString());
		// Get all openings from the database
		// endDateTime => +1s because nextSaturday is hh:59:59 and we need to include hh++:00:00 for the final possible time slot
		// endDateTime => +1d because if the opening spans several hours into the next day, we want to capture it onthis.getClass().toString() the previous
		final List<Opening> allOpenings = openingDAO == null ? null : openingDAO.retrieveAllBetweenDatetimeAndDatetime(startDateTime, endDateTime.plusSeconds(1).plusDays(1));

		// Short circuit if no connection or no openings
		final boolean noOpenings = allOpenings == null || allOpenings.size() == 0;
		if (noConnection || noOpenings) {
			templateName = "openings.ftl";
			templateDataMap.put("batchEnabled", false);
			templateDataMap.put("startDate", startString);
			templateDataMap.put("endDate", endString);
			templateDataMap.put("weeks", weeks);
			if (noConnection) {
				templateDataMap.put("message", "Failed to contact database, please try again.");
			} else if (noOpenings) {
				templateDataMap.put("message", "No openings found.");
			}
			trySendResponse();
			return;
		}

		final DAO<Member> memberDAO = this.connectionPackage.getMemberDAO(this.getClass().toString());
		final List<Member> allMembers = memberDAO.retrieveAll();

		// Prepare iterative variables for constructing "prettified" openings
		List<PrettifiedDay> thisWeek = null;
		PrettifiedDay today;
		LocalDateTime startTime;
		LocalDateTime endTime;
		for (int day = 0; day < 7 * weeksToShow; day++) {
			// once every week
			if (day % 7 == 0) {
				thisWeek = new LinkedList<>();
				weeks.add(thisWeek);
			}
			// get specific start and end milliseconds of scanned day
			startTime = startDateTime.plusDays(day);
			endTime = startTime.plusDays(1).minusSeconds(1);
			String dateName = startTime.format(DateTimeFormatter.ofPattern("dd"));
			String dateColor = DateHelpers.isInThePast(endTime) ? "graybg" : "whitebg";
			String dateToday = startTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			List<PrettifiedOpening> openingsToday = new ArrayList<>();
			today = new PrettifiedDay(dateName, dateColor, openingsToday);
			thisWeek.add(today);

			// Scan all openings for any that fall within the day:
			// TODO: This can be made more efficient by pulling the openings out of the allOpenings as they get used
			for (Opening iOpening : allOpenings) {
				if (DateHelpers.timeIsBetweenTimeAndTime(iOpening.getStartTime().toLocalDateTime(), startTime, endTime)) {

					Member member = MemberHelpers.FindByID(allMembers, iOpening.getInstructorID());
					if (member == null || member.getIsDeleted()) continue;
					String iName = member.getDisplayName();
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

			// Letting the SQL database take care of this, so commented out:
			//today.sortOpenings();
		}

		// Go
		if (this.jsonMode) {
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
			boolean enableBatch = !GlobalConfig.instructorAdminMakeAppointmentsRequiresOpening && (isAdmin || isInstructor);
			templateDataMap.put("batchEnabled", enableBatch);
			templateDataMap.put("startDate", startString);
			templateDataMap.put("endDate", endString);
			templateDataMap.put("weeks", weeks);
			trySendResponse();
		}
	}

}