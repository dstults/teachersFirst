package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.QueryHelpers;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class NewOpeningsPage extends PageLoader {

	// Constructor
	public NewOpeningsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Register");
		String instructorIdString = QueryHelpers.getGet(request, "instructorId", String.valueOf(uid));
		String startDateString = QueryHelpers.getGet(request, "startDate");
		String endDateString = QueryHelpers.getGet(request, "endDate");
		String daysOfWeekString = QueryHelpers.getGet(request, "daysOfWeek"); // SuMoTuWdThFrSa
		String startTimeString = QueryHelpers.getGet(request, "startTime", "12:00");
		String endTimeString = QueryHelpers.getGet(request, "endTime", "21:00");

		String sundayChecked = daysOfWeekString.contains("su") ? " checked" : "";
		String mondayChecked = daysOfWeekString.contains("mo") ? " checked" : "";
		String tuesdayChecked = daysOfWeekString.contains("tu") ? " checked" : "";
		String wednesdayChecked = daysOfWeekString.contains("we") ? " checked" : "";
		String thursdayChecked = daysOfWeekString.contains("th") ? " checked" : "";
		String fridayChecked = daysOfWeekString.contains("fr") ? " checked" : "";
		String saturdayChecked = daysOfWeekString.contains("sa") ? " checked" : "";

		// FreeMarker
		templateDataMap.put("instructorId", instructorIdString);
		templateDataMap.put("sundayChecked", sundayChecked);
		templateDataMap.put("mondayChecked", mondayChecked);
		templateDataMap.put("tuesdayChecked", tuesdayChecked);
		templateDataMap.put("wednesdayChecked", wednesdayChecked);
		templateDataMap.put("thursdayChecked", thursdayChecked);
		templateDataMap.put("fridayChecked", fridayChecked);
		templateDataMap.put("saturdayChecked", saturdayChecked);
		templateDataMap.put("startDate", startDateString);
		templateDataMap.put("endDate", endDateString);
		templateDataMap.put("daysOfWeek", daysOfWeekString);
		templateDataMap.put("startTime", startTimeString);
		templateDataMap.put("endTime", endTimeString);
		templateName = "newOpenings.ftl";

		// Go
		trySendResponse();
	}

}