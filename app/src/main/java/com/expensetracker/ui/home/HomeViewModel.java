package com.expensetracker.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.ExpenseDao;
import com.expensetracker.model.ExpenseWithCategory;
import com.expensetracker.utils.DateUtils;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private ExpenseDao expenseDao;
    private LiveData<List<ExpenseWithCategory>> allExpenses;
    private LiveData<Double> currentMonthTotal;

    public HomeViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpensesWithCategory();
        
        long[] monthRange = DateUtils.getCurrentMonthRange();
        currentMonthTotal = expenseDao.getTotalAmountByDateRange(monthRange[0], monthRange[1]);
    }

    public LiveData<List<ExpenseWithCategory>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getCurrentMonthTotal() {
        return currentMonthTotal;
    }

    public void deleteExpense(com.expensetracker.model.Expense expense) {
        com.expensetracker.database.ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            com.expensetracker.database.ExpenseDatabase database = com.expensetracker.database.ExpenseDatabase.getDatabase(getApplication());
            database.expenseDao().delete(expense);
        });
    }
}