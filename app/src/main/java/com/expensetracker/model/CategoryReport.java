package com.expensetracker.model;

public class CategoryReport {
    public String categoryName;
    public String colorHex;
    public double total;

    public CategoryReport(String categoryName, String colorHex, double total) {
        this.categoryName = categoryName;
        this.colorHex = colorHex;
        this.total = total;
    }
}