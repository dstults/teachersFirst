package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.*;

import edu.lwtech.csd297.teachersfirst.DateHelpers;

public class Member implements IJsonnable {

	// Encapsulated member variables
	private int recID; // Database ID (or -1 if it isn't in the database yet)

	// Login:
	private String loginName;
	private String passwordHash;

	// Bios:
	private String displayName;
	private Timestamp birthdate;
	private String gender;
	private String instructorNotes;

	// Metadata:
	private String phone1;
	private String phone2;
	private String email;

	// Permissions
	private boolean isStudent;
	private boolean isInstructor;
	private boolean isAdmin;

	// no record id, no birthdate -- this is the default for the new member made by the page
	public Member(String loginName, String passwordHash, String displayName, String gender, String instructorNotes,
					String phone1, String phone2, String email, boolean isStudent, boolean isInstructor, boolean isAdmin) {

		this(-1, loginName, passwordHash, displayName, DateHelpers.toTimestamp("1800/01/01 01:01:01"), gender, instructorNotes, phone1, phone2, email, isStudent, isInstructor, isAdmin);
	}

	// no record id, yes birthdate -- used in code
	public Member(String loginName, String passwordHash, String displayName, Timestamp birthdate, String gender, String instructorNotes,
					String phone1, String phone2, String email, boolean isStudent, boolean isInstructor, boolean isAdmin) {

		this(-1, loginName, passwordHash, displayName, birthdate, gender, instructorNotes, phone1, phone2, email, isStudent, isInstructor, isAdmin);
	}

	// everything
	public Member(int recID, String loginName, String passwordHash, String displayName, Timestamp birthdate, String gender, String instructorNotes,
					String phone1, String phone2, String email, boolean isStudent, boolean isInstructor, boolean isAdmin) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (loginName == null) throw new IllegalArgumentException("Invalid argument: loginName is null");
		if (loginName.isEmpty()) throw new IllegalArgumentException("Invalid argument: loginName is empty");
		if (passwordHash == null) throw new IllegalArgumentException("Invalid argument: passwordHash is null");
		if (passwordHash.isEmpty()) throw new IllegalArgumentException("Invalid argument: passwordHash is empty");
		//TODO: SHA1 Password Hash should be 40 chars long -- need hashing first
		if (displayName == null) throw new IllegalArgumentException("Invalid argument: displayName is null");
		if (displayName.isEmpty()) throw new IllegalArgumentException("Invalid argument: displayName is empty");
		if (birthdate == null) throw new IllegalArgumentException("Invalid argument: birthdate is null");
		if (gender == null) throw new IllegalArgumentException("Invalid argument: gender is null");
		// gender can be empty string
		if (instructorNotes == null) throw new IllegalArgumentException("Invalid argument: instructorNotes is null");
		// notes can be empty string
		if (phone1 == null) throw new IllegalArgumentException("Invalid argument: phone1 is null");
		// phone1 can be empty string
		if (phone2 == null) throw new IllegalArgumentException("Invalid argument: phone2 is null");
		// phone2 can be empty string
		if (email == null) throw new IllegalArgumentException("Invalid argument: email is null");
		// email can be empty string

		this.recID = recID;
		this.loginName = loginName;
		this.passwordHash = passwordHash;
		this.displayName = displayName;
		this.birthdate = birthdate;
		this.gender = gender;
		this.instructorNotes = instructorNotes;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.isStudent = isStudent;
		this.isInstructor = isInstructor;
		this.isAdmin = isAdmin;
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

	public String getLoginName() {
		return this.loginName;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public String getName() {
		return this.loginName + "/" + this.displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Timestamp getBirthdate() {
		return this.birthdate;
	}

	public LocalDate getBirthDate() {
		return this.birthdate.toLocalDateTime().toLocalDate();
	}

	public String getBirthDateFormatted() {
		return this.birthdate.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
	}

	public int getAge() {
		return DateHelpers.calculateAgeFrom(this.birthdate);
	}

	public String getGender() {
		return this.gender;
	}

	public String getGenderWord() {
		switch (this.gender) {
			case "m":
				return "Male";
			case "f":
				return "Female";
			case "":
				return "Other/Unspecified";
			default:
				return "[ " + this.gender + " ]";
		}
	}

	public String getInstructorNotes() {
		return this.instructorNotes;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean getIsStudent() {
		return this.isStudent;
	}

	public boolean getIsInstructor() {
		return this.isInstructor;
	}

	public boolean getIsAdmin() {
		return this.isAdmin;
	}

	// ----------------------------------------------------------------

	public void setLoginName(String loginName) {
		if (loginName == null) throw new IllegalArgumentException("Invalid argument: loginName is null");
		if (loginName.isEmpty()) throw new IllegalArgumentException("Invalid argument: loginName is empty");

		this.loginName = loginName;
	}

	public void setPasswordHash(String passwordHash) {
		if (passwordHash == null) throw new IllegalArgumentException("Invalid argument: passwordHash is null");
		if (passwordHash.isEmpty()) throw new IllegalArgumentException("Invalid argument: passwordHash is empty");
		// TODO: SHA1 Password Hash should be 40 chars long -- need hashing first

		this.passwordHash = passwordHash;
	}

	public void setDisplayName(String name) {
		if (name == null) throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty()) throw new IllegalArgumentException("Invalid argument: name is empty");

		this.displayName = name;
	}

	public void setBirthdate(int years, int months, int days, int hours, int minutes, int seconds) {
		//TODO: validate integers

		setBirthdate(DateHelpers.toTimestamp(years, months, days, hours, minutes, seconds));
	}

	public void setBirthdate(Timestamp birthdate) {
		if (birthdate == null) throw new IllegalArgumentException("Invalid argument: birthdate is null");

		this.birthdate = birthdate;
	}

	public void setGender(String gender) {
		if (gender == null) throw new IllegalArgumentException("Invalid argument: gender is null");
		// can be empty string

		this.gender = gender;
	}

	public void setInstructorNotes(String instructorNotes) {
		if (instructorNotes == null) throw new IllegalArgumentException("Invalid argument: instructorNotes is null");
		// can be empty string

		this.instructorNotes = instructorNotes;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}

	public void setIsInstructor(boolean isInstructor) {
		this.isInstructor = isInstructor;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		String recordLogin = "(R:" + this.recID + "/L:" + this.loginName + ") ";
		String permissions = (this.isStudent ? "1" : "0") + "-" + (this.isInstructor ? "1" : "0") + "-"	+ (this.isAdmin ? "1" : "0");
		String ageGenderPermissions = " (A:" + this.getAge() + "/G:" + this.gender	+ "/P:" + permissions + ")";

		return recordLogin + this.displayName + ageGenderPermissions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Member)) return false; // must be same type of object

		Member other = (Member) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (!this.loginName.equals(other.loginName)) return false;
		if (!this.passwordHash.equals(other.passwordHash)) return false;
		if (!this.displayName.equals(other.displayName)) return false;
		if (!this.birthdate.equals(other.birthdate)) return false;
		if (!this.gender.equals(other.gender)) return false;
		if (!this.instructorNotes.equals(other.instructorNotes)) return false;
		if (!this.phone1.equals(other.phone1)) return false;
		if (!this.phone2.equals(other.phone2)) return false;
		if (!this.email.equals(other.email)) return false;
		if (this.isStudent != other.isStudent) return false;
		if (this.isInstructor != other.isInstructor) return false;
		if (this.isAdmin != other.isAdmin) return false;

		// no failures, good match
		return true;
	}

	@Override
	public String toJson() {
		return "{\"id\":\"" + this.recID +
				"\",\"loginName\":\"" + this.loginName +
				//"\",\"passwordHash\":\"" + this.passwordHash +
				"\",\"displayName\":\"" + this.displayName +
				"\",\"birthdate\":\"" + this.birthdate.toLocalDateTime().toLocalDate() +
				"\",\"gender\":\"" + this.gender +
				"\",\"instructorNotes\":\"" + this.instructorNotes +
				"\",\"phone1\":\"" + this.phone1 +
				"\",\"phone2\":\"" + this.phone2 +
				"\",\"email\":\"" + this.email +
				"\",\"isStudent\":\"" + this.isStudent +
				"\",\"isInstructor\":\"" + this.isInstructor +
				"\",\"isAdmin\":\"" + this.isAdmin +
				"\"}";
	}

}