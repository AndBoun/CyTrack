package CyTrack.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    // ========================= Fields ========================= //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userID;
    // One user can have many workouts
    @OneToMany(mappedBy = "user")
    private List<Workout> workouts;
    // Username is unique
    @Column(nullable = false, unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private int age;
    private int streak;
    private String gender;

    // ========================= Constructor ========================= //
    public Long getUserID() {
        return userID;
    }
    // ========================= Getter and Setter ========================= //
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}