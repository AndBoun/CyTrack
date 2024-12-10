package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a table for a 'Meal' entity.
 */
@Entity
public class Meal {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealID;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "meals", fetch = FetchType.LAZY)
    private List<MealCategory> mealCategories = new ArrayList<>();

    private String mealName;
    private Integer calories;
    private Integer protein;
    private Integer carbs;
    private String time;
    private String date;

    // =============================== Constructors ================================== //

    //Default constructor
    public Meal(){}

    //Constructor with fields
    public Meal(String name, int calories, int protein, int carbs, String time, String date) {
        this.mealName = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.time = time;
        this.date = date;
    }

    // =============================== Getters and Setters for each field ================================== //


    public Long getMealId() {
        return mealID;
    }

    public void setMealId(Long id) {
        this.mealID = id;
    }

    public User getUser() {return user; }

    public void setUser(User user) {this.user = user; }

    public List<MealCategory> getMealCategories() {return mealCategories; }

    public void setMealCategories(List<MealCategory> mealCategories) {this.mealCategories = mealCategories; }

    public String getMealName() {return mealName; }

    public void setMealName(String mealName) {this.mealName = mealName; }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public String getTime() {return time; }

    public void setTime(String time) {this.time = time; }

    public String getDate() {return date; }

    public void setDate(String date) {this.date = date; }
}
