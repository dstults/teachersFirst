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
			templateDataMap.putIfAbsent("primaryHighlight", GlobalConfig.primaryHighlightAdmin);
			templateDataMap.putIfAbsent("primaryHighlightDark", GlobalConfig.primaryHighlightDarkAdmin);
			templateDataMap.putIfAbsent("backgroundColor", GlobalConfig.backgroundColorAdmin);
			templateDataMap.putIfAbsent("backgroundColorLight", GlobalConfig.backgroundColorLightAdmin);
		} else if (isInstructor) {
			templateDataMap.putIfAbsent("primaryHighlight", GlobalConfig.primaryHighlightInstructor);
			templateDataMap.putIfAbsent("primaryHighlightDark", GlobalConfig.primaryHighlightDarkInstructor);
			templateDataMap.putIfAbsent("backgroundColor", GlobalConfig.backgroundColorInstructor);
			templateDataMap.putIfAbsent("backgroundColorLight", GlobalConfig.backgroundColorLightInstructor);
		} else {
			templateDataMap.putIfAbsent("primaryHighlight", GlobalConfig.primaryHighlightGeneral);
			templateDataMap.putIfAbsent("primaryHighlightDark", GlobalConfig.primaryHighlightDarkGeneral);
			templateDataMap.putIfAbsent("backgroundColor", GlobalConfig.backgroundColorGeneral);	
			templateDataMap.putIfAbsent("backgroundColorLight", GlobalConfig.backgroundColorLightGeneral);
		}

		// Go
		trySendResponse("text/css");
	}

}