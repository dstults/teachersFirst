package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;

import edu.lwtech.csd297.teachersfirst.*;

public class Appointment {
    
	private int recID;
	private int studentID;
	private int instructorID;
	private Timestamp startTime;
	private Timestamp endTime;

	// ----------------------------------------------------------------
	
	public Appointment(int studentID, int instructorID,
			int startYear, int startMonth, int startDay, int startHour, int startMinute,
			int endYear, int endMonth, int endDay, int endHour, int endMinute) {

		this(-1, studentID, instructorID, DateHelpers.toTimestamp(startYear, startMonth, startDay, startHour, startMinute, 0),
				DateHelpers.toTimestamp(endYear, endMonth, endDay, endHour, endMinute, 0));
	}
	
	public Appointment(int studentID, int instructorID, Timestamp startTime, Timestamp endTime) {

		this(-1, studentID, instructorID, startTime, endTime);
	}

	public Appointment(int recID, int studentID, int instructorID, Timestamp startTime, Timestamp endTime) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		//TODO: input validation
		
		this.recID = recID;
		this.studentID = studentID;
		this.instructorID = instructorID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		// Updates the recID of POJOs that have just been added to the database
		if (recID <= 0) throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1) throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	// ----------------------------------------------------------------

	public int getStudentID() {
		return this.studentID;
	}
	
	public int getInstructorID() {
		return this.instructorID;
	}
	
	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public String getName() {
		return this.toString();
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Appointment/" + this.studentID + ">" + this.instructorID + "@" + this.startTime.toString() + "-" + this.endTime.toString();
	}

}
