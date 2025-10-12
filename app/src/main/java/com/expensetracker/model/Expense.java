package com.expensetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "expenses",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"category_id"})})
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "date_time")
    public long dateTime;

    @ColumnInfo(name = "note")
    public String note;

    public Expense(double amount, int categoryId, long dateTime, String note) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.dateTime = dateTime;
        this.note = note;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public long getDateTime() { return dateTime; }
    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}