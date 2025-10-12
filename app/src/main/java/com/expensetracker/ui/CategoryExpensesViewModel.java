package com.expensetracker.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.ExpenseDao;
import com.expensetracker.model.ExpenseWithCategory;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryExpensesViewModel extends AndroidViewModel {
    private ExpenseDao expenseDao;

    public CategoryExpensesViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
    }

    public LiveData<List<ExpenseWithCategory>> getExpensesByCategory(int categoryId) {
        return Transformations.map(expenseDao.getAllExpensesWithCategory(), expenses -> 
            expenses.stream()
                .filter(expense -> expense.expense.getCategoryId() == categoryId)
                .collect(Collectors.toList())
        );
    }

    public void deleteExpense(com.expensetracker.model.Expense expense) {
        com.expensetracker.database.ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            com.expensetracker.database.ExpenseDatabase database = com.expensetracker.database.ExpenseDatabase.getDatabase(getApplication());
            database.expenseDao().delete(expense);
        });
    }
}