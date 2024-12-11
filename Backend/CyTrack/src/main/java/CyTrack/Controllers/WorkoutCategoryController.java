package CyTrack.Controllers;

import CyTrack.Entities.Workout;
import CyTrack.Entities.WorkoutCategory;
import CyTrack.Entities.User;
import CyTrack.Responses.WorkoutCategoryResponse;
import CyTrack.Services.WorkoutCategoryService;
import CyTrack.Services.WorkoutService;
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
@RequestMapping("/workoutCategory")
public class WorkoutCategoryController {

    private final WorkoutCategoryService workoutCategoryService;
    private final UserService userService;
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutCategoryController(WorkoutCategoryService workoutCategoryService, UserService userService, WorkoutService workoutService) {
        this.workoutCategoryService = workoutCategoryService;
        this.userService = userService;
        this.workoutService = workoutService;
    }

    // ============================ Get all workout categories for a specific user ============================ //
    @Operation(
            summary = "Get workout categories for a user",
            description = "Retrieves workout categories for a specific user by user ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User's workout categories retrieved"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{userId}/workoutCategory")
    public ResponseEntity<?> getWorkoutCategoriesByUser(@PathVariable Long userId) {
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

        List<WorkoutCategory> userWorkoutCategories = workoutCategoryService.getUserWorkoutCategories(userOpt.get());
        List<WorkoutCategoryResponse.WorkoutCategoryData> workoutCategoryDataList = userWorkoutCategories.stream()
                .map(category -> new WorkoutCategoryResponse.WorkoutCategoryData(
                        category.getWorkoutCategoryId(),
                        category.getWorkoutCategoryName()
                ))
                .collect(Collectors.toList());

        WorkoutCategoryResponse response = new WorkoutCategoryResponse("success", workoutCategoryDataList, "User's workout categories retrieved");
        return ResponseEntity.ok(response);
    }

    // ============================ Get Workouts By Specific User and Category ============================ //
    @GetMapping("/{userId}/workoutCategory/{workoutCategoryId}")
    public ResponseEntity<WorkoutResponse> getWorkoutsByCategoryAndUser(
            @PathVariable Long userId,
            @PathVariable Long workoutCategoryId) {
        try {
            // Validate input
            if (userId == null || workoutCategoryId == null) {
                return ResponseEntity.badRequest()
                        .body(new WorkoutResponse("failure", null, "User ID and Workout Category ID must be provided"));
            }

            // Fetch workouts for the given user and category
            List<Workout> workouts = workoutService.getWorkoutsByCategoryAndUser(workoutCategoryId, userId);

            if (workouts.isEmpty()) {
                return ResponseEntity.ok(new WorkoutResponse("success", List.of(), "No workouts found for the specified category and user"));
            }

            // Map to WorkoutResponse.WorkoutData
            List<WorkoutResponse.WorkoutData> workoutData = workouts.stream()
                    .map(workout -> new WorkoutResponse.WorkoutData(
                            workout.getExerciseType(),
                            workout.getDuration(),
                            workout.getCalories(),
                            workout.getDate(),
                            workout.getWorkoutID(),
                            workout.getWorkoutCategories()))
                    .toList();

            WorkoutResponse response = new WorkoutResponse("success", workoutData, "Workouts retrieved successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(new WorkoutResponse("failure", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new WorkoutResponse("failure", null, "An unexpected error occurred: " + e.getMessage()));
        }
    }



    // ============================ Add a Workout Category By UserId ============================ //
    @PostMapping("/{userId}/workoutCategory")
    public ResponseEntity<?> addWorkoutCategoryByUserId(@PathVariable Long userId, @RequestBody WorkoutCategory workoutCategory) {
        try {
            User user = userService.findByUserID(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            workoutCategory.setUser(user);
            WorkoutCategory savedCategory = workoutCategoryService.addWorkoutCategory(workoutCategory);

            WorkoutCategoryResponse.WorkoutCategoryData savedData =
                    new WorkoutCategoryResponse.WorkoutCategoryData(savedCategory.getWorkoutCategoryId(), savedCategory.getWorkoutCategoryName());

            WorkoutCategoryResponse response = new WorkoutCategoryResponse("success", List.of(savedData), "Workout category added successfully");
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

    // ============================ Add a Workout to a Workout Category ============================ //
    @PostMapping("/{workoutCategoryId}/addWorkout/{workoutId}")
    public ResponseEntity<?> addWorkoutToCategory(@PathVariable Long workoutCategoryId, @PathVariable Long workoutId) {
        try {
            // Add the workout to the category
            workoutCategoryService.addWorkoutToCategory(workoutCategoryId, workoutId);

            // Fetch the updated category
            WorkoutCategory updatedCategory = workoutCategoryService.getWorkoutCategoryById(workoutCategoryId);

            // Map to WorkoutCategoryData
            WorkoutCategoryResponse.WorkoutCategoryData categoryData = new WorkoutCategoryResponse.WorkoutCategoryData(
                    updatedCategory.getWorkoutCategoryId(),
                    updatedCategory.getWorkoutCategoryName()
            );

            // Wrap in response
            WorkoutCategoryResponse.Data responseData = new WorkoutCategoryResponse.Data(List.of(categoryData));

            // Return success response
            return ResponseEntity.ok(new WorkoutCategoryResponse(
                    "success",
                    responseData.getWorkoutCategories(),
                    "Workout added to category successfully"
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



    // ============================ Delete a Workout Category By UserId ============================ //
    @DeleteMapping("/{userId}/workoutCategory/{workoutCategoryId}")
    public ResponseEntity<?> deleteWorkoutCategoryByUserId(@PathVariable Long userId, @PathVariable Long workoutCategoryId) {
        try {
            workoutCategoryService.deleteWorkoutCategory(userId, workoutCategoryId);
            WorkoutCategoryResponse response = new WorkoutCategoryResponse("success", null, "Workout category deleted successfully");
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
