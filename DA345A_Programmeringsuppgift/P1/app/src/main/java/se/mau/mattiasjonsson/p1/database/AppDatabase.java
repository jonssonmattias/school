package se.mau.mattiasjonsson.p1.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.*;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import se.mau.mattiasjonsson.p1.*;

@Database(entities = {Budget.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract BudgetDao budgetDao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "budget-database").allowMainThreadQueries().build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE budget "
                    + " ADD COLUMN username TEXT");
        }
    };
}