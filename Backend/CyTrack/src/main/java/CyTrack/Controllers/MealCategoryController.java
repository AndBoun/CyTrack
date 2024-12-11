package CyTrack.Controllers;

import CyTrack.Entities.Meal;
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
import CyTrack.Responses.Util.ErrorResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mealCategory")
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;
    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public MealCategoryController(MealCategoryService mealCategoryService, UserService userService, MealService mealService) {
        this.mealCategoryService = mealCategoryService;
        this.userService = userService;
        this.mealService = mealService;
    }

    // ============================ Get all meal categories for a specific user ============================ //
    @Operation(
            summary = "Get meal categories for a user",
            description = "Retrieves meal categories for a specific user by user ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User's meal categories retrieved"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{userId}/mealCategory")
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

    // ============================ Get Meals By Specific User and Category ============================ //
    @GetMapping("/{userId}/mealCategory/{mealCategoryId}")
    public ResponseEntity<MealResponse> getMealsByCategoryAndUser(
            @PathVariable Long userId,
            @PathVariable Long mealCategoryId) {
        try {
            // Validate input
            if (userId == null || mealCategoryId == null) {
                return ResponseEntity.badRequest()
                        .body(new MealResponse("failure", null, "User ID and Meal Category ID must be provided"));
            }

            // Fetch meals for the given user and category
            List<Meal> meals = mealService.getMealsByCategoryAndUser(mealCategoryId, userId);

            if (meals.isEmpty()) {
                return ResponseEntity.ok(new MealResponse("success", List.of(), "No meals found for the specified category and user"));
            }

            // Map to MealResponse.MealData
            List<MealResponse.MealData> mealData = meals.stream()
                    .map(meal -> new MealResponse.MealData(
                            meal.getMealId(),
                            meal.getMealName(),
                            meal.getCalories(),
                            meal.getProtein(),
                            meal.getCarbs(),
                            meal.getTime(),
                            meal.getDate(),
                            meal.getMealCategories()))
                    .toList();

            MealResponse response = new MealResponse("success", mealData, "Meals retrieved successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(new MealResponse("failure", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new MealResponse("failure", null, "An unexpected error occurred: " + e.getMessage()));
        }
    }



    // ============================ Add a Meal Category By UserId ============================ //
    @PostMapping("/{userId}/mealCategory")
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

    // ============================ Add a Meal to a Meal Category ============================ //
    @PostMapping("/{mealCategoryId}/addMeal/{mealId}")
    public ResponseEntity<?> addMealToCategory(@PathVariable Long mealCategoryId, @PathVariable Long mealId) {
        try {
            // Add the meal to the category
            mealCategoryService.addMealToCategory(mealCategoryId, mealId);

            // Fetch the updated category
            MealCategory updatedCategory = mealCategoryService.getMealCategoryById(mealCategoryId);

            // Map to MealCategoryData
            MealCategoryResponse.MealCategoryData categoryData = new MealCategoryResponse.MealCategoryData(
                    updatedCategory.getMealCategoryId(),
                    updatedCategory.getMealCategoryName()
            );

            // Wrap in response
            MealCategoryResponse.Data responseData = new MealCategoryResponse.Data(List.of(categoryData));

            // Return success response
            return ResponseEntity.ok(new MealCategoryResponse(
                    "success",
                    responseData.getMealCategories(),
                    "Meal added to category successfully"
            ));
        } catch (IllegalArgumentException e) {
            // Construct error response
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
    @DeleteMapping("/{userId}/mealCategory/{mealCategoryId}")
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
