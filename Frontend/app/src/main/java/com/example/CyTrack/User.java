package com.example.CyTrack;

import androidx.annotation.NonNull;

import java.io.Serializable;

class User implements Serializable {
    private final int ID;

    private String name;

    private int age;

    private String gender;

    private int streak;

    User(int ID, String name, int age, String gender, int streak) {
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.streak = streak;
    }

    int getID() {
        return ID;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
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

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", streak=" + streak +
                '}';
    }
}
