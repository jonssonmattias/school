package se.mau.mattiasjonsson.p1.database;

import android.arch.persistence.room.*;
import java.util.List;

@Dao
public interface BudgetDao {

    @Query("SELECT * FROM budget WHERE username == :username")
    List<Budget> getAll(String username);

    @Query("SELECT * FROM budget WHERE type == :type AND username == :username")
    List<Budget> getAll(int type, String username);

    @Query("SELECT * FROM budget WHERE date >= :dateFrom AND date <= :dateTo AND username == :username")
    List<Budget> findByDateFromAndTo(String dateFrom, String dateTo, String username);

    @Query("SELECT * FROM budget WHERE date >= :dateFrom AND date <= :dateTo AND type == :type AND username == :username")
    List<Budget> findByDateFromAndTo(String dateFrom, String dateTo, int type, String username);

    @Query("SELECT * FROM budget WHERE date >= :dateFrom AND username == :username")
    List<Budget> findByDateFrom(String dateFrom, String username);

    @Query("SELECT * FROM budget WHERE date >= :dateFrom AND type == :type AND username == :username")
    List<Budget> findByDateFrom(String dateFrom, int type, String username);

    @Query("SELECT * FROM budget WHERE date <= :dateTo AND username == :username")
    List<Budget> findByDateTo(String dateTo ,String username);

    @Query("SELECT * FROM budget WHERE date <= :dateTo AND type == :type AND username == :username")
    List<Budget> findByDateTo(String dateTo, int type, String username);

    @Insert
    void insertAll(Budget... budgets);
}