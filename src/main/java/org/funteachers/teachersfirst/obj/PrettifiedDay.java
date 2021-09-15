package org.funteachers.teachersfirst.obj;

import java.util.*;

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
}
