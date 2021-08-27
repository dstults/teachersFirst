package org.funteachers.teachersfirst.obj;

import java.time.format.DateTimeFormatter;

import org.funteachers.teachersfirst.managers.*;

import java.sql.Timestamp;

public class Appointment implements IJsonnable {

	public static final int STATE_MISSED_REFUNDED = -2;
	public static final int STATE_MISSED = -1;
	public static final int STATE_UNKNOWN = 0;
	public static final int STATE_COMPLETED = 1;
	public static final int STATE_CANCELLED = 2;

	private int recId;
	private int studentId;
	private int instructorId;
	private Timestamp startTime;
	private Timestamp endTime;
	private boolean schedulingVerified;
	private int completionState;

	private String studentName; // Temporary/Prettification use only -- should not be saved to database
	private String instructorName; // Temporary/Prettification use only -- should not be saved to database

	// ----------------------------------------------------------------
	
	public Appointment(int studentID, int instructorID,
			int year, int month,
			int startDay, int startHour, int startMinute,
			int endDay, int endHour, int endMinute,
			boolean isConfirmed) {

		this(-1, studentID, instructorID, DateHelpers.toTimestamp(year, month, startDay, startHour, startMinute, 0),
				DateHelpers.toTimestamp(year, month, endDay, endHour, endMinute, 0), isConfirmed, -1);
	}
	
	public Appointment(int studentID, int instructorID, Timestamp startTime, Timestamp endTime, boolean isConfirmed) {

		this(-1, studentID, instructorID, startTime, endTime, isConfirmed, -1);
	}
	
	public Appointment(PlannedAppointment plan) {
		if (plan.getStudentID() < -1) throw new IllegalArgumentException("Invalid argument: studentID < -1");
		if (plan.getInstructorID() < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (plan.getStartTime() == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (plan.getEndTime() == null) throw new IllegalArgumentException("Invalid argument: endTime is null");
		if (!plan.getResult().contains("OK")) throw new IllegalArgumentException("Cannot create appointment from an untested or conflicting planned appointment."); // Invalid operation
		
		this.recId = -1;
		this.studentId = plan.getStudentID();
		this.instructorId = plan.getInstructorID();
		this.startTime = plan.getStartTime();
		this.endTime = plan.getEndTime();
		this.schedulingVerified = false;
		this.completionState = STATE_UNKNOWN;
	}

	public Appointment(int recID, int studentID, int instructorID, Timestamp startTime, Timestamp endTime, boolean schedulingVerified, int completionState) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (studentID < -1) throw new IllegalArgumentException("Invalid argument: studentID < -1");
		if (instructorID < -1) throw new IllegalArgumentException("Invalid argument: instructorID < -1");
		if (startTime == null) throw new IllegalArgumentException("Invalid argument: startTime is null");
		if (endTime == null) throw new IllegalArgumentException("Invalid argument: endTime is null");
		if (completionState < -2 || completionState > 2) throw new IllegalArgumentException("Invalid argument: completionState range is -2 to 2");

		this.recId = recID;
		this.studentId = studentID;
		this.instructorId = instructorID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.schedulingVerified = schedulingVerified;
		this.completionState = completionState;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return this.recId;
	}

	public void setRecID(int recID) {
		// Updates the recID of objects that have just been added to the database
		if (recID <= 0){ throw new IllegalArgumentException("setRecID: recID cannot be negative."); }
		if (this.recId != (-1)){ throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1)."); }

		this.recId = recID;
	}

	public boolean update(ConnectionPackage cp) {
		return cp.getAppointmentDAO().update(this);
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

	public String getStartTimeFormatted() {
		return this.startTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public String getEndTimeFormatted() {
		return this.endTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	public float getLength() {
		float diff = this.endTime.getTime() - this.startTime.getTime();
		return diff / (60 * 60 * 1000);
	}

	public String getDateFormatted() {
		return this.startTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	public String getName() {
		return this.toString();
	}

	public boolean getSchedulingVerified() {
		return this.schedulingVerified;
	}

	public int getCompletionState() {
		return this.completionState;
	}

	public boolean getCompletionUnconfirmed() {
		return this.completionState == STATE_UNKNOWN;
	}

	public boolean getWasCompleted() {
		return this.completionState == STATE_COMPLETED;
	}

	public boolean getWasCancelled() {
		return this.completionState == STATE_CANCELLED;
	}

	public void setSchedulingVerified(ConnectionPackage cp, boolean value) {
		this.schedulingVerified = value;
		cp.getAppointmentDAO().update(this);
	}

	public boolean setCompletionState(ConnectionPackage cp, int value, int operator, String operatorName) {
		if (!this.hasRefundableValue()) return false;

		this.completionState = value;
		cp.getAppointmentDAO().update(this);
		
		if (!this.hasRefundableValue()) {
			final Member student = cp.getMemberDAO().retrieveByID(this.studentId);
			final String refundProcess;
			if (value == STATE_CANCELLED) {
				refundProcess = "cancel auto-refund";
			} else if (value == STATE_MISSED_REFUNDED) {
				refundProcess = "missed manual refund";
			} else {
				throw new IllegalArgumentException("Invalid refund process!");
			}
			final float myCredits = this.getLength();
			final float newCredits = student.getCredits() + myCredits;
			student.setCredits(cp, operator, operatorName, "appointment[" + this.recId + "] " + refundProcess + " len=" + myCredits + " hrs", newCredits);
		}

		return true;
	}

	public String getStatusText() {
		String status1 = this.schedulingVerified ? "verified" : "UNVERIFIED";
		String status2;
		switch(this.completionState) {
			case -1:
				status2 = "NEEDS COMPLETION CONFIRMATION";
				break;
			case 0:
				status2 = "CANCELLED";
				break;
			case 1:
				status2 = "completed";
				break;
			default:
				status2 = "";
				break;
		}
		return status1 + "/" + status2;
	}

	// ----------------------------------------------------------------

	public boolean canBeDeleted() {
		return canBeDeleted(this.completionState);
	}

	public static boolean canBeDeleted(int state) {
		switch (state) {
			case STATE_MISSED_REFUNDED:
				return false;
			case STATE_MISSED:
				return false;
			case STATE_UNKNOWN:
				return false;			
			case STATE_COMPLETED:
				return false;
			case STATE_CANCELLED:
				return true;
			default: 
				throw new IllegalArgumentException("Need to add support for canBeDeleted for completionState of [" + state + "]");
		}
	}

	public boolean hasRefundableValue() {
		return hasRefundableValue(this.completionState);
	}

	public static boolean hasRefundableValue(int state) {
		switch (state) {
			case STATE_MISSED_REFUNDED:
				return false;
			case STATE_MISSED:
				return true;
			case STATE_UNKNOWN:
				return true;			
			case STATE_COMPLETED:
				return true;
			case STATE_CANCELLED:
				return false;
			default: 
				throw new IllegalArgumentException("Need to add support for hasRefundableValue for completionState of [" + state + "]");
		}
	}

	// ----------------------------------------------------------------

	public String getStudentName() {
		if (this.studentName == null || this.studentName == "")
			return "unset";
		return this.studentName;
	}

	public String getInstructorName() {
		if (this.instructorName == null || this.instructorName == "")
			return "unset";
		return this.instructorName;
	}

	public void setStudentName(String value) {
		this.studentName = value;
	}

	public void setInstructorName(String value) {
		this.instructorName = value;
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Appointment(" + this.recId + "): Student(" + this.studentId + ") > Instructor(" + this.instructorId + ") @ " + this.getDateFormatted() + " " + this.getStartTimeFormatted() + " - " + this.getEndTimeFormatted();
	}

	@Override
	public String toJson() {
		return "{\"id\":" + this.recId + "," +
			"\"instructorId\":" + this.instructorId + "," +
			"\"instructorName\":\"" + this.getInstructorName() + "\"," +
			"\"studentId\":" + this.studentId + "," +
			"\"studentName\":\"" + this.getStudentName() + "\"," +
			"\"startTime\":\"" + this.startTime + "\"," +
			"\"endTime\":\"" + this.endTime + "\"," +
			"\"dateFormatted\":\"" + this.getDateFormatted() + "\"," +
			"\"startTimeFormatted\":\"" + this.getStartTimeFormatted() + "\"," +
			"\"endTimeFormatted\":\"" + this.getEndTimeFormatted() + "\"," +
			"\"schedulingVerified\":" + this.getSchedulingVerified() + "," +
			"\"completionState\":" + this.getCompletionState() + "," +
			"\"statusText\":\"" + this.getStatusText() + "\"" +
			"}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Appointment)) return false; // must be same type of object

		Appointment other = (Appointment) obj; // cast to compare fields
		if (this.recId != other.recId) return false;
		if (this.studentId != other.studentId) return false;
		if (this.instructorId != other.instructorId) return false;
		if (!this.startTime.equals(other.startTime)) return false;
		if (!this.endTime.equals(other.endTime)) return false;
		if (this.schedulingVerified != other.schedulingVerified) return false;
		if (this.completionState != other.completionState) return false;

		// no failures, good match
		return true;
	}

}
