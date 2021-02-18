package edu.lwtech.csd297.skeleton.pojos;

public class Skeleton {

    // Encapsulated member variables
    private int recID;              // Database ID (or -1 if it isn't in the database yet)
    private String name;
    private int age;

    public Skeleton(String name, int age) {
        this(-1, name, age);
    }

    public Skeleton(int recID, String name, int age) {
        if (recID < -1)
            throw new IllegalArgumentException("Invalid argument: recID < -1");
        if (name == null)
            throw new IllegalArgumentException("Invalid argument: name is null");
        if (name.isEmpty())
            throw new IllegalArgumentException("Invalid argument: name is empty");
        if (age < 0)
            throw new IllegalArgumentException("Invalid argument: age cannot be negative");

        this.recID = recID;
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return recID + ": " + name + " (" + age + ")";
    }

}
