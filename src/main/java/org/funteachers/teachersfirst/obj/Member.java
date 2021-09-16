package org.funteachers.teachersfirst.obj;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.*;

import org.funteachers.teachersfirst.managers.*;

public class Member implements IJsonnable {

	// Encapsulated member variables
	private int recID; // Database ID (or -1 if it isn't in the database yet)

	// Login:
	private String loginName;

	// Funtional:
	private String displayName;
	private float credits;

	// Descriptive:
	private Timestamp birthdate;
	private String gender;
	private String instructorNotes;
	private String selfIntroduction;

	// Metadata:
	private String phone1;
	private String phone2;
	private String email;

	// Permissions
	private boolean isStudent;
	private boolean isInstructor;
	private boolean isAdmin;
	private boolean isDeleted;

	// no record id, no birthdate -- this is the default for the new member made by the page
	public Member(String loginName, String displayName, float credits,
					String gender,
					String selfIntroduction, String instructorNotes,
					String phone1, String phone2, String email, boolean isAdmin, boolean isInstructor, boolean isStudent) {

		this(-1, loginName, displayName, credits, DateHelpers.toTimestamp("1800/01/01 01:01:01"), gender, selfIntroduction, instructorNotes, phone1, phone2, email, isAdmin, isInstructor, isStudent, false);
	}

	// no record id, yes birthdate -- used in test code
	public Member(String loginName, String displayName, float credits,
					Timestamp birthdate, String gender,
					String selfIntroduction, String instructorNotes,
					String phone1, String phone2, String email, boolean isAdmin, boolean isInstructor, boolean isStudent, boolean isDeleted) {

		this(-1, loginName, displayName, credits, birthdate, gender, selfIntroduction, instructorNotes, phone1, phone2, email, isAdmin, isInstructor, isStudent, isDeleted);
	}

	// everything
	public Member(int recID, String loginName, String displayName, float credits,
				Timestamp birthdate, String gender,
				String selfIntroduction, String instructorNotes,
				String phone1, String phone2, String email, boolean isAdmin, boolean isInstructor, boolean isStudent, boolean isDeleted) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (loginName == null) throw new IllegalArgumentException("Invalid argument: loginName is null");
		if (loginName.isEmpty()) throw new IllegalArgumentException("Invalid argument: loginName is empty");
		if (displayName == null) throw new IllegalArgumentException("Invalid argument: displayName is null");
		if (displayName.isEmpty()) throw new IllegalArgumentException("Invalid argument: displayName is empty");
		if (birthdate == null) throw new IllegalArgumentException("Invalid argument: birthdate is null");
		if (gender == null) throw new IllegalArgumentException("Invalid argument: gender is null");
		// gender can be empty string
		if (selfIntroduction == null) throw new IllegalArgumentException("Invalid argument: selfIntroduction is null");
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
		this.displayName = displayName;
		this.credits = credits;
		this.birthdate = birthdate;
		this.gender = gender;
		this.selfIntroduction = selfIntroduction;
		this.instructorNotes = instructorNotes;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.isAdmin = isAdmin;
		this.isInstructor = isInstructor;
		this.isStudent = isStudent;
		this.isDeleted = isDeleted;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		// Updates the recID of objects that have just been added to the database
		if (recID <= 0) throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1) throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	public boolean update(ConnectionPackage cp) {
		return cp.getMemberDAO(this.getClass().getSimpleName()).update(this);
	}

	// ----------------------------------------------------------------

	public String getLoginName() {
		return this.loginName;
	}

	public String getName() {
		return this.loginName + "/" + this.displayName + this.getIsDeletedString();
	}

	public String getDisplayName() {
		return this.displayName + this.getIsDeletedString();
	}

	public float getCredits() {
		return credits;
	}

	public Timestamp getBirthdate() {
		return this.birthdate;
	}

	public LocalDate getBirthDate() {
		return this.birthdate.toLocalDateTime().toLocalDate();
	}

	public String getBirthDateFormatted() {
		return getBirthDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
	}

	public String getBirthDateView() {
		if (getBirthDate().getYear() == 1800 && getBirthDate().getMonth() == Month.JANUARY && getBirthDate().getDayOfMonth() == 1)
			return "unset";
		return getBirthDateFormatted();
	}

	public int getAge() {
		int age = DateHelpers.calculateAgeFrom(this.birthdate);
		return age >= 0 && age < 130 ? age : -1;
	}

	public String getAgeClass() {
		int age = DateHelpers.calculateAgeFrom(this.birthdate);
		if (age < 10) return "child";
		if (age < 13) return "pre-teen";
		if (age < 18) return "teenager";
		if (age < 55) return "adult";
		if (age < 130) return "senior";
		return "-";
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

	public String getSelfIntroduction() {
		return selfIntroduction;
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

	public boolean getIsAdmin() {
		if (this.isDeleted) return false;
		return this.isAdmin;
	}

	public boolean getIsInstructor() {
		if (this.isDeleted) return false;
		return this.isInstructor;
	}

	public boolean getIsStudent() {
		if (this.isDeleted) return false;
		return this.isStudent;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	private String getIsDeletedString() {
		return this.isDeleted ? " (DELETED)" : "";
	}

	// ----------------------------------------------------------------

	public void setLoginName(String loginName) {
		if (loginName == null) throw new IllegalArgumentException("Invalid argument: loginName is null");
		if (loginName.isEmpty()) throw new IllegalArgumentException("Invalid argument: loginName is empty");

		this.loginName = loginName;
	}

	public void setDisplayName(String name) {
		if (name == null) throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty()) throw new IllegalArgumentException("Invalid argument: name is empty");

		this.displayName = name;
	}

	public void setCredits(ConnectionPackage cp, int operator, String operatorName, String method, float credits) {
		float oldCredits = this.credits;
		this.credits = credits;
		if (cp != null && cp.getIsConnectionHealthy()) {
			cp.getMemberDAO(this.getClass().getSimpleName()).update(this);
			LoggedEvent.log(cp, operator, operatorName + " > CHANGE CREDITS (" + method + ") > " + this.displayName + " -- [" + oldCredits + "] > [" + credits + "]");
		}
	}

	public void setBirthdate(int years, int months, int days, int hours, int minutes, int seconds) {
		//TODO: validate integers to make sure they make sense

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

	public void setSelfIntroduction(String selfIntroduction) {
		if (selfIntroduction == null) throw new IllegalArgumentException("Invalid argument: selfIntroduction is null");
		// can be empty string
		this.selfIntroduction = selfIntroduction;
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

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	// ----------------------------------------------------------------

	@Override
	public String toString() {
		String recordLogin = "(R:" + this.getRecID() + "/L:" + this.getLoginName() + ") ";
		String permissions = (this.getIsAdmin() ? "1" : "0") + "-" + (this.getIsInstructor() ? "1" : "0") + "-" + (this.getIsStudent() ? "1" : "0");
		String ageGenderPermissions = " (A:" + this.getAge() + "/G:" + this.getGender()	+ "/P:" + permissions + ")";

		return recordLogin + this.getDisplayName() + ageGenderPermissions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Member)) return false; // must be same type of object

		Member other = (Member) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (!this.loginName.equals(other.loginName)) return false;
		if (!this.displayName.equals(other.displayName)) return false;
		if (this.credits != other.credits) return false;
		if (!this.birthdate.equals(other.birthdate)) return false;
		if (!this.gender.equals(other.gender)) return false;
		if (!this.selfIntroduction.equals(other.selfIntroduction)) return false;
		if (!this.instructorNotes.equals(other.instructorNotes)) return false;
		if (!this.phone1.equals(other.phone1)) return false;
		if (!this.phone2.equals(other.phone2)) return false;
		if (!this.email.equals(other.email)) return false;
		if (this.isStudent != other.isStudent) return false;
		if (this.isInstructor != other.isInstructor) return false;
		if (this.isAdmin != other.isAdmin) return false;
		if (this.isDeleted != other.isDeleted) return false;

		// no failures, good match
		return true;
	}

	@Override
	public String toJson() {
		return toJsonPrivate();
	}

	public String toJsonPrivate() {
		int ageVal = this.getAge();
		String age = ageVal == -1 ? "null" : String.valueOf(ageVal);
		String birthdateRaw = this.getBirthDate().toString();
		String birthdate = birthdateRaw.equals("1800-01-01") ? "null" : "\"" + birthdateRaw + "\"";
		return "{\"id\":" + this.getRecID() + "," +
				"\"loginName\":\"" + this.getLoginName() + "\"," +
				"\"displayName\":\"" + this.getDisplayName() + "\"," +
				"\"credits\":" + this.getCredits() + "," +
				"\"birthdate\":" + birthdate + "," +
				"\"age\":" + age + "," +
				"\"ageClass\":\"" + this.getAgeClass() + "\"," +
				"\"gender\":\"" + this.getGender() + "\"," +
				"\"selfIntroduction\":\"" + this.getSelfIntroduction() + "\"," +
				"\"instructorNotes\":\"" + this.getInstructorNotes() + "\"," +
				"\"phone1\":\"" + this.getPhone1() + "\"," +
				"\"phone2\":\"" + this.getPhone2() + "\"," +
				"\"email\":\"" + this.getEmail() + "\"," +
				"\"isAdmin\":" + this.getIsAdmin() + "," +
				"\"isInstructor\":" + this.getIsInstructor() + "," +
				"\"isStudent\":" + this.getIsStudent() +
				"}";
	}

	public String toJsonPublic() {
		return "{\"id\":" + this.getRecID() + "," +
				"\"displayName\":\"" + this.getDisplayName() + "\"," +
				"\"ageClass\":\"" + this.getAgeClass() + "\"," +
				"\"gender\":\"" + this.getGender() + "\"," +
				"\"selfIntroduction\":\"" + this.getSelfIntroduction() + "\"" +
				"}";
	}

}