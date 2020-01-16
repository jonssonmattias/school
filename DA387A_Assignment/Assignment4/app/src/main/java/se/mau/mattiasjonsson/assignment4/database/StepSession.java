package se.mau.mattiasjonsson.assignment4.database;

import android.arch.persistence.room.*;

@Entity(tableName = "stepsession")
public class StepSession {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stepSessionID")
    private int stepSessionID;

    @ColumnInfo(name = "steps")
    private int steps;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "userID")
    private int userID;

    public int getStepSessionID() { return stepSessionID; }

    public int getSteps() { return steps; }

    public String getTime() { return time;}

    public int getUserID() { return userID; }

    public void setStepSessionID(int stepSessionID) {this.stepSessionID = stepSessionID; }

    public void setSteps(int steps) { this.steps = steps; }

    public void setTime(String time) { this.time = time; }

    public void setUserID(int userID) { this.userID = userID; }
}
