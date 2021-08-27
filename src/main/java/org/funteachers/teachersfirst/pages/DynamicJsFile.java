package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.PageLoader;
import org.funteachers.teachersfirst.managers.*;

public class DynamicJsFile extends PageLoader {

	// Constructor
	public DynamicJsFile(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Dynamic JS File");
		
		// FreeMarker
		templateName = "dynamic-js.ftl";
		templateDataMap.putIfAbsent("userId", this.uid);
		templateDataMap.putIfAbsent("userName", this.userName);
		templateDataMap.putIfAbsent("isAdmin", this.isAdmin);
		templateDataMap.putIfAbsent("isInstructor", this.isInstructor);
		templateDataMap.putIfAbsent("isStudent", this.isStudent);

		// Go
		trySendResponse("application/javascript");
	}

}