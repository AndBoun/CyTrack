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
     * @return list of all meals given that belong to a given user
     */
    @GetMapping("/{userID}/meal")
    public ResponseEntity<?> getAllMealsByUserID(@PathVariable Long userID){
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
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "Could not find meal with given mealID")
    }

        //TODO - finish refactoring and updating to use correct methods and responses
    /**
     * CREATE new meal entry
     * @param meal we're attempting to add/create
     * @return response indicating whether meal was added or not
     */
    @PostMapping("")
    public ResponseEntity<?> createMeal(@RequestBody Meal meal) {
        //Check if Meal with same mealName already exists
        Optional<Meal> existingMeal = mealRepo.findByMealName(meal.getMealName());

        if (existingMeal.isPresent()) {
            //409 conflict
            ErrorResponse response = new ErrorResponse("error", 409, "Unable to create Meal", "Meal already exists");
            return ResponseEntity.status(409).body(response);
        } else {
            //save meal in repository
            Meal savedMeal = mealRepo.save(meal);
            MealResponse response = new MealResponse(
                    "success",
                    "Meal created successfully",
                    savedMeal.getId(),
                    savedMeal.getMealName(),
                    savedMeal.getCalories(),
                    savedMeal.getProtein(),
                    savedMeal.getCarbs(),
                    savedMeal.getTime(),
                    savedMeal.getDate()
            );

            //201 Created + new meal as response body
            return ResponseEntity.status(201).body(response);
        }
    }

    /**
     * DELETE a meal row based on a given input
     */
    @DeleteMapping("/{mealId}")
    ResponseEntity<?> deleteMeal(@PathVariable Long mealId) {
        //check if Meal with given id exists
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(mealId);

        if (existingMealOptional.isPresent()) {
            //meal exists --delete meal
            mealRepo.deleteByMealId(mealId);
            DeleteResponse response = new DeleteResponse("success", 200, "Meal deleted");
            return ResponseEntity.ok(response); //204 success no content
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "failed to delete meal", "Could not find specified Meal");
            return ResponseEntity.status(404).body(response);//404 not found
        }
    }

    /**
     * UPDATE/replace existing meal based on given JSON input
     * @param mealID of Meal to update
     * @param newMeal Meal object (from JSON) containing info for updating
     * @return response status indicating success or failure
     */
    @PutMapping("/{mealID}")
    public ResponseEntity<?> updateMeal (@PathVariable Long mealID, @RequestBody Meal newMeal) {
        //
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(mealID);

        if (existingMealOptional.isPresent()) {
            Meal existingMeal = existingMealOptional.get();

            //update fields if new values not null
            if (newMeal.getMealName() != null) {
                existingMeal.setMealName(newMeal.getMealName());
            }
            if (newMeal.getCalories() != null) {
                existingMeal.setCalories(newMeal.getCalories());
            }
            if (newMeal.getProtein() != null) {
                existingMeal.setProtein(newMeal.getProtein());
            }
            if (newMeal.getCarbs() != null) {
                existingMeal.setCarbs(newMeal.getCarbs());
            }
            if (newMeal.getTime() != null) {
                existingMeal.setTime(newMeal.getTime());
            }

            Meal updatedMeal = mealRepo.save(existingMeal);
            MealResponse response = new MealResponse(
                    "success",
                    "Meal updated successfully",
                    updatedMeal.getId(),
                    updatedMeal.getMealName(),
                    updatedMeal.getCalories(),
                    updatedMeal.getProtein(),
                    updatedMeal.getCarbs(),
                    updatedMeal.getTime(),
                    updatedMeal.getDate()
            );
            return ResponseEntity.status(201).body(response);
        }

        //404 not found if meal DNE
        return ResponseEntity.notFound().build();
    }


}
