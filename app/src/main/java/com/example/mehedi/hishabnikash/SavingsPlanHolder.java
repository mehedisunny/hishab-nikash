package com.example.mehedi.hishabnikash;

public class SavingsPlanHolder {

    private int _id;
    private int _budgetAmount;
    private int _expenseAmount;
    private int _month;
    private int _year;

    public SavingsPlanHolder (int budgetAmount, int month, int year) {
        this._budgetAmount = budgetAmount;
        this._month = month;
        this._year = year;
    }

    public SavingsPlanHolder (int budgetAmount, int expenseAmount, int month, int year) {
        this._budgetAmount = budgetAmount;
        this._expenseAmount = expenseAmount;
        this._month = month;
        this._year = year;
    }

    public SavingsPlanHolder (int id, int budgetAmount, int expenseAmount, int month, int year) {
        this._id = id;
        this._budgetAmount = budgetAmount;
        this._expenseAmount = expenseAmount;
        this._month = month;
        this._year = year;
    }

    public int getId () {
        return this._id;
    }

    public int getBudgetAmount () {
        return this._budgetAmount;
    }

    public int getExpenseAmount () {
        return this._expenseAmount;
    }

    public int getMonth () {
        return this._month;
    }

    public int getYear() {
        return this. _year;
    }
}
