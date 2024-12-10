package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MealCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealCategoryId;

    private String mealCategoryName; // e.g., "Breakfast", "Snack"

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "meal_category_meal",
            joinColumns = @JoinColumn(name = "meal_category_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<Meal> meals = new ArrayList<>();

    // =============================== Constructors ================================== //

    //default constructor
    public MealCategory() {}

    public MealCategory(String mealCategoryName, User user) {
        this.mealCategoryName = mealCategoryName;
        this.user = user;
    }

    public MealCategory(String mealCategoryName) {
        this.mealCategoryName = mealCategoryName;
    }

    // =============================== Helper Methods ================================== //

    public void addMeal(Meal meal) {
        if (!meals.contains(meal)) {
            meals.add(meal);
            meal.getMealCategories().add(this); // Synchronize bidirectional relationship
        }
    }

    public void removeMeal(Meal meal) {
        if (meals.contains(meal)) {
            meals.remove(meal);
            meal.getMealCategories().remove(this); // Synchronize bidirectional relationship
        }
    }


    // =============================== Getters and Setters for each field ================================== //
    public Long getMealCategoryId() {return mealCategoryId; }

    public void setMealCategoryId(Long mealCategoryId) {this.mealCategoryId = mealCategoryId; }

    public String getMealCategoryName() {return mealCategoryName; }

    public void setMealCategoryName(String mealCategoryName) {this.mealCategoryName = mealCategoryName; }

    public User getUser() {return user; }

    public void setUser(User user) {this.user = user; }

    public List<Meal> getMeals() {return meals; }

    public void setMeals(List<Meal> meals) {this.meals = meals; }
}
