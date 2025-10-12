package com.expensetracker.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.expensetracker.databinding.FragmentSettingsBinding;

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
            Toast.makeText(getContext(), "Reset data feature coming soon", Toast.LENGTH_SHORT).show();
        });

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            applyTheme(isChecked);
            requireActivity().recreate();
        });

        binding.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] currencies = {"USD", "EUR", "GBP", "INR"};
                String selectedCurrency = currencies[position];
                prefs.edit().putString("currency", selectedCurrency).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        String[] currencies = {"USD", "EUR", "GBP", "INR"};
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