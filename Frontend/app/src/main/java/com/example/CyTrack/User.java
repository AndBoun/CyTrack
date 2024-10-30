package com.example.CyTrack;

import androidx.annotation.NonNull;

import java.io.Serializable;

class User implements Serializable {
    private final int ID;

    private String firstName;

    private String lastName;
    private int age;

    private String gender;

    private int streak;

    private final String username;

    User(int ID, String username, String firstName,String lastName, int age, String gender, int streak) {
        this.ID = ID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.streak = streak;
    }

    public String getUsername() {
        return username;
    }

    int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    int getAge() {
        return age;
    }

    void setAge(int age) {
        this.age = age;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    int getStreak() {
        return streak;
    }

    void setStreak(int streak) {
        this.streak = streak;
    }

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
