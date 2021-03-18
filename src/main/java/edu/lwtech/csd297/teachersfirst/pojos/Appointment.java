package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.*;

public class Appointment {
    private Date startDate;
	private Date endDate;
	
	private int studentID;
	private int teacherID;
	
    public Appointment(
        int startYear, int startMonth, int startDay, int startHour, int startMinute, 
        int endYear, int endMonth, int endDay, int endHour, int endMinute,
        int teacherID) 
    {
    
        this.teacherID = teacherID;
        this.startDate = newDate(startYear,startMonth,startDay,startHour,startMinute);
        this.endDate = newDate(endYear,endMonth,endDay,endHour,endMinute);
    }

	public Appointment(Date startDate, Date endDate, int studentID, int teacherID) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.studentID = studentID;
		this.teacherID = teacherID;
	}

  private static Date newDate(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		// year, month, day, hour, minute, second
		cal.set(year, month, day, hour, minute, 0);
		return cal.getTime();
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

  public int getRecID() {
		return this.teacherID;
	}

	public void setRecID(int recID) {
		// Updates the recID of POJOs that have just been added to the database
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.teacherID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.teacherID = recID;
	}

}
