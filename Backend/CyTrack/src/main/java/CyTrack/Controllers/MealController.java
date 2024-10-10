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
public class MealController {

    @Autowired
    MealRepository mealRepo;

    /**
     * LIST of all meals in DB
     * @return list of all meals in DB
     */
    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals (){
        List<Meal> meals = mealRepo.findAll();
        return ResponseEntity.ok(meals);
    }

    /**
     * READ/return contents of an individual meal
     * @param id of meal to return
     * @return individual meal object
     */
    @GetMapping("/meal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Integer id) {
        //Find Meal by id
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(id);

        if (existingMealOptional.isPresent()) {
            //200 ok + meal data
           return ResponseEntity.ok(existingMealOptional.get());
        } else {

            //404 not found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * CREATE new meal entry
     * @param meal we're attempting to add/create
     * @return response indicating whether meal was added or not
     */
    @PostMapping("/meal")
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {

        //Check if Meal with same mealName already exists
        Optional<Meal> existingMeal = mealRepo.findByMealName(meal.getMealName());

        if (existingMeal.isPresent()) {
            //409 conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            //save meal in repository
            Meal savedMeal = mealRepo.save(meal);

            //201 Created + new meal as response body
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMeal);
        }
    }

    /**
     * DELETE a meal row based on a given input
     */
    @DeleteMapping("/meal/{id}")
    ResponseEntity<String> deleteMeal(@PathVariable Integer id) {
        //check if Meal with given id exists
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(id);

        if (existingMealOptional.isPresent()) {
            //meal exists --delete meal
            mealRepo.deleteById(id);
            return ResponseEntity.ok("Meal deleted successfully"); //204 success no content
        } else {
            return ResponseEntity.notFound().build();//404 not found
        }
    }

    /**
     * UPDATE/replace existing meal based on given JSON input
     * @param id of Meal to update
     * @param newMeal Meal object (from JSON) containing info for updating
     * @return response status indicating success or failure
     */
    @PutMapping("/meal/{id}")
    public ResponseEntity<Meal> updateMeal (@PathVariable Integer id, @RequestBody Meal newMeal) {
        //
        Optional<Meal> existingMealOptional = mealRepo.findByMealId(id);

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

            Meal updatedMeal = mealRepo.save(existingMeal);
            return ResponseEntity.ok(updatedMeal);
        }

        //404 not found if meal DNE
        return ResponseEntity.notFound().build();
    }


}
