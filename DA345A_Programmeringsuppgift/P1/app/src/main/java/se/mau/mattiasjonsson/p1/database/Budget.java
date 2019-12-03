package se.mau.mattiasjonsson.p1.database;

import android.arch.persistence.room.*;

@Entity(tableName = "budget")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int budgetID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "username")
    private String username;

    public void setBudgetID(int budgetID) {
        this.budgetID = budgetID;
    }

    public int getBudgetID() {
        return budgetID;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
