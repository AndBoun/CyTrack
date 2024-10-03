package Entity;

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

    /**
     * Primary key -- id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    /**
     *Attribute -- meal name
     */
    @Column(name = "meal_name")
    private String mealName;

    /**
     *Attribute -- calories
     */
    @Column(name = "calories")
    private int calories;

    /**
     *Attribute -- protein
     */
    @Column(name = "protein")
    private int protein;

    /**
     *Attribute -- carbs
     */
    @Column(name = "carbs")
    private int carbs;

    //TODO--figure out best way to store timestamp



    public Meal(String name, int calories, int protein, int carbs) {
        this.mealName = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", meal name='" + mealName + '\'' +
                ", calories='" + calories + '\'' +
                ", protein='" + protein + '\'' +
                ", carbs='" + carbs + '\'' +
                '}';
    }




}
