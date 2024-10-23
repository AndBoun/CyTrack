package CyTrack.Services;

import CyTrack.Controllers.MealResponse;
import CyTrack.Repositories.MealRepository;
import CyTrack.Repositories.WorkoutRepository;
import org.springframework.stereotype.Service;
import CyTrack.Entities.Meal;

@Service
public class MealService {

    private final MealRepository mealRepo;

    public mealService (MealRepository mealRepository) {
        this.mealRepo = mealRepository;
    }


}
