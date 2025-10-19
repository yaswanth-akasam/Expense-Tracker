package com.expensetracker.ui.categories;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.CategoryDao;
import com.expensetracker.model.Category;

import java.util.List;

public class CategoriesViewModel extends AndroidViewModel {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;

    public CategoriesViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getCategoriesWithExpenses();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category category) {
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.insert(category);
        });
    }

    public void update(Category category) {
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.update(category);
        });
    }

    public void delete(Category category) {
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.delete(category);
        });
    }
}