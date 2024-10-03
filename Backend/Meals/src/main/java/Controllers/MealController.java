package Controllers;

import Entity.Meal;
import Repositiories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MealController {

    @Autowired
    MealRepository mealRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/meals")
    List<Meal> getAllMeals() { return mealRepository.findAll(); }

    @GetMapping(path = "/meal{id}")
    Meal getMealById(@PathVariable int id) {
        return mealRepository.findById(id);
    }


}
