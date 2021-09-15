package org.funteachers.teachersfirst.obj;

public class PrettifiedOpening implements IJsonnable {

	private int id;
	private int instructorId;
	private String instructorName;
	private String date;
	private String startTime;
	private String endTime;
	private boolean highlight;

	public PrettifiedOpening(int id, int instructorId, String instructorName, String date, String startTime, String endTime, boolean highlight) {
		this.id = id;
		this.instructorId = instructorId;
		this.instructorName = instructorName;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.highlight = highlight;
	}

	public int getId() { return id; }
	public int getInstructorId() { return instructorId; }
	public String getInstructorName() { return instructorName; }
	public String getDate() { return date; }
	public String getStartTime() { return startTime; }
	public String getEndTime() { return endTime; }
	public String getHighlight() { return highlight ? "highlight" : ""; }
	@Override public String toJson() {
		return "{\"id\":\"" + this.id +
				"\",\"instructorId\":\"" + this.instructorId +
				"\",\"instructorName\":\"" + this.instructorName +
				"\",\"date\":\"" + this.date +
				"\",\"startTime\":\"" + this.startTime +
				"\",\"endTime\":\"" + this.endTime +
				"\",\"highlight\":\"" + this.highlight +
				"\"}";
	}
}
