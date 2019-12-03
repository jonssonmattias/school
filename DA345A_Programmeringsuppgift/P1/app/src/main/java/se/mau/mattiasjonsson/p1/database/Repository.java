package se.mau.mattiasjonsson.p1.database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;


public class Repository {
    private BudgetDao budgetDao;
    private Budget budget;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        budgetDao = database.budgetDao();
    }

    public void insert(Budget budget) {
        new InsertAsyncTask(budgetDao).execute(budget);
    }

    public List<Budget> select(int type, String username){
        return (type==2?budgetDao.getAll(username):budgetDao.getAll(type, username));
    }

    public List<Budget> selectFromAndTo(String from, String to, int type, String username){
        return (type==2?budgetDao.findByDateFromAndTo(from, to, username):budgetDao.findByDateFromAndTo(from, to, type, username));
    }

    public List<Budget> selectFrom(String from, int type, String username){
        return (type==2?budgetDao.findByDateFrom(from, username):budgetDao.findByDateFrom(from, type, username));
    }

    public List<Budget> selectTo(String to, int type, String username){
        return (type==2?budgetDao.findByDateTo(to, username):budgetDao.findByDateTo(to, type, username));
    }

    private static class InsertAsyncTask extends AsyncTask<Budget, Void, Void> {
        private BudgetDao budgetDao;

        public InsertAsyncTask(BudgetDao budgetDao){
            this.budgetDao=budgetDao;
        }

        @Override
        protected Void doInBackground(Budget... budget) {
            budgetDao.insertAll(budget);
            return null;
        }
    }
}
