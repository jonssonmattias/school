package se.mau.mattiasjonsson.assignment4.database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;


public class Repository {
    private Dao dao;
    private User user;
    private StepSession stepSession;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.dao();
    }

    public void insertUser(User user) {
        new InsertUserAsyncTask(dao).execute(user);
    }

    public void insertStepSession(StepSession stepSession){ new InsertStepSessionAsyncTask(dao).execute(stepSession); }

    public String getPassword(String username){
        return dao.getPassword(username);
    }

    public int getUserID(String username){ return dao.getUserID(username); }

    public List<StepSession> getStepSessions(int userID){
        return dao.getStepSessions(userID);
    }

    public int getStepSessionID(int userID){
        return dao.getStepSessionID(userID);
    }

    public void updateStepSession(int steps, int stepSessionID){new UpdateStepSessionAsyncTask(dao).execute(new int[]{steps, stepSessionID}); }

    public void clearHistory(int userID){ dao.clearHistory(userID);}

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private Dao dao;
        public InsertUserAsyncTask(Dao dao){
            this.dao=dao;
        }
        @Override
        protected Void doInBackground(User... users) {
            dao.insertUser(users);
            return null;
        }
    }

    private static class InsertStepSessionAsyncTask extends AsyncTask<StepSession, Void, Void> {
        private Dao dao;
        public InsertStepSessionAsyncTask(Dao dao){
            this.dao=dao;
        }
        @Override
        protected Void doInBackground(StepSession... stepSessions) {
            dao.insertStepSession(stepSessions);
            return null;
        }
    }

    private static class UpdateStepSessionAsyncTask extends AsyncTask<int[], Void, Void> {
        private Dao dao;
        public UpdateStepSessionAsyncTask(Dao dao){
            this.dao=dao;
        }
        @Override
        protected Void doInBackground(int[]... ints) {
            int steps = ints[0][0];
            int stepSessionID = ints[0][1];
            dao.updateStepSession(steps, stepSessionID);
            return null;
        }
    }
}
