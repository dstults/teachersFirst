package org.funteachers.teachersfirst.managers;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.*;

public class FreeMarkerExceptionHandler implements TemplateExceptionHandler {

	@Override
	public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
		try {
			out.write("[ERROR: " + te.getMessage() + "]");
		} catch (IOException e) {
			throw new TemplateException("Failed to print error message. Cause: " + e, env);
		}
	}
}
