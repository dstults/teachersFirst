package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.*;
import edu.lwtech.csd297.teachersfirst.pojos.*;

public class ServicesPage extends PageLoader {

	// Notes for making classes that FreeMarker can actually read:
	//  - class must be public
	//  - class variables must be private
	//  - class variables must have appropriate getters

	// Constructor
	public ServicesPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }
		boolean jsonMode = QueryHelpers.getGetBool(request, "json");

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		
		// Get Data from DAO
		final List<Service> services = DataManager.getServiceDAO().retrieveAll();
		boolean jsonMode = QueryHelpers.getGetBool(request, "json");

		// FreeMarker
		templateName = "services.ftl";
		templateDataMap.put("services", services);

		// Go
		if (jsonMode) {
			String json = JsonUtils.BuildArrays(services);
			//logger.debug("Json: " + json);
			trySendJson(json);
		} else {
			trySendResponse();
		}
	}

}