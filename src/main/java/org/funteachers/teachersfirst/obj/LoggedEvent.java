package org.funteachers.teachersfirst.obj;

import java.sql.Timestamp;
import java.util.*;

import org.funteachers.teachersfirst.DataManager;

public class LoggedEvent implements IJsonnable {

	private int recID; // Database ID (or -1 if it isn't in the database yet)
	private int operator;
	private Timestamp date;
	private String message;

	// ----------------------------------------------------------------
	
	public LoggedEvent(int operator, Timestamp date, String message) {
		this(-1, operator, date, message);
	}

	public LoggedEvent(int recID, int operator, Timestamp date, String message) {

		if (recID < -1)
			throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (operator < 1)
			throw new IllegalArgumentException("Invalid argument: operator < 1");
		if (date == null)
			throw new IllegalArgumentException("Invalid argument: date is null");
		if (message == null)
			throw new IllegalArgumentException("Invalid argument: message is null");
		if (message.isEmpty())
			throw new IllegalArgumentException("Invalid argument: message is empty");

		this.recID = recID;
		this.operator = operator;
		this.date = date;
		this.message = message;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return recID;
	}

	public void setRecID(int recID) {
		// Updates the recID of objects that have just been added to the database
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	public boolean update() {
		throw new IllegalAccessError("Logged events must not be modified.");
	}

	// ----------------------------------------------------------------

	public int getOperator() {
		return this.operator;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public String getMessage() {
		return this.message;
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "(Event:" + this.recID + "/OP:" + this.operator + "/DT:[" + this.date + "]/MSG:[" + this.message + "])";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof LoggedEvent)) return false; // must be same type of object

		LoggedEvent other = (LoggedEvent) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (this.operator != other.operator) return false;
		if (!this.date.equals(other.date)) return false;
		if (!this.message.equals(other.message)) return false;

		// no failures, good match
		return true;
	}

	@Override
	public String toJson() {
		return "{\"id\":" + this.recID + "," +
		"\"operator\":" + this.operator + "," +
		"\"date\":\"" + this.date + "\"," +
		"\"message\":\"" + this.message + "\"" +
		"}";
	}

}