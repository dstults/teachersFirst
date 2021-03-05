package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.Calendar;
import java.util.Date;

public class Opening {
    private Date startDate;
	private Date endDate;
	
	private int teacherID;
    private String teacherName;
	
	
	public Opening(
			int startYear, int startMonth, int startDay, int startHour, int startMinute, 
			int endYear, int endMonth, int endDay, int endHour, int endMinute,
			int teacherID, String teacherName) 
	{		
		this.teacherID = teacherID;
    	this.teacherName = teacherName;
		this.startDate = newDate(startYear,startMonth,startDay,startHour,startMinute);
		this.endDate = newDate(endYear,endMonth,endDay,endHour,endMinute);
  }

    public Opening(Date startDate, Date endDate, int teacherID, String teacherName){
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
	
	private static Date newDate(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		// year, month, day, hour, minute, second
		cal.set(year, month, day, hour, minute, 0);
		return cal.getTime();
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
    /*
	public int getTeacherID() {
		return this.teacherID;
	}
    */

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

    public String getName(){
        return this.teacherName;
    }

    public Appointment newAppointment(Date startDate, int duration, int studentID){
        Appointment newAppointment = new Appointment(startDate, addMinutes(startDate, duration), studentID, this.teacherID);
        //check other appoints from the appointment dao
		//approve or disapprove
        return newAppointment;
    }

    public Date addMinutes(Date date, int minutes){
        int currentMinutes = date.getMinutes();
        date.setMinutes(currentMinutes + minutes);
        return date;
    }

}
