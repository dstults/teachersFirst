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
		templateDataMap.putIfAbsent("primaryHighlight", DataManager.primaryHighlight);
		templateDataMap.putIfAbsent("primaryHighlightDark", DataManager.primaryHighlightDark);
		templateDataMap.putIfAbsent("backgroundColor", DataManager.backgroundColor);

		// Go
		trySendResponse();
	}

}