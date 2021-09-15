package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class MakeOpeningsPage extends PageLoader {

	// Constructor
	public MakeOpeningsPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Create Openings");

		// Days of Week - Data & FreeMarker
		String daysOfWeekString = QueryHelpers.getGet(request, "daysOfWeek"); // SuMoTuWdThFrSa
		if (daysOfWeekString.isEmpty()) daysOfWeekString = DateHelpers.getDayOfWeekStringFromToday();
		final String sundayChecked = daysOfWeekString.contains("su") ? " checked" : "";
		final String mondayChecked = daysOfWeekString.contains("mo") ? " checked" : "";
		final String tuesdayChecked = daysOfWeekString.contains("tu") ? " checked" : "";
		final String wednesdayChecked = daysOfWeekString.contains("we") ? " checked" : "";
		final String thursdayChecked = daysOfWeekString.contains("th") ? " checked" : "";
		final String fridayChecked = daysOfWeekString.contains("fr") ? " checked" : "";
		final String saturdayChecked = daysOfWeekString.contains("sa") ? " checked" : "";
		templateDataMap.put("daysOfWeek", daysOfWeekString);
		templateDataMap.put("sundayChecked", sundayChecked);
		templateDataMap.put("mondayChecked", mondayChecked);
		templateDataMap.put("tuesdayChecked", tuesdayChecked);
		templateDataMap.put("wednesdayChecked", wednesdayChecked);
		templateDataMap.put("thursdayChecked", thursdayChecked);
		templateDataMap.put("fridayChecked", fridayChecked);
		templateDataMap.put("saturdayChecked", saturdayChecked);

		// FreeMarker Template
		templateName = "newOpenings.ftl";

		// Go
		trySendResponse();
	}

}