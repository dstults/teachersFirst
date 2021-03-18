package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class ServicesPage extends PageLoader {

	// Note for making dummy classes that FreeMarker can actually read:
	// class must be public
	// class variables must be private
	// class variables must have appropriate getters
	public class DummyService {

		private String name;
		private String description;
		private String teachers;

		public DummyService(String name, String description, String teachers) {
			this.name = name;
			this.description = description;
			this.teachers = teachers;
		}

		public String getName() { return name; }
		public String getDescription() { return description; }
		public String getTeachers() { return teachers; }
	}


	// Constructor
	public ServicesPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void loadPage() {
		templateDataMap.put("title", "Services");
		
		// Dummy data for display
		final List<DummyService> services = new LinkedList<>();
		services.add(new DummyService("Pilates", "Pilates is a method of exercise that consists of low-impact flexibility and muscular strength and endurance movements. Use it to stay super trim!", "Fred, Edmund"));
		services.add(new DummyService("Phonetics", "Learn how to make all the sounds with your mouth and talk like a charismatic expert! IPA is your friend!", "Darren"));
		services.add(new DummyService("Database Repair", "Fix all the errors in your SQL, learn how to avoid them in the future, never get escaped again!", "Edmund, Tanya"));
		services.add(new DummyService("Extreme E-Biking", "Pretend you're biking when you're really not! Hear someone yell at you in a crowded room from the spacious comfort of your home!", "Fred, Darren"));

		// FreeMarker
		templateName = "services.ftl";
		templateDataMap.put("services", services);

		// Go
		trySendResponse();
	}

}