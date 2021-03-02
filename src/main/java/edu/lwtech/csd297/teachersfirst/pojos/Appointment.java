package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.Date;

public class Appointment {
    private Date startDate;
	private Date endDate;
	
	private int studentID;
	private int teacherID;
	
	public Appointment(Date StartDate, Date EndDate, int studentID, int teacherID) {
		this.startDate = StartDate;
		this.endDate = EndDate;
		
		this.studentID = studentID;
		this.teacherID = teacherID;
	}
	
	public int getStudentID() {
		return this.studentID;
	}
	
	public int getTeacherID() {
		return this.teacherID;
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
}
