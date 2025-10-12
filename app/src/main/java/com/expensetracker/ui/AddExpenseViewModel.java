package com.expensetracker.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.ExpenseDao;
import com.expensetracker.database.CategoryDao;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import java.util.List;

public class AddExpenseViewModel extends AndroidViewModel {
    private ExpenseDao expenseDao;
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;

    public AddExpenseViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Expense expense) {
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.insert(expense);
        });
    }
}