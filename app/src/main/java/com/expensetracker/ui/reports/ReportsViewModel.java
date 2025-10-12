package com.expensetracker.ui.reports;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.ExpenseDao;
import com.expensetracker.model.CategoryReport;
import com.expensetracker.model.ExpenseWithCategory;
import com.expensetracker.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsViewModel extends AndroidViewModel {
    private ExpenseDao expenseDao;
    private LiveData<Double> monthTotal;
    private LiveData<List<CategoryReport>> categoryReports;

    public ReportsViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        
        long[] monthRange = DateUtils.getCurrentMonthRange();
        monthTotal = expenseDao.getTotalAmountByDateRange(monthRange[0], monthRange[1]);
        
        LiveData<List<ExpenseWithCategory>> expenses = expenseDao.getAllExpensesWithCategory();
        categoryReports = Transformations.map(expenses, this::calculateCategoryReports);
    }

    private List<CategoryReport> calculateCategoryReports(List<ExpenseWithCategory> expenses) {
        Map<String, CategoryReport> reportMap = new HashMap<>();
        
        for (ExpenseWithCategory expense : expenses) {
            String categoryName = expense.category.getName();
            if (!reportMap.containsKey(categoryName)) {
                reportMap.put(categoryName, new CategoryReport(
                    categoryName,
                    expense.category.getColorHex(),
                    0
                ));
            }
            CategoryReport report = reportMap.get(categoryName);
            report.total += expense.expense.getAmount();
        }
        
        return new ArrayList<>(reportMap.values());
    }

    public LiveData<List<CategoryReport>> getCategoryReports() {
        return categoryReports;
    }

    public LiveData<Double> getMonthTotal() {
        return monthTotal;
    }
}