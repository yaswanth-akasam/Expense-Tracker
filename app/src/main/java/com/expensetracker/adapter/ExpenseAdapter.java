package com.expensetracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.R;
import com.expensetracker.model.ExpenseWithCategory;
import com.expensetracker.utils.DateUtils;
import com.expensetracker.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<ExpenseWithCategory> expenses = new ArrayList<>();
    private OnExpenseClickListener listener;

    public interface OnExpenseClickListener {
        void onExpenseClick(ExpenseWithCategory expense);
        void onExpenseEdit(ExpenseWithCategory expense);
        void onExpenseDelete(ExpenseWithCategory expense);
    }

    public void setOnExpenseClickListener(OnExpenseClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseWithCategory current = expenses.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<ExpenseWithCategory> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public void setShowActions(boolean showActions) {
        this.showActions = showActions;
    }

    private boolean showActions = true;

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView textAmount;
        private TextView textCategory;
        private TextView textNote;
        private TextView textDateTime;
        private View categoryIndicator;
        private android.widget.ImageButton buttonEdit;
        private android.widget.ImageButton buttonDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textAmount = itemView.findViewById(R.id.text_amount);
            textCategory = itemView.findViewById(R.id.text_category);
            textNote = itemView.findViewById(R.id.text_note);
            textDateTime = itemView.findViewById(R.id.text_date_time);
            categoryIndicator = itemView.findViewById(R.id.category_indicator);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onExpenseClick(expenses.get(position));
                }
            });

            buttonEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onExpenseEdit(expenses.get(position));
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onExpenseDelete(expenses.get(position));
                }
            });
        }

        public void bind(ExpenseWithCategory expenseWithCategory) {
            textAmount.setText(CurrencyUtils.formatAmount(itemView.getContext(), expenseWithCategory.expense.getAmount()));
            textCategory.setText(expenseWithCategory.category.getName());
            textNote.setText(expenseWithCategory.expense.getNote());
            textDateTime.setText(DateUtils.formatDateTime(expenseWithCategory.expense.getDateTime()));
            
            // Set category color
            try {
                int color = Color.parseColor(expenseWithCategory.category.getColorHex());
                categoryIndicator.setBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                categoryIndicator.setBackgroundColor(Color.GRAY);
            }
            
            // Show/hide action buttons
            if (buttonEdit != null && buttonDelete != null) {
                buttonEdit.setVisibility(showActions ? android.view.View.VISIBLE : android.view.View.GONE);
                buttonDelete.setVisibility(showActions ? android.view.View.VISIBLE : android.view.View.GONE);
            }
        }
    }
}