package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class MakeAppointmentBatchPage extends PageLoader {

	// Constructor
	public MakeAppointmentBatchPage(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Batch Appointments");

		// FreeMarker
		templateName = "makeAppointmentBatch.ftl";

		// Go
		trySendResponse();
	}

}