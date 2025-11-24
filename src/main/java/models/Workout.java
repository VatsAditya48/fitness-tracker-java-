package models;

import java.time.LocalDate;

/**
 * Workout model.
 */
public class Workout {
    private int workoutId;
    private int userId;
    private String type;
    private int durationMinutes;
    private int caloriesBurned;
    private LocalDate workoutDate;

    public Workout() {}

    // getters and setters
    public int getWorkoutId() { return workoutId; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public LocalDate getWorkoutDate() { return workoutDate; }
    public void setWorkoutDate(LocalDate workoutDate) { this.workoutDate = workoutDate; }
}