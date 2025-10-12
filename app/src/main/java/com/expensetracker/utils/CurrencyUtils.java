package com.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrencyUtils {
    private static final String PREFS_NAME = "ExpenseTrackerPrefs";
    private static final String CURRENCY_KEY = "currency";
    private static final String DEFAULT_CURRENCY = "USD";

    public static String getSelectedCurrency(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(CURRENCY_KEY, DEFAULT_CURRENCY);
    }

    public static String formatAmount(Context context, double amount) {
        String currency = getSelectedCurrency(context);
        String symbol = getCurrencySymbol(currency);
        return String.format("%s%.2f", symbol, amount);
    }

    private static String getCurrencySymbol(String currency) {
        switch (currency) {
            case "USD": return "$";
            case "EUR": return "€";
            case "GBP": return "£";
            case "INR": return "₹";
            default: return "$";
        }
    }
}