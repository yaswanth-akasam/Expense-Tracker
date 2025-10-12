package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.expensetracker.databinding.ActivityMainBinding;
import com.expensetracker.ui.AddExpenseActivity;
import com.expensetracker.utils.ThemeHelper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        
        // Setup FAB click listener
        binding.fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }
}