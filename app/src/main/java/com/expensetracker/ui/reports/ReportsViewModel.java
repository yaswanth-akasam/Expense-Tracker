package com.expensetracker.ui.reports;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.database.ExpenseDao;
import com.expensetracker.model.CategoryReport;

import java.util.Calendar;
import java.util.List;

public class ReportsViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final ExpenseDao expenseDao;
    private final SharedPreferences prefs;
    private final MutableLiveData<Calendar> selectedMonth = new MutableLiveData<>();
    private final LiveData<Double> monthTotal;
    private final LiveData<List<CategoryReport>> categoryReports;
    private final MutableLiveData<Float> budget = new MutableLiveData<>();

    public ReportsViewModel(Application application) {
        super(application);
        ExpenseDatabase database = ExpenseDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        prefs = application.getSharedPreferences("ExpenseTrackerPrefs", Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);

        selectedMonth.setValue(Calendar.getInstance());

        monthTotal = Transformations.switchMap(selectedMonth, calendar -> {
            long[] range = getMonthRange(calendar);
            return expenseDao.getTotalAmountByDateRange(range[0], range[1]);
        });

        categoryReports = Transformations.switchMap(selectedMonth, calendar -> {
            long[] range = getMonthRange(calendar);
            return expenseDao.getCategoryReportsByDateRange(range[0], range[1]);
        });

        loadBudget();
    }

    private void loadBudget() {
        float monthlyBudget = prefs.getFloat("monthly_budget", 1000f);
        budget.setValue(monthlyBudget);
    }

    private long[] getMonthRange(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long startDate = cal.getTimeInMillis();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        long endDate = cal.getTimeInMillis();
        return new long[]{startDate, endDate};
    }

    public void nextMonth() {
        Calendar cal = selectedMonth.getValue();
        if (cal != null) {
            cal.add(Calendar.MONTH, 1);
            selectedMonth.setValue(cal);
        }
    }

    public void prevMonth() {
        Calendar cal = selectedMonth.getValue();
        if (cal != null) {
            cal.add(Calendar.MONTH, -1);
            selectedMonth.setValue(cal);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("monthly_budget")) {
            loadBudget();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    public LiveData<Calendar> getSelectedMonth() {
        return selectedMonth;
    }

    public LiveData<Double> getMonthTotal() {
        return monthTotal;
    }

    public LiveData<List<CategoryReport>> getCategoryReports() {
        return categoryReports;
    }

    public LiveData<Float> getBudget() {
        return budget;
    }
}
