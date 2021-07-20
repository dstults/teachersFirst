package org.funteachers.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.obj.*;

public class ServicesPage extends PageLoader {

	// Notes for making classes that FreeMarker can actually read:
	//  - class must be public
	//  - class variables must be private
	//  - class variables must have appropriate getters

	// Constructor
	public ServicesPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		
		// Get Data from DAO
		final List<Service> services = DataManager.getServiceDAO().retrieveAll();

		// Go
		if (jsonMode) {
			String json = JsonUtils.BuildArrays(services);
			//logger.debug("Json: " + json);
			trySendJson(json);
		} else {
			// FreeMarker
			if (services.size() > 0) {
				templateName = "services.ftl";
				templateDataMap.put("services", services);
			} else {
				templateName = "messageOnly.ftl";
				templateDataMap.put("message", "Could not contact database or no services loaded.");
			}
			trySendResponse();
		}
	}

}