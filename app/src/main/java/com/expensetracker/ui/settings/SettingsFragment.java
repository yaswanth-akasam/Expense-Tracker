package com.expensetracker.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.expensetracker.adapter.CurrencySpinnerAdapter;
import com.expensetracker.database.ExpenseDatabase;
import com.expensetracker.databinding.FragmentSettingsBinding;
import com.expensetracker.utils.DateUtils;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prefs = requireActivity().getSharedPreferences("ExpenseTrackerPrefs", Context.MODE_PRIVATE);
        
        loadSettings();
        setupListeners();

        return root;
    }

    private void loadSettings() {
        float budget = prefs.getFloat("monthly_budget", 1000f);
        binding.editBudget.setText(String.valueOf(budget));
        
        String currency = prefs.getString("currency", "USD");
        String[] currencies = getResources().getStringArray(com.expensetracker.R.array.currencies);
        binding.spinnerCurrency.setAdapter(new CurrencySpinnerAdapter(requireContext(), currencies));
        binding.spinnerCurrency.setSelection(getCurrencyPosition(currency));

        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        binding.switchDarkMode.setChecked(isDarkMode);
        applyTheme(isDarkMode);
    }

    private void setupListeners() {
        binding.buttonSaveBudget.setOnClickListener(v -> {
            String budgetStr = binding.editBudget.getText().toString();
            if (!budgetStr.isEmpty()) {
                float budget = Float.parseFloat(budgetStr);
                prefs.edit().putFloat("monthly_budget", budget).apply();
                Toast.makeText(getContext(), "Budget saved", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonResetData.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle("Reset Current Month's Data")
                .setMessage("Are you sure you want to delete all expenses for the current month? This action cannot be undone.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    resetCurrentMonthData();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            applyTheme(isChecked);
            requireActivity().recreate();
        });

        binding.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] currencies = getResources().getStringArray(com.expensetracker.R.array.currencies);
                String selectedCurrency = currencies[position];
                prefs.edit().putString("currency", selectedCurrency).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void resetCurrentMonthData() {
        long[] monthRange = DateUtils.getCurrentMonthRange();
        
        ExpenseDatabase.databaseWriteExecutor.execute(() -> {
            ExpenseDatabase.getDatabase(requireContext()).expenseDao().deleteExpensesByDateRange(monthRange[0], monthRange[1]);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Current month's data has been reset.", Toast.LENGTH_SHORT).show();
                // Restart the app to reflect changes
                Intent i = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        });
    }

    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private int getCurrencyPosition(String currency) {
        String[] currencies = getResources().getStringArray(com.expensetracker.R.array.currencies);
        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i].equals(currency)) return i;
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}