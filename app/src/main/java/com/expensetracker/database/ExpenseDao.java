package com.expensetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseWithCategory;


import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Transaction
    @Query("SELECT * FROM expenses ORDER BY date_time DESC")
    LiveData<List<ExpenseWithCategory>> getAllExpensesWithCategory();

    @Query("SELECT SUM(amount) FROM expenses WHERE date_time BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalAmountByDateRange(long startDate, long endDate);
}