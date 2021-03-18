package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;
import java.util.Calendar;

import edu.lwtech.csd297.teachersfirst.DateHelpers;

public class Opening {

	private int recID;
	private int instructorID;
	private Timestamp startTime;
	private Timestamp endTime;

	// ----------------------------------------------------------------	

	public Opening(int instructorID,
			int startYear, int startMonth, int startDay, int startHour, int startMinute,
			int endYear, int endMonth, int endDay, int endHour, int endMinute) {

		this(-1, instructorID, DateHelpers.toTimestamp(startYear, startMonth, startDay, startHour, startMinute, 0), 
				DateHelpers.toTimestamp(endYear, endMonth, endDay, endHour, endMinute, 0));
	}

	public Opening(int instructorID, Timestamp startTime, Timestamp endTime) {

		this(-1, instructorID, startTime, endTime);
	}

	public Opening(int recID, int instructorID, Timestamp startTime, Timestamp endTime) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (instructorID < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (startTime == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (endTime == null) throw new IllegalArgumentException("Invalid argument: endTime is null");

		this.recID = recID;
		this.instructorID = instructorID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// ----------------------------------------------------------------	

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		if (recID <= 0) { throw new IllegalArgumentException("setRecID: recID cannot be negative."); }
		if (this.recID != -1) { throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1)."); }

		this.recID = recID;
	}

	// ----------------------------------------------------------------	


	public Timestamp getStartTime() {
		return this.startTime;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}
	
	public int getInstructorID() {
		return this.instructorID;
	}

	public String getName() {
		return this.toString();
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Opening/" + this.instructorID + "@" + this.startTime.toString() + "-" + this.endTime.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Opening)) return false; // must be same type of object

		Opening other = (Opening) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (this.instructorID != other.instructorID) return false;
		if (!this.startTime.equals(other.startTime)) return false;
		if (!this.endTime.equals(other.endTime)) return false;

		// no failures, good match
		return true;
	}

}
