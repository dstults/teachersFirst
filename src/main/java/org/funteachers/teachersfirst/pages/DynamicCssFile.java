package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class DynamicCssFile extends PageLoader {

	// Constructor
	public DynamicCssFile(ConnectionPackage cp) { super(cp); }

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