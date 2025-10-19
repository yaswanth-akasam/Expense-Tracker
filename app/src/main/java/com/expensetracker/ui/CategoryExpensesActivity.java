package com.expensetracker.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.expensetracker.databinding.ActivityCategoryExpensesBinding;
import com.expensetracker.adapter.ExpenseAdapter;
import com.expensetracker.utils.CurrencyUtils;

public class CategoryExpensesActivity extends AppCompatActivity {
    private ActivityCategoryExpensesBinding binding;
    private CategoryExpensesViewModel viewModel;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String categoryName = getIntent().getStringExtra("category_name");
        String categoryColor = getIntent().getStringExtra("category_color");
        int categoryId = getIntent().getIntExtra("category_id", -1);

        setupUI(categoryName, categoryColor);
        setupRecyclerView();
        setupViewModel(categoryId);
    }

    private void setupUI(String categoryName, String categoryColor) {
        binding.textCategoryName.setText(categoryName);
        
        try {
            int color = Color.parseColor(categoryColor);
            binding.categoryHeader.setBackgroundColor(color);
        } catch (Exception e) {
            binding.categoryHeader.setBackgroundColor(Color.GRAY);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName + " Expenses");
        }
    }

    private void setupRecyclerView() {
        expenseAdapter = new ExpenseAdapter();
        expenseAdapter.setShowActions(true); // Show edit/delete buttons in category view
        binding.recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewExpenses.setAdapter(expenseAdapter);
        
        expenseAdapter.setOnExpenseClickListener(new com.expensetracker.adapter.ExpenseAdapter.OnExpenseClickListener() {
            @Override
            public void onExpenseClick(com.expensetracker.model.ExpenseWithCategory expense) {
                // Handle expense click if needed
            }

            @Override
            public void onExpenseEdit(com.expensetracker.model.ExpenseWithCategory expense) {
                android.content.Intent intent = new android.content.Intent(CategoryExpensesActivity.this, com.expensetracker.ui.AddExpenseActivity.class);
                intent.putExtra("expense_id", expense.expense.getId());
                intent.putExtra("amount", expense.expense.getAmount());
                intent.putExtra("category_id", expense.expense.getCategoryId());
                intent.putExtra("date_time", expense.expense.getDateTime());
                intent.putExtra("note", expense.expense.getNote());
                intent.putExtra("edit_mode", true);
                startActivity(intent);
            }

            @Override
            public void onExpenseDelete(com.expensetracker.model.ExpenseWithCategory expense) {
                new androidx.appcompat.app.AlertDialog.Builder(CategoryExpensesActivity.this)
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        viewModel.deleteExpense(expense.expense);
                        android.widget.Toast.makeText(CategoryExpensesActivity.this, "Expense deleted", android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            }
        });
    }

    private void setupViewModel(int categoryId) {
        viewModel = new ViewModelProvider(this).get(CategoryExpensesViewModel.class);
        viewModel.getExpensesByCategory(categoryId).observe(this, expenses -> {
            if (expenses == null || expenses.isEmpty()) {
                binding.recyclerViewExpenses.setVisibility(android.view.View.GONE);
                binding.textEmptyState.setVisibility(android.view.View.VISIBLE);
            } else {
                binding.recyclerViewExpenses.setVisibility(android.view.View.VISIBLE);
                binding.textEmptyState.setVisibility(android.view.View.GONE);
                expenseAdapter.setExpenses(expenses);
            }
            
            binding.textExpenseCount.setText(expenses.size() + " expenses");
            double total = expenses.stream().mapToDouble(e -> e.expense.getAmount()).sum();
            binding.textTotalAmount.setText(CurrencyUtils.formatAmount(CategoryExpensesActivity.this, total));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}