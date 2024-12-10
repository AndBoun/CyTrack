package CyTrack.Controllers;

import CyTrack.Entities.MealCategory;
import CyTrack.Entities.User;
import CyTrack.Responses.MealCategoryResponse;
import CyTrack.Services.MealCategoryService;
import CyTrack.Services.MealService;
import CyTrack.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mealCategory")
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;
    private final UserService userService;

    @Autowired
    public MealCategoryController(MealCategoryService mealCategoryService, UserService userService) {
        this.mealCategoryService = mealCategoryService;
        this.userService = userService;
    }

    // ============================ Get meal categories for a specific user ============================ //
    @Operation(
            summary = "Get meal categories for a user",
            description = "Retrieves meal categories for a specific user by user ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User's meal categories retrieved"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMealCategoriesByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findByUserID(userId);

        if (userOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "error",
                    404,
                    "User not found",
                    "No user exists with ID: " + userId
            );
            return ResponseEntity.status(404).body(errorResponse);
        }

        List<MealCategory> userMealCategories = mealCategoryService.getUserMealCategories(userOpt.get());
        List<MealCategoryResponse.MealCategoryData> mealCategoryDataList = userMealCategories.stream()
                .map(category -> new MealCategoryResponse.MealCategoryData(
                        category.getMealCategoryId(),
                        category.getMealCategoryName()
                ))
                .collect(Collectors.toList());

        MealCategoryResponse response = new MealCategoryResponse("success", mealCategoryDataList, "User's meal categories retrieved");
        return ResponseEntity.ok(response);
    }

    // ============================ Add a Meal Category By UserId ============================ //
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> addMealCategoryByUserId(@PathVariable Long userId, @RequestBody MealCategory mealCategory) {
        try {
            User user = userService.findByUserID(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            mealCategory.setUser(user);
            MealCategory savedCategory = mealCategoryService.addMealCategory(mealCategory);

            MealCategoryResponse.MealCategoryData savedData =
                    new MealCategoryResponse.MealCategoryData(savedCategory.getMealCategoryId(), savedCategory.getMealCategoryName());

            MealCategoryResponse response = new MealCategoryResponse("success", List.of(savedData), "Meal category added successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "error",
                    400,
                    "Bad Request",
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // ============================ Delete a Meal Category By UserId ============================ //
    @DeleteMapping("/user/{userId}/{mealCategoryId}")
    public ResponseEntity<?> deleteMealCategoryByUserId(@PathVariable Long userId, @PathVariable Long mealCategoryId) {
        try {
            mealCategoryService.deleteMealCategory(userId, mealCategoryId);
            MealCategoryResponse response = new MealCategoryResponse("success", null, "Meal category deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "error",
                    400,
                    "Bad Request",
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
