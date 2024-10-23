package CyTrack.Controllers;

import CyTrack.Entities.MealLog;
import CyTrack.Repositories.MealLogRepository;
import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/mealLog")
public class MealLogController {

    private final UserService userService;

    @Autowired
    MealLogRepository mealLogRepo;


    @Autowired
    public MealLogController(UserService userService) {
        this.userService = userService;
    }

    /**
     * LIST ALL MealLogs for a SPECIFIC USER
     * @param userID
     * @return list of ALL MEALLOGS for a SPECIFIC USER based on userID
     */
    @GetMapping("/{userID}/mealLog")
    public ResponseEntity<?> getAllMealLogsByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);

        List<MealLog> mealLogs = mealLogRepo.findAll();
        return ResponseEntity.ok(mealLogs);
    }

    }
