package org.funteachers.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.DAO;
import org.funteachers.teachersfirst.obj.*;

public class ServicesPage extends PageLoader {

	// Notes for making classes that FreeMarker can actually read:
	//  - class must be public
	//  - class variables must be private
	//  - class variables must have appropriate getters

	// Constructor
	public ServicesPage(HttpServletRequest request, HttpServletResponse response, Security security) { super(request, response, security); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		
		// Check DAO connection
		final DAO<Service> serviceDAO = DataManager.getServiceDAO();
		if (serviceDAO == null) {
			// Failed to contact SQL Server
			templateName = "messageOnly.ftl";
			templateDataMap.put("message", "Failed to connect with database, please try again.");
			trySendResponse();
			DataManager.resetDAOs();
			return;
		}

		// Get data from DAO
		final List<Service> services = serviceDAO.retrieveAll();

		// Go
		if (jsonMode) {
			String json = JsonUtils.BuildArrays(services);
			//logger.debug("Json: " + json);
			trySendJson(json);
		} else {
			// FreeMarker
			if (services == null || services.size() == 0) {
				templateName = "messageOnly.ftl";
				templateDataMap.put("message", "Could not contact database or no services loaded.");
			} else {
				templateName = "services.ftl";
				templateDataMap.put("services", services);
			}
			trySendResponse();
		}
	}

}