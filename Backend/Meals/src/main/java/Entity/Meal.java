package Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 *@author Eduardo Barboza-Campos
 * Class representing a table for a 'Meal' entity.
 * For now, we're just working on this table alone and not worrying too much about
 * table relationships.
 * We will worry about this later lmao.
 */
@Entity
@Table(name ="Meal")
public class Meal {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meal_id")
    private int mealID;

    @Column(name="meal_name")
    private String mealName;

    @Column(name="calories")
    private int calories;

    @Column(name="protein")
    private int protein;

    @Column(name="carbs")
    private int carbs;

    //TODO--figure out best way to store timestamp
    //TODO--timestamp methods


    // =============================== Constructors ================================== //

    public Meal(){}

    public Meal(String name, int calories, int protein, int carbs) {
        this.mealName = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;

        //TODO initialize/set timestamp
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return mealID;
    }

    public void setId(int id) {
        this.mealID = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "mealID= " + mealID + '\'' +
                ", meal name='" + mealName + '\'' +
                ", calories='" + calories + '\'' +
                ", protein='" + protein + '\'' +
                ", carbs='" + carbs + '\'' +
                '}';
    }




}
