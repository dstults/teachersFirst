package edu.lwtech.csd297.teachersfirst.pages;

import java.util.*;
import javax.servlet.http.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class OpeningsPage extends PageLoader {

	public class DummyOpening {

		private String instructor;
		private String startTime;
		private String endTime;

		public DummyOpening(String instructor, String startTime, String endTime) {
			this.instructor = instructor;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public String getInstructor() { return instructor; }
		public String getStartTime() { return startTime; }
		public String getEndTime() { return endTime; }
	}


	// Constructor
	public OpeningsPage(HttpServletRequest request, HttpServletResponse response) { super(request, response); }

	// Page-specific

	@Override
	public void LoadPage() {
		templateDataMap.put("title", "Openings");

		// Should only show members that it should show based on who's querying...
		//final List<Member> members = memberDAO.retrieveAll();
		
		// FreeMarker
		templateName = "openings.ftl";
		templateDataMap.put("startDate", "2/28/2021");
		templateDataMap.put("endDate", "3/6/2021");
		templateDataMap.put("teacherName", "Fred");
		List<List<DummyOpening>> days = new LinkedList<>();
		List<DummyOpening> openings = new LinkedList<>();
		openings.add(new DummyOpening("Fred", "12:00 pm", "6:00 pm"));
		openings.add(new DummyOpening("Darren", "1:00 pm", "8:00 pm"));
		days.add(openings);
		logger.debug("temp1");
		for(int i = 0; i < 4; i++) {
			openings = new LinkedList<>();
			openings.add(new DummyOpening("Fred", "6:00 pm", "9:00 pm"));
			if (i % 2 == 0) openings.add(new DummyOpening("Tanya", "2:00 pm", "5:00 pm"));
			if (i % 2 == 1) openings.add(new DummyOpening("Edmund", "5:00 pm", "9:00 pm"));
			days.add(openings);
		}
		logger.debug("temp2");
		openings = new LinkedList<>();
		days.add(openings);
		openings = new LinkedList<>();
		days.add(openings);
		logger.debug("temp3 " + days.size());
		templateDataMap.put("days", days);

		// Go
		TrySendResponse();
	}

}