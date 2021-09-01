package org.funteachers.teachersfirst.managers;

import javax.servlet.*;

import freemarker.template.*;

import java.io.*;

import org.apache.logging.log4j.*;

public class FreeMarkerSetup {
		
	final private static Logger logger = LogManager.getLogger();

	final public static Configuration freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_24);
	
	public static void initializeFreeMarker(String resourcesDir) throws ServletException {
		if (resourcesDir == null) {
			logger.warn("===========DEBUG HELP===========");
			logger.warn("Something has broken above here!");
			logger.warn("FreeMarker is attempting to initialize without a resourcesDir, this should not be possible.");
			throw new RuntimeException("FreeMarker is attempting to initialize without a resourcesDir.");
		}

		// Set the template directory
		String templateDir = resourcesDir + "/templates";
		try {
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			String msg = "Template directory not found: " + templateDir;
			logger.fatal(msg, e);
			throw new UnavailableException(msg);
		}

		// Make FreeMarker not output its errors to user:
		freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		
		// Set this to "true" (default) to log errors; set to "false" to hide them from the catalina log:
		freeMarkerConfig.setLogTemplateExceptions(true);

		//freeMarkerConfig.setTemplateExceptionHandler(new FreeMarkerExceptionHandler());
		
	}

}
