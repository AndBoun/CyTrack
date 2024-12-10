package CyTrack.Controllers;

import CyTrack.Entities.Meal;
import CyTrack.Entities.User;

import CyTrack.Services.MealService;
import CyTrack.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Meal Controller", description = "REST APIs related to Meal")
@RestController
@RequestMapping("/meal")
public class MealController {

    private final MealService mealService;
    private final UserService userService;

    @Autowired
    public MealController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }

    /**
     * LIST of ALL MEALS for a SPECIFIC USER
     *
     * @return list of all meals given that belong to a given user
     */
    @Operation(
            summary = "List All Meals for a User",
            description = "Get a list of all meals for a specified user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Meals found",
                            content = @Content(schema = @Schema(implementation = MealResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user to retrieve meals for", required = true, example = "1")
    })
    @GetMapping("/{userID}/meal")
    public ResponseEntity<?> getAllMealsByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Meal> meals = mealService.getAllMeals(userID);
            List<MealResponse.MealData> mealDataList = meals.stream()
                    .map(meal -> new MealResponse.MealData(
                            meal.getMealId(),
                            meal.getMealName(),
                            meal.getCalories(),
                            meal.getProtein(),
                            meal.getCarbs(),
                            meal.getTime(),
                            meal.getDate(),
                            meal.getMealCategories()
                    ))
                    .toList();
            MealResponse response = new MealResponse("success", mealDataList, "Meals found");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Unable to find user given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * READ/return contents of an individual meal
     *
     * @param mealId of meal to return
     * @return individual meal object
     */
    @Operation(
            summary = "Get Meal by ID",
            description = "Retrieve details of a meal by meal ID for a specific user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Meal found",
                            content = @Content(schema = @Schema(implementation = MealResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or meal not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "mealId", description = "ID of the meal to retrieve", required = true, example = "100")
    })
    @GetMapping("/{mealId}/meal/{mealID}")
    public ResponseEntity<?> getMealById(@PathVariable Long userID, @PathVariable Long mealId) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> meal = mealService.findByMealID(mealId);
            if (meal.isPresent()) {
                Meal foundMeal = meal.get();
                MealResponse.MealData mealData = new MealResponse.MealData(
                        foundMeal.getMealId(),
                        foundMeal.getMealName(),
                        foundMeal.getCalories(),
                        foundMeal.getProtein(),
                        foundMeal.getCarbs(),
                        foundMeal.getTime(),
                        foundMeal.getDate(),
                        foundMeal.getMealCategories()
                );
                MealResponse response = new MealResponse("success", List.of(mealData), "Meal found");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "Could not find meal with given mealID");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Could not find user with the given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * GET all meals by userID and date
     */
    @Operation(
            summary = "Get Meals by Date",
            description = "Get all meals logged by a user on a specified date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Meals found for date",
                            content = @Content(schema = @Schema(implementation = MealResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "date", description = "Date to retrieve meals for, in format YYYY-MM-DD", required = true, example = "2024-11-13")
    })
    @GetMapping("/{userID}/mealsByDate/{date}")
    public ResponseEntity<?> getMealsByUserIDAndDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Meal> meals = mealService.getMealsByUserIDAndDate(userID, date);
            List<MealResponse.MealData> mealDataList = meals.stream()
                    .map(meal -> new MealResponse.MealData(
                            meal.getMealId(),
                            meal.getMealName(),
                            meal.getCalories(),
                            meal.getProtein(),
                            meal.getCarbs(),
                            meal.getTime(),
                            meal.getDate(),
                            meal.getMealCategories()
                    ))
                    .toList();
            MealResponse response = new MealResponse("success", mealDataList, "Meals found for date " + date);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "Could not find user with given userID"));
    }

    /**
     * GET total calories, protein, and carbs for a given userID and date
     */
    @Operation(
            summary = "Calculate Total Nutrients by Date",
            description = "Calculate total calories, protein, and carbs consumed by a user on a specific date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total nutrients calculated",
                            content = @Content(schema = @Schema(implementation = NutrientSummaryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "date", description = "Date to calculate nutrients for, in format YYYY-MM-DD", required = true, example = "2024-11-13")
    })
    @GetMapping("/{userID}/nutrients/{date}")
    public ResponseEntity<?> getTotalNutrientsForDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            MealService.NutrientSummary nutrientSummary = mealService.calculateTotalNutrients(userID, date);
            NutrientSummaryResponse response = new NutrientSummaryResponse(
                    "success",
                    nutrientSummary,
                    "Total nutrients calculated for date " + date
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "Could not find user with given userID"));
    }


    /**
     * CREATE new meal entry
     *
     * @param meal we're attempting to add/create
     * @return response indicating whether meal was added or not
     */
    @Operation(
            summary = "Create a Meal Entry",
            description = "Add a new meal entry for a user.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Meal created",
                            content = @Content(schema = @Schema(implementation = MealIDResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user to add the meal for", required = true, example = "1"),
            @Parameter(name = "meal", description = "JSON body representing the meal details", required = true, schema = @Schema(implementation = Meal.class))
    })
    @PostMapping("/{userID}/meal")
    public ResponseEntity<?> createMeal(@PathVariable Long userID, @RequestBody Meal meal) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            meal.setUser(user.get());
            Meal newMeal = mealService.createMeal(meal);

            MealIDResponse response = new MealIDResponse("success", newMeal.getMealId(), "Meal created");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "Could not find user with the given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * DELETE a meal row based on a given input
     */
    @Operation(
            summary = "Delete a Meal Entry",
            description = "Delete a meal entry by meal ID for a specified user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Meal deleted",
                            content = @Content(schema = @Schema(implementation = MealIDResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or meal not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "mealID", description = "ID of the meal to delete", required = true, example = "100")
    })
    @DeleteMapping("/{userID}/meal/{mealID}")
    ResponseEntity<?> deleteMeal(@PathVariable Long userID, @PathVariable Long mealID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> meal = mealService.findByMealID(mealID);
            if (meal.isPresent()) {
                mealService.deleteMeal(mealID);
                MealIDResponse response = new MealIDResponse("success", mealID, "Meal deleted");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "could not find meal with given mealID");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "could not find user with given userID");
        return ResponseEntity.status(404).body(response);
    }

    /**
     * UPDATE/replace existing meal based on given JSON input
     *
     * @param mealID  of Meal to update
     * @param newMeal Meal object (from JSON) containing info for updating
     * @return response status indicating success or failure
     */
    @Operation(
            summary = "Update a Meal Entry",
            description = "Update an existing meal entry with new details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Meal updated",
                            content = @Content(schema = @Schema(implementation = MealIDResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or meal not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "mealID", description = "ID of the meal to update", required = true, example = "100"),
            @Parameter(name = "newMeal", description = "JSON body with updated meal details", required = true, schema = @Schema(implementation = Meal.class))
    })
    @PutMapping("/{userID}/meal/{mealID}")
    public ResponseEntity<?> updateMeal(@PathVariable Long userID, @PathVariable Long mealID, @RequestBody Meal newMeal) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Meal> existingMeal = mealService.findByMealID(mealID);
            if (existingMeal.isPresent()) {
                Meal updatedMeal = existingMeal.get();
                //update fields if new values not null
                if (newMeal.getMealName() != null) {
                    updatedMeal.setMealName(newMeal.getMealName());
                }
                if (newMeal.getCalories() != null) {
                    updatedMeal.setCalories(newMeal.getCalories());
                }
                if (newMeal.getProtein() != null) {
                    updatedMeal.setProtein(newMeal.getProtein());
                }
                if (newMeal.getCarbs() != null) {
                    updatedMeal.setCarbs(newMeal.getCarbs());
                }
                if (newMeal.getTime() != null) {
                    updatedMeal.setTime(newMeal.getTime());
                }
                if (newMeal.getDate() != null) {
                    updatedMeal.setDate(newMeal.getDate());
                }
                mealService.createMeal(updatedMeal);
                MealIDResponse response = new MealIDResponse("success", mealID, "Meal created");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Meal not found", "could not find meal with given mealID");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "could not find user with given userID");
        return ResponseEntity.status(404).body(response);
    }
}
