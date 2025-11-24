package models;

import java.time.LocalDate;

/**
 * Progress model for daily metrics.
 */
public class Progress {
    private int progressId;
    private int userId;
    private LocalDate day;
    private Integer steps;
    private Double sleepHours;
    private String notes;

    public Progress() {}

    // getters and setters
    public int getProgressId() { return progressId; }
    public void setProgressId(int progressId) { this.progressId = progressId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Double getSleepHours() { return sleepHours; }
    public void setSleepHours(Double sleepHours) { this.sleepHours = sleepHours; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
