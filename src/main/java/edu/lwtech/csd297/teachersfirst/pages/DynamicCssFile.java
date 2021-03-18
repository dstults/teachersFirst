package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.DataManager;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class DynamicCssFile extends PageLoader {

	// Constructor
	public DynamicCssFile(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Dynamic CSS File");

		// FreeMarker
		templateName = "dynamic-css.ftl";
		if (isAdmin) {
			templateDataMap.putIfAbsent("primaryHighlight", DataManager.primaryHighlightAdmin);
			templateDataMap.putIfAbsent("primaryHighlightDark", DataManager.primaryHighlightDarkAdmin);
			templateDataMap.putIfAbsent("backgroundColor", DataManager.backgroundColorAdmin);
			templateDataMap.putIfAbsent("backgroundColorLight", DataManager.backgroundColorLightAdmin);
		} else if (isInstructor) {
			templateDataMap.putIfAbsent("primaryHighlight", DataManager.primaryHighlightInstructor);
			templateDataMap.putIfAbsent("primaryHighlightDark", DataManager.primaryHighlightDarkInstructor);
			templateDataMap.putIfAbsent("backgroundColor", DataManager.backgroundColorInstructor);
			templateDataMap.putIfAbsent("backgroundColorLight", DataManager.backgroundColorLightInstructor);
		} else {
			templateDataMap.putIfAbsent("primaryHighlight", DataManager.primaryHighlightGeneral);
			templateDataMap.putIfAbsent("primaryHighlightDark", DataManager.primaryHighlightDarkGeneral);
			templateDataMap.putIfAbsent("backgroundColor", DataManager.backgroundColorGeneral);	
			templateDataMap.putIfAbsent("backgroundColorLight", DataManager.backgroundColorLightGeneral);
		}

		// Go
		trySendResponse("text/css");
	}

}