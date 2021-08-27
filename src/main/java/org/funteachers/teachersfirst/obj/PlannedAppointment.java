package org.funteachers.teachersfirst.obj;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.funteachers.teachersfirst.ServerMain;
import org.funteachers.teachersfirst.managers.DateHelpers;

public class PlannedAppointment {
	
	protected static final Logger logger = LogManager.getLogger();

	public static List<PlannedAppointment> MakeList(
				int studentId, int instructorId,
				List<DayOfWeek> daysOfWeek,
				int startYear, int startMonth, int startDay, int startHour, int startMinute,
				int endYear, int endMonth, int endDay, int endHour, int endMinute) {

		LocalDate iDate = LocalDate.of(startYear, startMonth, startDay);
		LocalDate endDate = LocalDate.of(endYear, endMonth, endDay).plusDays(1);
		List<PlannedAppointment> list = new ArrayList<>();
		int crossDay = endHour < startHour ? 1 : 0;
		DayOfWeek dayOfWeek;

		while (iDate.isBefore(endDate)) {
			//System.out.println("iDate: " + iDate.toString() + " -- endDate: " + endDate);
			dayOfWeek = iDate.getDayOfWeek();
			if (daysOfWeek.contains(dayOfWeek)) {
				list.add(new PlannedAppointment(
					studentId, instructorId,
					iDate.getYear(), iDate.getMonthValue(),
					iDate.getDayOfMonth(), startHour, startMinute,
					iDate.getDayOfMonth() + crossDay, endHour, endMinute
				));	
			}
			iDate = iDate.plusDays(1);
		}

		return list;
	}

	private int studentId;
	private int instructorId;
	private Timestamp startTime;
	private Timestamp endTime;
	private String result;

	// ----------------------------------------------------------------
	
	public PlannedAppointment(int studentID, int instructorID,
			int year, int month,
			int startDay, int startHour, int startMinute,
			int endDay, int endHour, int endMinute) {

		this(studentID, instructorID,
				DateHelpers.toTimestamp(year, month, startDay, startHour, startMinute, 0),
				DateHelpers.toTimestamp(year, month, endDay, endHour, endMinute, 0));
	}

	public PlannedAppointment(int studentID, int instructorID, Timestamp startTime, Timestamp endTime) {

		if (studentID < -1) throw new IllegalArgumentException("Invalid argument: studentID < -1");
		if (instructorID < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (startTime == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (endTime == null) throw new IllegalArgumentException("Invalid argument: endTime is null");
		if (endTime.before(startTime)) throw new IllegalArgumentException("Invalid argument: endTime is before startTime");
		
		this.studentId = studentID;
		this.instructorId = instructorID;
		this.startTime = startTime;
		this.endTime = endTime;

		// IMPORTANT: This shouldn't be immediately tested, because that would require
		// relentlessly spamming the database. Instead we will do a single database
		// query and then test all the planned appointments against the single query.
		this.result = "UNTESTED";
	}

	// ----------------------------------------------------------------

	public int getStudentID() {
		return this.studentId;
	}
	
	public int getInstructorID() {
		return this.instructorId;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public float getLength() {
		float diff = this.endTime.getTime() - this.startTime.getTime();
		return diff / (60 * 60 * 1000);
	}

	public String getDateString() {
		return this.startTime.toLocalDateTime().toLocalDate().toString();
	}

	public String getResult() {
		return this.result;
	}

	public boolean hasConflictWithAppointments(List<Appointment> list) {
		for(Appointment iApt : list) {
			//if (hasConflictWithAppointment(iApt, false)) return true;
			if (hasConflictWithAppointment(iApt)) return true;
		}

		this.result = "OK!";
		return false;
	}

	//private boolean hasConflictWithAppointment(Appointment iApt, boolean lastCheck) {
	private boolean hasConflictWithAppointment(Appointment iApt) {
		if (iApt.getWasCancelled()) return false;

		int p1 = iApt.getStudentID();
		int p2 = iApt.getInstructorID();
		if (p1 == this.studentId || p1 == this.instructorId || p2 == this.studentId || p2 == this.instructorId) {
			// At least one of the included members is in scheduled appointment and planned appointments
			if (DateHelpers.timeIsBetweenTimeAndTime(
				this.startTime.toLocalDateTime().plusMinutes(1),
				iApt.getStartTime().toLocalDateTime(),
				iApt.getEndTime().toLocalDateTime()) ||
			DateHelpers.timeIsBetweenTimeAndTime(
				this.endTime.toLocalDateTime().minusMinutes(1),
				iApt.getStartTime().toLocalDateTime(),
				iApt.getEndTime().toLocalDateTime()) ||
			DateHelpers.timeIsBetweenTimeAndTime( // catches edge case of appointment being completely this one
				iApt.getStartTime().toLocalDateTime().plusMinutes(1),
				this.startTime.toLocalDateTime(),
				this.endTime.toLocalDateTime())) {
	
				// Conflict found:
				this.result = "CONFLICT: " + iApt.toString();
				return true;
			}
		}

		// No conflicts found
		//if (lastCheck) this.result = "OK!";
		return false;
	}

	public String getName() {
		return this.toString();
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Appointment/Student(" + this.studentId + ")>Instructor(" + this.instructorId + ")@" + this.startTime.toString() + "-" + this.endTime.toString() + " - Result: " + this.result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof PlannedAppointment)) return false; // must be same type of object

		PlannedAppointment other = (PlannedAppointment) obj; // cast to compare fields
		if (this.studentId != other.studentId) return false;
		if (this.instructorId != other.instructorId) return false;
		if (!this.startTime.equals(other.startTime)) return false;
		if (!this.endTime.equals(other.endTime)) return false;

		// no failures, good match
		return true;
	}

}
