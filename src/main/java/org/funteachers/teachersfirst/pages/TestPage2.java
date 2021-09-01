package org.funteachers.teachersfirst.pages;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.managers.*;

public class TestPage2 extends PageLoader {

	// Constructor
	public TestPage2(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		
		// ========================= Diagnostic security checks

		// Get initial bit to verify ID and operation
		final String clientIp = security.getRealIp();

		// Check if whitelisted
		if (!SecurityChecker.isWhitelisted(clientIp)) {
			sendFake404("Unauthorized user [ " + clientIp + "] attempted to access diagnostics page.");
			return;
		}

		// ========================= Place content below this line

		// FreeMarker
		templateName = "test2.ftl";

		// Go
		trySendResponse();
	}

}