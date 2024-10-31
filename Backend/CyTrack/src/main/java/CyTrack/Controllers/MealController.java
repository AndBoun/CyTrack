package CyTrack.Controllers;

import CyTrack.Entities.Meal;
import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.MealRepository;
import CyTrack.Services.MealService;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meal")
public class MealController {

    private final MealService mealService;
    private final UserService userService;

    @Autowired
    public MealController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }

    /**
     * LIST of ALL MEALS for a SPECIFIC USER
     *
     * @return list of all meals given that belong to a given user
     */
    @GetMapping("/{userID}/meal")
    public ResponseEntity<?> getAllMealsByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Meal> meals = mealService.getAllMeals(userID);
            List<MealResponse.MealData> mealDataList = meals.stream()
                    .map(meal -> new MealResponse.MealData(
                            meal.getMealId(),
                            meal.getMealName(),
                            meal.getCalories(),
                            meal.getProtein(),
                            meal.getCarbs(),
                            meal.getTime(),
                            meal.getDate()
                    ))
                    .toList();
            MealResponse response = new MealResponse("success", mealDataList, "Meals found");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Unable to find user given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * READ/return contents of an individual meal
     *
     * @param mealId of meal to return
     * @return individual meal object
     */
    @GetMapping("/{mealId}/meal/{mealID}")
    public ResponseEntity<?> getMealById(@PathVariable Long userID, @PathVariable Long mealId) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> meal = mealService.findByMealID(mealId);
            if (meal.isPresent()) {
                Meal foundMeal = meal.get();
                MealResponse.MealData mealData = new MealResponse.MealData(
                        foundMeal.getMealId(),
                        foundMeal.getMealName(),
                        foundMeal.getCalories(),
                        foundMeal.getProtein(),
                        foundMeal.getCarbs(),
                        foundMeal.getTime(),
                        foundMeal.getDate()
                );
                MealResponse response = new MealResponse("success", List.of(mealData), "Meal found");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "Could not find meal with given mealID");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Could not find user with the given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * CREATE new meal entry
     *
     * @param meal we're attempting to add/create
     * @return response indicating whether meal was added or not
     */
    @PostMapping("/{userID}/meal")
    public ResponseEntity<?> createMeal(@PathVariable Long userID, @RequestBody Meal meal) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            meal.setUser(user.get());
            Meal newMeal = mealService.createMeal(meal);

            MealIDResponse response = new MealIDResponse("success", newMeal.getMealId(), "Meal created");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Could not find user with the given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * DELETE a meal row based on a given input
     */
    @DeleteMapping("/{userID}/meal/{mealID}")
    ResponseEntity<?> deleteMeal(@PathVariable Long userID, @PathVariable Long mealID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> meal = mealService.findByMealID(mealID);
            if (meal.isPresent()) {
                mealService.deleteMeal(mealID);
                MealIDResponse response = new MealIDResponse("success", mealID, "Meal deleted");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "could not find meal with given mealID");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "could not find user with given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * UPDATE/replace existing meal based on given JSON input
     *
     * @param mealID  of Meal to update
     * @param newMeal Meal object (from JSON) containing info for updating
     * @return response status indicating success or failure
     */
    @PutMapping("/{userID}/meal/{mealID}")
    public ResponseEntity<?> updateMeal(@PathVariable Long userID, @PathVariable Long mealID, @RequestBody Meal newMeal) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> existingMeal = mealService.findByMealID(mealID);
            if (existingMeal.isPresent()) {
                Meal updatedMeal = existingMeal.get();
                //update fields if new values not null
                if (newMeal.getMealName() != null) {
                    updatedMeal.setMealName(newMeal.getMealName());
                }
                if (newMeal.getCalories() != null) {
                    updatedMeal.setCalories(newMeal.getCalories());
                }
                if (newMeal.getProtein() != null) {
                    updatedMeal.setProtein(newMeal.getProtein());
                }
                if (newMeal.getCarbs() != null) {
                    updatedMeal.setCarbs(newMeal.getCarbs());
                }
                if (newMeal.getTime() != null) {
                    updatedMeal.setTime(newMeal.getTime());
                }
                if (newMeal.getDate() != null) {
                    updatedMeal.setDate(newMeal.getDate());
                }
                mealService.createMeal(updatedMeal);
                MealIDResponse response = new MealIDResponse("success", mealID, "Meal created");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "could not find meal with given mealID");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "could not find user with given userID");
        return ResponseEntity.status(404).body(response);
    }
}
