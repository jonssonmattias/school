package se.mau.mattiasjonsson.assignment4.database;

import android.arch.persistence.room.*;
import android.content.Context;

@Database(entities = {User.class, StepSession.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract Dao dao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "budget-database").allowMainThreadQueries().build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}