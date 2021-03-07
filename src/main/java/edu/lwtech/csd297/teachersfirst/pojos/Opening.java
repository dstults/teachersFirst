package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;
import java.util.Calendar;

import edu.lwtech.csd297.teachersfirst.DateHelpers;

public class Opening {

	private int recID;
	private int teacherID;
	private Timestamp startTime;
	private Timestamp endTime;

	// ----------------------------------------------------------------	

	public Opening(int teacherID,
			int startYear, int startMonth, int startDay, int startHour, int startMinute,
			int endYear, int endMonth, int endDay, int endHour, int endMinute) {

		this(-1, teacherID, DateHelpers.toTimestamp(startYear, startMonth, startDay, startHour, startMinute, 0), 
				DateHelpers.toTimestamp(endYear, endMonth, endDay, endHour, endMinute, 0));
	}

	public Opening(int teacherID, Timestamp startTime, Timestamp endTime) {

		this(-1, teacherID, startTime, endTime);
	}

	public Opening(int recID, int teacherID, Timestamp startTime, Timestamp endTime) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		//TODO: input validation

		this.recID = recID;
		this.teacherID = teacherID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// ----------------------------------------------------------------	

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	// ----------------------------------------------------------------	


	public Timestamp getStartTime() {
		return this.startTime;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}
	
	public int getTeacherID() {
		return this.teacherID;
	}

	public String getName() {
		return this.toString();
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Opening/" + this.teacherID + "@" + this.startTime.toString() + "-" + this.endTime.toString();
	}

}
