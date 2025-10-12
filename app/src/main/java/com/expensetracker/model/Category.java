package com.expensetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color_hex")
    public String colorHex;

    @ColumnInfo(name = "icon_name")
    public String iconName;

    @ColumnInfo(name = "is_default")
    public boolean isDefault;

    public Category(String name, String colorHex, String iconName, boolean isDefault) {
        this.name = name;
        this.colorHex = colorHex;
        this.iconName = iconName;
        this.isDefault = isDefault;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}