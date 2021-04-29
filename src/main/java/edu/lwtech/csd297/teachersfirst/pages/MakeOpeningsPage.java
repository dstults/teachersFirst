package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.QueryHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class MakeOpeningsPage extends PageLoader {

	// Constructor
	public MakeOpeningsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Register");
		String instructorIdString = QueryHelpers.getGet(request, "instructorId", String.valueOf(uid));
		String startDateString = QueryHelpers.getGet(request, "startDate");
		String endDateString = QueryHelpers.getGet(request, "endDate");
		String startTimeString = QueryHelpers.getGet(request, "startTime", "12:00");
		String endTimeString = QueryHelpers.getGet(request, "endTime", "21:00");

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
		templateDataMap.put("instructorId", instructorIdString);
		templateDataMap.put("startDate", startDateString);
		templateDataMap.put("endDate", endDateString);
		templateDataMap.put("startTime", startTimeString);
		templateDataMap.put("endTime", endTimeString);

		// FreeMarker Template
		templateName = "newOpenings.ftl";

		// Go
		trySendResponse();
	}

}