package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class HealthPage extends PageLoader {

	// Constructor
	public HealthPage(ConnectionPackage cp) { super(cp); }

	// Page-specific
	@Override
	public void loadPage() {
		// Check DB connection
		final boolean controllerRunning = true; // This is a given
		final boolean databaseConnection = this.connectionPackage.getIsConnectionHealthy();
		
		final String status;
		if (controllerRunning && databaseConnection) {
			// All conditions true
			status = "good";
		} else if (!controllerRunning || !databaseConnection) {
			// Any condition false
			status = "bad";
		} else {
			// Prevent potential unhandled exceptions for bad future updates
			status = "undetermined";
		}
		String json = "{\n\t\"controller\": \"good\",\n\t\"database\": \"" + this.connectionPackage.getConnectionStatusMessage() + "\",\n\t\"status\": \"" + status + "\"\n}";

		// Make respondable to anyone:
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		trySendJson(json);
	}

}