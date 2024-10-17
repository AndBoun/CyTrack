package CyTrack.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

/**
 * @author Eduardo Barboza-Campos
 * Class representing a table for a 'Meal Log' entity.
 * Represents a dairy entry for a given day.
 * This diary entry is composed of
 * a unique ID
 * a list of meals for the day,
 * and a date
 */
@Entity
public class MealLog {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;

    private List<Meal> mealList;

    private String logDate;

    // =============================== Constructors ================================== //

    public MealLog(){}

    public MealLog(String date) {
        this.logDate = date;
    }

    // =============================== Getters and Setters for each field ================================== //

}
