package Controllers;

import Entity.Meal;
import Repositiories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MealController {

    @Autowired
    MealRepository mealRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * return list of all meals in DB
     * @return
     */
    @GetMapping(path = "/meals")
    List<Meal> getAllMeals() { return mealRepository.findAll(); }

    /**
     * return contents of an individual meal
     * @param id
     * @return individual meal object
     */
    @GetMapping(path = "/meal{id}")
    Meal getMealById(@PathVariable int id) {
        return mealRepository.findById(id);
    }




}
