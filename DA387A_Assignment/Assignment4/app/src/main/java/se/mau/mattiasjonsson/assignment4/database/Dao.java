package se.mau.mattiasjonsson.assignment4.database;

import android.arch.persistence.room.*;
import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {

    @Insert
    void insertUser(User... users);

    @Insert
    void insertStepSession(StepSession... stepSessions);

    @Query("SELECT password FROM user WHERE username = :username;")
    String getPassword(String username);

    @Query("SELECT userID FROM user WHERE username = :username;")
    int getUserID(String username);

    @Query("SELECT * FROM stepsession WHERE userID = :userID ORDER BY stepSessionID DESC;")
    List<StepSession> getStepSessions(int userID);

    @Query("SELECT stepSessionID FROM stepsession WHERE userID = :userID ORDER BY stepSessionID DESC LIMIT 1")
    int getStepSessionID(int userID);

    @Query("UPDATE stepsession SET steps = :steps WHERE stepSessionID = :stepSessionID")
    void updateStepSession(int steps, int stepSessionID);

    @Query("DELETE FROM stepsession WHERE userID = :userID;")
    void clearHistory(int userID);
}