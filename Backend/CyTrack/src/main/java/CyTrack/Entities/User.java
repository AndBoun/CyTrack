package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "user")
public class User {
    // ========================= Fields ========================= //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description="ID of the user", name="userID", required=true, example="1")
    private Long userID;

    /**
     * Workouts relationship and field
     */
    @OneToMany(mappedBy = "user")
    private List<Workout> workouts;


    /**
     * Meals relationship and field
     */
    @OneToMany(mappedBy = "user")
    private List<Meal> meals;


    /**
     * Friends relationship and field
     */
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id")
    )
    private List<Friends> friends = new ArrayList<>();

    /**
     * Badge relationship and field
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Badge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    @JsonIgnoreProperties("sender")
    private List<FriendRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnoreProperties("receiver")
    private List<FriendRequest> receivedRequests = new ArrayList<>();

    /**
     * Meal Categories
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealCategory> mealCategories = new ArrayList<>();

    /**
     * Workout Categories
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutCategory> workoutCategories = new ArrayList<>(); //

    @Column(nullable = false, unique = true)
    @Schema(description="Username of the user", name="username", required=true, example="user1")
    private String username;
    @Schema(description="First name of the user", name="firstName", required=true, example="John")
    private String firstName;
    @Schema(description="Last name of the user", name="lastName", required=true, example="Doe")
    private String lastName;
    @Schema(description="Password of the user", name="password", required=true, example="password")
    private String password;
    @Schema(description="Age of the user", name="age", required=true, example="21")
    private int age;
    @Schema(description = "Height of user in inches", name = "height", required=true, example = "69")
    private int height; //new
    @Schema(description = "Weight of user in inches", name = "weight", required=true, example = "150")
    private double weight; //new
    private int currentStreak;
    private int highestStreak;

    private String pfpURL; //new
    private int totalTime;
    private String gender;

    // ========================= Constructors ========================= //

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String username, String firstName, String lastName, String password, int currentStreak) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.currentStreak = currentStreak;
        this.highestStreak = currentStreak;
    }


    // ========================= Getter and Setter ========================= //
    public Long getUserID() {
        return userID;
    }
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

    public int getHeight() {return height; }

    public void setHeight(int height) {this.height = height; }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getHighestStreak() {
        return highestStreak;
    }

    public void setHighestStreak(int highestStreak) {
        this.highestStreak = highestStreak;
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

    public List<Meal> getMeals() {return meals; }

    public void setMeals(List<Meal> meals) {this.meals = meals; }

    public List<Friends> getFriends() {
        return friends;
    }

    public List<MealCategory> getMealCategories() {
        return mealCategories;
    }

    public void setMealCategories(List<MealCategory> mealCategories) {
        this.mealCategories = mealCategories;
    }

    public List<WorkoutCategory> getWorkoutCategories() {
        return workoutCategories;
    }

    public void setWorkoutCategories(List<WorkoutCategory> workoutCategories) {
        this.workoutCategories = workoutCategories;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }

    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    // Getters and Setters for badges
    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public void addBadge(Badge badge) {
        if (!badges.contains(badge)) {
            badges.add(badge);
            badge.setUser(this);
        }
    }

    public void removeBadge(Badge badge) {
        if (badges.contains(badge)) {
            badges.remove(badge);
            badge.setUser(null);
        }
    }


    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {this.totalTime = totalTime;}

    public String getPfpURL() {return pfpURL; }

    public void setPfpURL(String pfpURL) {this.pfpURL = pfpURL; }

    public void updateHighestStreak () {
        if (currentStreak > highestStreak) {
            this.highestStreak = currentStreak;
        }
    }


}