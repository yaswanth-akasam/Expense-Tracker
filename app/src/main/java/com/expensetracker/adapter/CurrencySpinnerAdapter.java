package com.expensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.expensetracker.R;

public class CurrencySpinnerAdapter extends ArrayAdapter<String> {
    private final String[] currencies;

    public CurrencySpinnerAdapter(@NonNull Context context, @NonNull String[] currencies) {
        super(context, 0, currencies);
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_with_flag, parent, false);
        }

        ImageView iconView = convertView.findViewById(R.id.spinner_icon);
        TextView textView = convertView.findViewById(R.id.spinner_text);

        String currency = getItem(position);

        if (currency != null) {
            textView.setText(currency);

            int resId = getContext().getResources().getIdentifier("ic_flag_" + currency.toLowerCase(), "drawable", getContext().getPackageName());
            if (resId != 0) {
                iconView.setImageResource(resId);
            } else {
                // Handle case where flag is not found, maybe a default icon
                iconView.setImageResource(R.drawable.ic_category); // Placeholder
            }
        }

        return convertView;
    }
}