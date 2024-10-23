package CyTrack.Entities;

import jakarta.persistence.*;

/**
 * @author Eduardo Barboza-Campos
 * Class representing a table for a 'Meal' entity.
 * For now, we're just working on this table alone and not worrying too much about
 * table relationships.
 * We will worry about this later lmao.
 */
@Entity
public class Meal {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    private String mealName;
    private Integer calories;
    private Integer protein;
    private Integer carbs;
    private String time;

    // =============================== Constructors ================================== //

    public Meal(){}

    public Meal(String name, int calories, int protein, int carbs, String time) {
        this.mealName = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.time = time;

    }

    // =============================== Getters and Setters for each field ================================== //


    public Long getId() {
        return mealId;
    }

    public void setId(Long id) {
        this.mealId = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

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
}
