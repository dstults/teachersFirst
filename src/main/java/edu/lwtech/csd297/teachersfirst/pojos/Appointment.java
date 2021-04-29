package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;

import edu.lwtech.csd297.teachersfirst.*;

public class Appointment {
    
	private int recID;
	private int studentId;
	private int instructorId;
	private Timestamp startTime;
	private Timestamp endTime;

	// ----------------------------------------------------------------
	
	public Appointment(int studentID, int instructorID,
			int year, int month,
			int startDay, int startHour, int startMinute,
			int endDay, int endHour, int endMinute) {

		this(-1, studentID, instructorID, DateHelpers.toTimestamp(year, month, startDay, startHour, startMinute, 0),
				DateHelpers.toTimestamp(year, month, endDay, endHour, endMinute, 0));
	}
	
	public Appointment(int studentID, int instructorID, Timestamp startTime, Timestamp endTime) {

		this(-1, studentID, instructorID, startTime, endTime);
	}
	
	public Appointment(PlannedAppointment plan) {
		if (plan.getStudentID() < -1) throw new IllegalArgumentException("Invalid argument: studentID < -1");
		if (plan.getInstructorID() < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (plan.getStartTime() == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (plan.getEndTime() == null) throw new IllegalArgumentException("Invalid argument: endTime is null");
		if (!plan.getResult().contains("OK")) throw new IllegalArgumentException("Cannot create appointment from an untested or conflicting planned appointment."); // Invalid operation
		
		this.recID = -1;
		this.studentId = plan.getStudentID();
		this.instructorId = plan.getInstructorID();
		this.startTime = plan.getStartTime();
		this.endTime = plan.getEndTime();
	}

	public Appointment(int recID, int studentID, int instructorID, Timestamp startTime, Timestamp endTime) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (studentID < -1) throw new IllegalArgumentException("Invalid argument: studentID < -1");
		if (instructorID < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (startTime == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (endTime == null) throw new IllegalArgumentException("Invalid argument: endTime is null");
		
		this.recID = recID;
		this.studentId = studentID;
		this.instructorId = instructorID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		// Updates the recID of POJOs that have just been added to the database
		if (recID <= 0){ throw new IllegalArgumentException("setRecID: recID cannot be negative."); }
		if (this.recID != (-1)){ throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1)."); }

		this.recID = recID;
	}

	// ----------------------------------------------------------------

	public int getStudentID() {
		return this.studentId;
	}
	
	public int getInstructorID() {
		return this.instructorId;
	}
	
	public boolean getIsMyAppointment(int memberId) {
		return this.studentId == memberId || this.instructorId == memberId;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public String getDateString() {
		return this.startTime.toLocalDateTime().toLocalDate().toString();
	}

	public String getName() {
		return this.toString();
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Appointment(" + this.recID + ")/Student(" + this.studentId + ")>Instructor(" + this.instructorId + ")@" + this.startTime.toString() + "-" + this.endTime.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Appointment)) return false; // must be same type of object

		Appointment other = (Appointment) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (this.studentId != other.studentId) return false;
		if (this.instructorId != other.instructorId) return false;
		if (!this.startTime.equals(other.startTime)) return false;
		if (!this.endTime.equals(other.endTime)) return false;

		// no failures, good match
		return true;
	}

}
