package org.funteachers.teachersfirst.obj;

import java.util.*;

class PrettifiedOpeningComparator implements Comparator<PrettifiedOpening> {
	public int compare(PrettifiedOpening po1, PrettifiedOpening po2) {
		// First by start time
		int startTimeResult = po1.getStartTime().compareTo(po2.getStartTime());
		if (startTimeResult != 0) return startTimeResult;

		// Second by instructor ID
		int instructorIdResult = po1.getInstructorId() - po2.getInstructorId();
		if (instructorIdResult != 0) return instructorIdResult;
		
		// Third by end time
		return po1.getEndTime().compareTo(po2.getEndTime());
	}
}

public class PrettifiedDay implements IJsonnable {
		
	private final String name;
	private final String color;
	private final List<PrettifiedOpening> openings;


	public PrettifiedDay(String name, String color, List<PrettifiedOpening> openings) {
		this.name = name;
		this.color = color;
		this.openings = openings;
	}
	
	public String getName() { return this.name; }
	public String getColor() { return this.color; }
	public List<PrettifiedOpening> getOpenings() { return this.openings; }
	@Override public String toJson() {
		return "{\"name\":\"" + this.name +
				"\",\"color\":\"" + this.color +
				"\",\"openings\":" + JsonUtils.BuildArrays(openings) +
				"}";
	}
	
	public void sortOpenings() {
		Collections.sort(this.openings, new PrettifiedOpeningComparator());
	}

}
