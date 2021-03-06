package edu.lwtech.csd297.teachersfirst.pojos;

public class Member {

	// Encapsulated member variables
	private int recID; // Database ID (or -1 if it isn't in the database yet)

	// Login:
	private String loginName;
	private String passwordHash;

	// Bios:
	private String name;
	private int age;
	private String gender;
	private String teacherNotes;

	// Permissions
	private boolean isStudent;
	private boolean isInstructor;
	private boolean isAdmin;

	public Member(String loginName, String passwordHash, String name, int age, String gender, String teacherNotes, boolean isStudent, boolean isInstructor, boolean isAdmin) {

		this(-1, loginName, passwordHash, name, age, gender, teacherNotes, isStudent, isInstructor, isAdmin);
	}

	public Member(int recID, String loginName, String passwordHash, String name, int age, String gender, String teacherNotes, boolean isStudent, boolean isInstructor, boolean isAdmin) {

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (loginName == null) throw new IllegalArgumentException("Invalid argument: loginName is null");
		if (loginName.isEmpty()) throw new IllegalArgumentException("Invalid argument: loginName is empty");
		if (passwordHash == null) throw new IllegalArgumentException("Invalid argument: passwordHash is null");
		if (passwordHash.isEmpty()) throw new IllegalArgumentException("Invalid argument: passwordHash is empty");
		// TODO: SHA1 Password Hash should be 40 chars long -- need hashing first
		if (name == null) throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty()) throw new IllegalArgumentException("Invalid argument: name is empty");
		if (age < 0) throw new IllegalArgumentException("Invalid argument: age cannot be negative");
		if (gender == null) throw new IllegalArgumentException("Invalid argument: gender is null");
		// gender can be empty string
		if (teacherNotes == null) throw new IllegalArgumentException("Invalid argument: teacherNotes is null");
		// notes can be empty string

		this.recID = recID;
		this.loginName = loginName;
		this.passwordHash = passwordHash;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.teacherNotes = teacherNotes;
		this.isStudent = isStudent;
		this.isInstructor = isInstructor;
		this.isAdmin = isAdmin;
	}

	// ----------------------------------------------------------------

	public int getRecID() {
		return recID;
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
		return this.name;
	}

	public int getAge() {
		return this.age;
	}

	public String getGender() {
		return this.gender;
	}

	public String getTeacherNotes() {
		return this.teacherNotes;
	}

	public boolean isStudent() {
		return this.isStudent;
	}

	public boolean isInstructor() {
		return this.isInstructor;
	}

	public boolean isAdmin() {
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

	public void setName(String name) {
		if (name == null) throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty()) throw new IllegalArgumentException("Invalid argument: name is empty");

		this.name = name;
	}

	public void setAge(int age) {
		if (age < 0) throw new IllegalArgumentException("Invalid argument: age cannot be negative");

		this.age = age;
	}

	public void setGender(String gender) {
		if (gender == null) throw new IllegalArgumentException("Invalid argument: gender is null");
		// can be empty string

		this.gender = gender;
	}

	public void setTeacherNotes(String teacherNotes) {
		if (teacherNotes == null) throw new IllegalArgumentException("Invalid argument: teacherNotes is null");
		// can be empty string

		this.teacherNotes = teacherNotes;
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
		String permissions = (this.isStudent ? "1" : "0") + "-" + (this.isInstructor ? "1" : "0") + "-"	+ (this.isStudent ? "1" : "0");
		String ageGenderPermissions = " (A:" + this.age + "/G:" + this.gender	+ "/P:" + permissions + ")";

		return recordLogin + this.name + ageGenderPermissions;
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
		if (!this.name.equals(other.name)) return false;
		if (this.age != other.age) return false;
		if (!this.gender.equals(other.gender)) return false;
		if (!this.teacherNotes.equals(other.teacherNotes)) return false;
		if (this.isStudent != other.isStudent) return false;
		if (this.isInstructor != other.isInstructor) return false;
		if (this.isAdmin != other.isAdmin) return false;

		// no failures, good match
		return true;
	}

}