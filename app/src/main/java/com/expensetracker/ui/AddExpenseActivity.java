package com.expensetracker.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.expensetracker.adapter.CategorySpinnerAdapter;
import com.expensetracker.databinding.ActivityAddExpenseBinding;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    private ActivityAddExpenseBinding binding;
    private AddExpenseViewModel viewModel;
    private List<Category> categories;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(AddExpenseViewModel.class);
        selectedDateTime = Calendar.getInstance();

        setupViews();
        observeViewModel();
        loadExpenseData();
    }

    private void loadExpenseData() {
        boolean editMode = getIntent().getBooleanExtra("edit_mode", false);
        if (editMode) {
            double amount = getIntent().getDoubleExtra("amount", 0);
            int categoryId = getIntent().getIntExtra("category_id", -1);
            long dateTime = getIntent().getLongExtra("date_time", System.currentTimeMillis());
            String note = getIntent().getStringExtra("note");
            
            binding.editTextAmount.setText(String.valueOf(amount));
            binding.editTextNote.setText(note);
            selectedDateTime.setTimeInMillis(dateTime);
            updateDateTimeDisplay();
            
            // Set category selection after categories are loaded
            viewModel.getAllCategories().observe(this, categories -> {
                if (categories != null) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getId() == categoryId) {
                            binding.spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupViews() {
        binding.buttonSelectDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSelectTime.setOnClickListener(v -> showTimePicker());
        binding.buttonSave.setOnClickListener(v -> saveExpense());
        binding.buttonCancel.setOnClickListener(v -> finish());

        updateDateTimeDisplay();
    }

    private void observeViewModel() {
        viewModel.getAllCategories().observe(this, categories -> {
            this.categories = categories;
            setupCategorySpinner();
        });
    }

    private void setupCategorySpinner() {
        if (categories != null && !categories.isEmpty()) {
            CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, categories);
            binding.spinnerCategory.setAdapter(adapter);
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        binding.textSelectedDate.setText(
                String.format("%02d/%02d/%d",
                        selectedDateTime.get(Calendar.MONTH) + 1,
                        selectedDateTime.get(Calendar.DAY_OF_MONTH),
                        selectedDateTime.get(Calendar.YEAR)));
        
        binding.textSelectedTime.setText(
                String.format("%02d:%02d",
                        selectedDateTime.get(Calendar.HOUR_OF_DAY),
                        selectedDateTime.get(Calendar.MINUTE)));
    }

    private void saveExpense() {
        String amountStr = binding.editTextAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            
            if (categories == null || categories.isEmpty()) {
                Toast.makeText(this, "Categories not loaded yet. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            int categoryPosition = binding.spinnerCategory.getSelectedItemPosition();
            
            if (categoryPosition >= 0 && categoryPosition < categories.size()) {
                Category selectedCategory = categories.get(categoryPosition);
                String note = binding.editTextNote.getText().toString().trim();
                
                Expense expense = new Expense(amount, selectedCategory.getId(),
                        selectedDateTime.getTimeInMillis(), note);
                
                viewModel.insert(expense);
                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}