package com.expensetracker.ui.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.expensetracker.databinding.FragmentReportsBinding;
import com.expensetracker.utils.DateUtils;
import com.expensetracker.utils.CurrencyUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {
    private FragmentReportsBinding binding;
    private ReportsViewModel reportsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupListeners();
        observeViewModel();

        return root;
    }

    private void setupListeners() {
        binding.buttonNextMonth.setOnClickListener(v -> reportsViewModel.nextMonth());
        binding.buttonPrevMonth.setOnClickListener(v -> reportsViewModel.prevMonth());
    }

    private void observeViewModel() {
        reportsViewModel.getSelectedMonth().observe(getViewLifecycleOwner(), calendar -> {
            binding.textMonth.setText(DateUtils.getMonthYearFromCalendar(calendar));
        });

        reportsViewModel.getMonthTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.textTotalAmount.setText(CurrencyUtils.formatAmount(getContext(), total));
            } else {
                binding.textTotalAmount.setText(CurrencyUtils.formatAmount(getContext(), 0));
            }
            updateBudgetProgress();
        });

        reportsViewModel.getCategoryReports().observe(getViewLifecycleOwner(), reports -> {
            binding.layoutCategories.removeAllViews();
            if (reports != null && !reports.isEmpty()) {
                updatePieChart(reports);
                for (int i = 0; i < Math.min(reports.size(), 5); i++) {
                    addCategoryItem(reports.get(i));
                }
            }
        });

        reportsViewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            updateBudgetProgress();
        });
    }

    private void updateBudgetProgress() {
        Double total = reportsViewModel.getMonthTotal().getValue();
        Float budget = reportsViewModel.getBudget().getValue();

        if (total != null && budget != null && budget > 0) {
            int progress = (int) ((total / budget) * 100);
            binding.progressBar.setProgress(progress);
            binding.textProgress.setText(String.format("%d%%", progress));
        } else {
            binding.progressBar.setProgress(0);
            binding.textProgress.setText("0%");
        }
    }

    private void updatePieChart(List<com.expensetracker.model.CategoryReport> reports) {
        List<com.expensetracker.ui.PieChartView.PieSlice> slices = new ArrayList<>();
        
        for (com.expensetracker.model.CategoryReport report : reports) {
            try {
                int color = Color.parseColor(report.colorHex);
                slices.add(new com.expensetracker.ui.PieChartView.PieSlice(
                    report.categoryName, report.total, color));
            } catch (Exception e) {
                slices.add(new com.expensetracker.ui.PieChartView.PieSlice(
                    report.categoryName, report.total, Color.GRAY));
            }
        }
        
        binding.pieChart.setData(slices);
    }

    private void addCategoryItem(com.expensetracker.model.CategoryReport report) {
        MaterialCardView card = new MaterialCardView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setCardElevation(4f);
        card.setRadius(12f);
        card.setContentPadding(24, 24, 24, 24);

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        View colorView = new View(requireContext());
        LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(8, LinearLayout.LayoutParams.MATCH_PARENT);
        colorParams.setMargins(0, 0, 16, 0);
        colorView.setLayoutParams(colorParams);
        try {
            colorView.setBackgroundColor(Color.parseColor(report.colorHex));
        } catch (Exception e) {
            colorView.setBackgroundColor(Color.GRAY);
        }

        LinearLayout textLayout = new LinearLayout(requireContext());
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView nameText = new TextView(requireContext());
        nameText.setText(report.categoryName);
        nameText.setTextSize(16f);
        nameText.setTextColor(Color.BLACK);

        TextView amountText = new TextView(requireContext());
        amountText.setText(CurrencyUtils.formatAmount(requireContext(), report.total));
        amountText.setTextSize(18f);
        amountText.setTextColor(Color.parseColor("#2196F3"));

        // Add percentage
        TextView percentText = new TextView(requireContext());
        reportsViewModel.getMonthTotal().observe(getViewLifecycleOwner(), totalAmount -> {
            if (totalAmount != null && totalAmount > 0) {
                double percentage = (report.total / totalAmount) * 100;
                percentText.setText(String.format("%.1f%%", percentage));
            } else {
                percentText.setText("0.0%");
            }
        });
        percentText.setTextSize(14f);
        percentText.setTextColor(Color.GRAY);

        textLayout.addView(nameText);
        textLayout.addView(amountText);
        textLayout.addView(percentText);

        layout.addView(colorView);
        layout.addView(textLayout);
        card.addView(layout);

        binding.layoutCategories.addView(card);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}