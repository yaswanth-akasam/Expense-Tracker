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
import com.expensetracker.model.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    public CategorySpinnerAdapter(@NonNull Context context, @NonNull List<Category> categories) {
        super(context, 0, categories);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_with_icon, parent, false);
        }

        ImageView iconView = convertView.findViewById(R.id.spinner_icon);
        TextView textView = convertView.findViewById(R.id.spinner_text);

        Category category = getItem(position);

        if (category != null) {
            textView.setText(category.getName());

            int resId = getContext().getResources().getIdentifier("ic_" + category.getIconName(), "drawable", getContext().getPackageName());
            if (resId != 0) {
                iconView.setImageResource(resId);
            } else {
                iconView.setImageResource(R.drawable.ic_category); // A default icon
            }
        }

        return convertView;
    }
}