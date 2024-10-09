package Controllers;

import Entity.Meal;
import Repositiories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MealController {

    @Autowired
    MealRepository mealRepo;

    /**
     * LIST of all meals in DB
     * @return list of all meals in DB
     */
    @GetMapping("/meals")
    List<Meal> getAllMeals() { return mealRepo.findAll(); }

    /**
     * READ/return contents of an individual meal
     * @param id of meal to return
     * @return individual meal object
     */
    @GetMapping("/meal{id}")
    public Map<String, Object> getMealById(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        //Find Meal by id
        Optional<Meal> existingMealOptional = mealRepo.findById(id);

        if (existingMealOptional.isPresent()) {
            Meal existingMeal = existingMealOptional.get();

            // Return the Meal information and HTTP status code
            response.put("meal", existingMeal);
            response.put("status", "200"); // HTTP 200 OK
        } else {
            // If Meal does not exist, return a 404 response
            response.put("message", "Meal not found with id: " + id);
            response.put("status", "404"); // HTTP 404 Not Found
        }

        //Return JSON response
        //SpringBoot automatically converts Java objects (like maps and classes)
        //into JSON when returned from a controller method!
        return response;
    }

    /**
     * CREATE new meal entry
     * @param meal we're attempting to add/create
     * @return response indicating whether meal was added or not
     */
    @PostMapping("/meal")
    public Map<String, String> createMeal(@RequestBody Meal meal) {

        Map<String, String> response = new HashMap<>();

        //Check if Meal with same mealName already exists
        Optional<Meal> existingMeal = mealRepo.findByName(meal.getMealName());

        if (existingMeal.isPresent()) {

            //Meal already exists -- return message indicating this
            response.put("message", "Meal already exists");
            response.put("status", "409");
        } else {
            mealRepo.save(meal);

            response.put("message", "New Meal posted correctly");
            response.put("status", "200"); // 200 OK HTTP status
        }

        return response;
    }

    /**
     * DELETE a meal row based on a given input
     */
    @DeleteMapping("/meal{id}")
    public Map<String, String> deleteMeal(@PathVariable Integer id) {

        Map<String, String> response = new HashMap<>();

        //check if Meal with given id exists
        Optional<Meal> existingMealOptional = mealRepo.findById(id);

        if (existingMealOptional.isPresent()) {
            //meal exists --delete meal
            mealRepo.deleteById(id);

            response.put("message", "Meal deleted successfully");
            response.put("status", "200");
        } else {
            //meal DNE -- return 404 response
            response.put("message", "Meal not found with id: " + id);
            response.put("status", "404");
        }

        return response;
    }





}
