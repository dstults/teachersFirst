package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;
import java.util.Calendar;

import edu.lwtech.csd297.teachersfirst.DateHelpers;

public class Opening {

	private int recID;
	private int teacherID;
	private Timestamp startDate;
	private Timestamp endDate;

	// ----------------------------------------------------------------	

	public Opening(int teacherID,
			int startYear, int startMonth, int startDay, int startHour, int startMinute,
			int endYear, int endMonth, int endDay, int endHour, int endMinute) {

		this(-1, teacherID, DateHelpers.ToTimestamp(startYear, startMonth, startDay, startHour, startMinute, 0), 
				DateHelpers.ToTimestamp(endYear, endMonth, endDay, endHour, endMinute, 0));
	}

	public Opening(int recID, int teacherID, Timestamp startDate, Timestamp endDate) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		//TODO: input validation

		this.recID = recID;
		this.teacherID = teacherID;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// ----------------------------------------------------------------	

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.teacherID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.teacherID = recID;
	}

	// ----------------------------------------------------------------	


	public Timestamp getStartDate() {
		return this.startDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
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
		return "Opening/" + this.teacherID + "@" + this.startDate.toString() + "-" + this.endDate.toString();
	}

}
