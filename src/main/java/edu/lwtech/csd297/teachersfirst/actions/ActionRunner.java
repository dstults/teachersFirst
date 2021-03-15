package edu.lwtech.csd297.teachersfirst.actions;

import java.io.*;

import javax.servlet.http.*;

import org.apache.logging.log4j.*;

import edu.lwtech.csd297.teachersfirst.*;

public abstract class ActionRunner {

	// Declarations

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected static final Logger logger = LogManager.getLogger(TeachersFirstServlet.class);

	// Constructors

	protected ActionRunner(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	// Public entry point

	public abstract void RunAction();

	// Protected Methods (shared magic between all actions)

	protected void SendRedirectToPage(String nextPage) {
		try {
			response.sendRedirect(nextPage);
		} catch (IOException e) {
			logger.error("IO Error: ", e);
		}
	}

}