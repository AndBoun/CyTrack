package com.example.CyTrack.Utilities;

import androidx.annotation.NonNull;

import java.io.Serializable;


/**
 * Represents a user with various attributes.
 */
public class User implements Serializable {
    /**
     * The unique identifier for the user.
     */
    private final int ID;

    /**
     * The username of the user.
     */
    private final String username;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The age of the user.
     */
    private int age;

    /**
     * The gender of the user.
     */
    private String gender;

    /**
     * The current streak of the user.
     */
    private int streak;



    private int weight;

    private int height;

    private String profileImg;

    /**
     * Constructs a new User.
     *
     * @param ID        the unique identifier for the user
     * @param username  the username of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param age       the age of the user
     * @param gender    the gender of the user
     * @param streak    the current streak of the user
     */
    public User(int ID, String username, String firstName, String lastName, int age, String gender, int streak) {
        this.ID = ID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.streak = streak;
    }

    public User(
            int ID,
            String username,
            String firstName,
            String lastName,
            int age,
            String gender,
            int streak,
            int weight,
            int height,
            String profileImg
    ){
        this(ID, username, firstName, lastName, age, gender, streak);
        this.weight = weight;
        this.height = height;
        this.profileImg = profileImg;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    /**
     * Gets the ID of the user.
     *
     * @return the ID of the user
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the age of the user.
     *
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the user.
     *
     * @param age the new age of the user
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the gender of the user.
     *
     * @return the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender the new gender of the user
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the current streak of the user.
     *
     * @return the current streak of the user
     */
    public int getStreak() {
        return streak;
    }

    /**
     * Sets the current streak of the user.
     *
     * @param streak the new streak of the user
     */
    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", streak=" + streak +
                '}';
    }
}