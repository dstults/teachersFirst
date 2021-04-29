package edu.lwtech.csd297.teachersfirst.pages;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class MakeAppointmentBatchPage extends PageLoader {

	// Constructor
	public MakeAppointmentBatchPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Make Appointment");

		if (uid > 0) {
			
			// Get Opening / Previous Data
			final String studentIdString = QueryHelpers.getGet(request, "studentId"); // sets default to self
			int studentIdInt;
			try {
				studentIdInt = Integer.parseInt(studentIdString);
			} catch (NumberFormatException e) {
				studentIdInt = 0;
			}
			final String studentName = studentIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(studentIdInt).getDisplayName() : "";
			final String instructorIdString = QueryHelpers.getGet(request, "instructorId", Integer.toString(uid));
			int instructorIdInt;
			try {
				instructorIdInt = Integer.parseInt(instructorIdString);
			} catch (NumberFormatException e) {
				instructorIdInt = 0;
			}
			final String instructorName = instructorIdInt > 0 ? DataManager.getMemberDAO().retrieveByID(instructorIdInt).getDisplayName() : "";
			final String startDateString = QueryHelpers.getGet(request, "startDate");
			final String endDateString = QueryHelpers.getGet(request, "endDate");
			final String startTimeString = QueryHelpers.getGet(request, "startTime");
			final String endTimeString = QueryHelpers.getGet(request, "endTime");
			
			// Days of Week - Data & FreeMarker
			String daysOfWeekString = QueryHelpers.getGet(request, "daysOfWeek"); // SuMoTuWdThFrSa
			String sundayChecked = daysOfWeekString.contains("su") ? " checked" : "";
			String mondayChecked = daysOfWeekString.contains("mo") ? " checked" : "";
			String tuesdayChecked = daysOfWeekString.contains("tu") ? " checked" : "";
			String wednesdayChecked = daysOfWeekString.contains("we") ? " checked" : "";
			String thursdayChecked = daysOfWeekString.contains("th") ? " checked" : "";
			String fridayChecked = daysOfWeekString.contains("fr") ? " checked" : "";
			String saturdayChecked = daysOfWeekString.contains("sa") ? " checked" : "";
			templateDataMap.put("daysOfWeek", daysOfWeekString);
			templateDataMap.put("sundayChecked", sundayChecked);
			templateDataMap.put("mondayChecked", mondayChecked);
			templateDataMap.put("tuesdayChecked", tuesdayChecked);
			templateDataMap.put("wednesdayChecked", wednesdayChecked);
			templateDataMap.put("thursdayChecked", thursdayChecked);
			templateDataMap.put("fridayChecked", fridayChecked);
			templateDataMap.put("saturdayChecked", saturdayChecked);

			// FreeMarker
			templateDataMap.put("studentId", studentIdString);
			templateDataMap.put("studentName", studentName);
			templateDataMap.put("instructorId", instructorIdString);
			templateDataMap.put("instructorName", instructorName);
			templateDataMap.put("startDate", startDateString);
			templateDataMap.put("endDate", endDateString);
			templateDataMap.put("startTime", startTimeString);
			templateDataMap.put("endTime", endTimeString);

	
		}

		// FreeMarker
		templateName = "makeAppointmentBatch.ftl";

		// Go
		trySendResponse();
	}

}