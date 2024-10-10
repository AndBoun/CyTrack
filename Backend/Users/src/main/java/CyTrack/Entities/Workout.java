package CyTrack.Entities;

import jakarta.persistence.*;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;
    private Long workoutID; // ??



}
