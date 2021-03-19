package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.*;

public class Service implements IJsonnable {

	private int recID; // Database ID (or -1 if it isn't in the database yet)
	private String name;
	private String description;
	private String instructors;

	public Service(String name, String description, String teachers) {

		this(-1, name, description, teachers);
	}

	public Service(int recID, String name, String description, String teachers) {

		if (recID < -1)
			throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (name == null)
			throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty())
			throw new IllegalArgumentException("Invalid argument: name is empty");
		if (description == null)
			throw new IllegalArgumentException("Invalid argument: name is null");
		if (description.isEmpty())
			throw new IllegalArgumentException("Invalid argument: name is empty");
		if (teachers == null)
			throw new IllegalArgumentException("Invalid argument: name is null");
		if (teachers.isEmpty())
			throw new IllegalArgumentException("Invalid argument: name is empty");

		this.recID = recID;
		this.name = name;
		this.description = description;
		this.instructors = teachers;
	}

	public int getRecID() {
		return recID;
	}

	public void setRecID(int recID) {
		// Updates the recID of POJOs that have just been added to the database
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name == null)
			throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty())
			name = "unnamed";

		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		if (description == null)
			throw new IllegalArgumentException("Invalid argument: name is null");

		this.description = description;
	}

	public String getInstructors() {
		return this.instructors;
	}

	public List<String> getInstructorList() {
		List<String> instructors = new ArrayList<>();
		String[] instructorsSplit = this.instructors.split(",");
		for (String s : instructorsSplit) {
			instructors.add(s.trim());
		}
		return instructors;
	}

	public void setTeachers(String teachers) {
		if (teachers == null)
			throw new IllegalArgumentException("Invalid argument: name is null");

		this.instructors = teachers;
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		String record = "(R:" + this.recID + ") ";
		String descript = " (D:" + this.description.substring(0, Math.min(this.description.length(), 24)) + ")";

		return record + this.name + "/" + this.instructors + descript;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Service)) return false; // must be same type of object

		Service other = (Service) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (!this.name.equals(other.name)) return false;
		if (!this.description.equals(other.description)) return false;
		if (!this.instructors.equals(other.instructors)) return false;

		// no failures, good match
		return true;
	}

	@Override
	public String toJson() {
		return "{\"id\":\"" + this.recID +
		"\",\"name\":\"" + this.name +
		"\",\"description\":\"" + this.description +
		"\",\"instructors\":\"" + this.instructors +
		"\"}";
	}

}