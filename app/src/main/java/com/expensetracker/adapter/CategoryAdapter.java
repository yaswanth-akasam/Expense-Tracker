package com.expensetracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.R;
import com.expensetracker.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category current = categories.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textCategoryName;
        private com.google.android.material.card.MaterialCardView iconContainer;
        private android.widget.ImageView categoryIcon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName = itemView.findViewById(R.id.text_category_name);
            iconContainer = itemView.findViewById(R.id.icon_container);
            categoryIcon = itemView.findViewById(R.id.category_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCategoryClick(categories.get(position));
                }
            });
        }

        public void bind(Category category) {
            textCategoryName.setText(category.getName());
            
            try {
                int color = Color.parseColor(category.getColorHex());
                iconContainer.setCardBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                iconContainer.setCardBackgroundColor(Color.GRAY);
            }
            
            // Set appropriate icon based on category name
            setIconForCategory(category.getName());
        }
        
        private void setIconForCategory(String categoryName) {
            int iconRes;
            switch (categoryName.toLowerCase()) {
                case "food":
                    iconRes = R.drawable.ic_food;
                    break;
                case "travel":
                    iconRes = R.drawable.ic_travel;
                    break;
                case "bills":
                    iconRes = R.drawable.ic_bills;
                    break;
                case "shopping":
                    iconRes = R.drawable.ic_shopping;
                    break;
                case "health":
                    iconRes = R.drawable.ic_health;
                    break;
                case "entertainment":
                    iconRes = R.drawable.ic_entertainment;
                    break;
                default:
                    iconRes = R.drawable.ic_other;
                    break;
            }
            categoryIcon.setImageResource(iconRes);
        }
    }
}