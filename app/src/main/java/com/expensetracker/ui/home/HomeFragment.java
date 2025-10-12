package com.expensetracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.expensetracker.databinding.FragmentHomeBinding;
import com.expensetracker.adapter.ExpenseAdapter;
import com.expensetracker.utils.CurrencyUtils;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ExpenseAdapter expenseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        observeViewModel();

        return root;
    }

    private void setupRecyclerView() {
        expenseAdapter = new ExpenseAdapter();
        expenseAdapter.setShowActions(false); // Hide edit/delete buttons in home
        binding.recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewExpenses.setAdapter(expenseAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            expenseAdapter.setExpenses(expenses);
        });

        homeViewModel.getCurrentMonthTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.textTotalSpent.setText(CurrencyUtils.formatAmount(getContext(), total));
                updateBudgetProgress(total);
            } else {
                binding.textTotalSpent.setText(CurrencyUtils.formatAmount(getContext(), 0));
                updateBudgetProgress(0);
            }
        });
    }

    private void updateBudgetProgress(double spent) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("ExpenseTrackerPrefs", android.content.Context.MODE_PRIVATE);
        double budget = prefs.getFloat("monthly_budget", 1000f);
        double remaining = budget - spent;
        
        binding.textBudget.setText("Budget: " + CurrencyUtils.formatAmount(getContext(), budget));
        binding.textRemaining.setText("Remaining: " + CurrencyUtils.formatAmount(getContext(), remaining));
        
        int progress = (int) ((spent / budget) * 100);
        binding.progressBudget.setProgress(Math.min(progress, 100));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}