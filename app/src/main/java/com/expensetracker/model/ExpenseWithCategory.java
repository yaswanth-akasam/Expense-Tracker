package com.expensetracker.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ExpenseWithCategory {
    @Embedded
    public Expense expense;

    @Relation(
            parentColumn = "category_id",
            entityColumn = "id"
    )
    public Category category;

    public ExpenseWithCategory() {}

    public Expense getExpense() { return expense; }
    public void setExpense(Expense expense) { this.expense = expense; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}