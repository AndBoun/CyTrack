package CyTrack.Controllers;

import CyTrack.Entities.Meal;
import CyTrack.Repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meal")
public class MealController {

    @Autowired
    MealRepository mealRepo;

    /**
     * LIST of all meals in DB
     * @return list of all meals in DB
     */
    @GetMapping("")
    public ResponseEntity<List<Meal>> getAllMeals (){
        List<Meal> meals = mealRepo.findAll();
        return ResponseEntity.ok(meals);
    }

    /**
     * READ/return contents of an individual meal
     * @param mealId of meal to return
     * @return individual meal object
     */
    @GetMapping("/{mealId}")
    public ResponseEntity<?> getMealById(@PathVariable Long mealId) {
        //Find Meal by id
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(mealId);

        if (existingMealOptional.isPresent()) {
            //201 ok + meal data
            Meal foundMeal = existingMealOptional.get();
            MealResponse response = new MealResponse(
                    "success",
                    "Meal retrieved successfully",
                    foundMeal.getId(),
                    foundMeal.getMealName(),
                    foundMeal.getCalories(),
                    foundMeal.getProtein(),
                    foundMeal.getCarbs(),
                    foundMeal.getTime()
            );
            return ResponseEntity.status(201).body(response);
        } else {
            //404 not found
            ErrorResponse error = new ErrorResponse("error", 404, "Meal not found", "The meal you are looking for does not exist");
            return ResponseEntity.status(404).body(error);
        }
    }

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
                    savedMeal.getTime()
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
                    updatedMeal.getTime()
            );
            return ResponseEntity.status(201).body(response);
        }

        //404 not found if meal DNE
        return ResponseEntity.notFound().build();
    }


}
