package se.mau.mattiasjonsson.assignment4;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.List;

import se.mau.mattiasjonsson.assignment4.database.*;

public class DatabaseController {
    private User user;
    private StepSession stepSession;
    private Repository repository;

    public DatabaseController(@NonNull Application application){
        user = new User();
        stepSession = new StepSession();
        repository = new Repository(application);
    }

    public void logStepSession(int steps, String time, String username){
        stepSession.setSteps(steps);
        stepSession.setTime(time);
        stepSession.setUserID(repository.getUserID(username));
        repository.insertStepSession(stepSession);
    }

    public int getStepSessionID(String username){
        return repository.getStepSessionID(repository.getUserID(username));
    }

    public List<StepSession> getStepSessions(String username){
        return repository.getStepSessions(repository.getUserID(username));
    }

    public void updateStepSession(int steps, int stepSessionID){
        repository.updateStepSession(steps, stepSessionID);
    }

    public boolean registerUser(String username, String password){
        try {
            user.setUsername(username);
            user.setPassword(password);
            repository.insertUser(user);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean login(String username, String password){
        return (password.equals(repository.getPassword(username)));
    }

    public void clearHistory(String username){
        repository.clearHistory(repository.getUserID(username));
    }
}
