package edu.lwtech.csd297.teachersfirst.pojos;

public class Member {

	// Encapsulated member variables
	private int recID; // Database ID (or -1 if it isn't in the database yet)
	private String name;
	private int age;
	private String gender;
	private String favoriteColor;
	private String favoriteFood;
	private boolean isStudent;
	private boolean isInstructor;
	private boolean isAdmin;

	public Member(String name, int age, String gender, String color, String food, boolean student, boolean instructor,
			boolean admin) {
		this(-1, name, age, gender, color, food, student, instructor, admin);
	}

	public Member(int recID, String name, int age, String gender, String color, String food, boolean student,
			boolean instructor, boolean admin) {
		if (recID < -1)
			throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (name == null)
			throw new IllegalArgumentException("Invalid argument: name is null");
		if (name.isEmpty())
			throw new IllegalArgumentException("Invalid argument: name is empty");
		if (age < 0)
			throw new IllegalArgumentException("Invalid argument: age cannot be negative");
		if (gender == null)
			throw new IllegalArgumentException("Invalid argument: gender is null");
		if (gender.isEmpty())
			throw new IllegalArgumentException("Invalid argument: gender is empty");
		if (color == null || color.isEmpty())
			throw new IllegalArgumentException("Invalid argument: color is null or empty");
		if (food == null || food.isEmpty())
			throw new IllegalArgumentException("Invalid argument: food is null or empty");

		this.recID = recID;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.setFavoriteColor(color);
		this.setFavoriteFood(food);
		this.setStudent(student);
		this.setInstructor(instructor);
		this.setAdmin(admin);
	}

	// ----------------------------------------------------------------

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
		return name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age < 0)
			throw new IllegalArgumentException("Invalid argument: age cannot be negative");
		this.age = age;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isInstructor() {
		return isInstructor;
	}

	public void setInstructor(boolean isInstructor) {
		this.isInstructor = isInstructor;
	}

	public boolean isStudent() {
		return isStudent;
	}

	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}

	public String getFavoriteFood() {
		return favoriteFood;
	}

	public void setFavoriteFood(String favoriteFood) {
		this.favoriteFood = favoriteFood;
	}

	public String getFavoriteColor() {
		return favoriteColor;
	}

	public void setFavoriteColor(String favoriteColor) {
		this.favoriteColor = favoriteColor;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return recID + ": " + name + " (" + age + ") G:" + gender + " - F: " + favoriteFood;
	}

}