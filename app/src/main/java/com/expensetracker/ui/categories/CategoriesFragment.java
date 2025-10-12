package com.expensetracker.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.expensetracker.databinding.FragmentCategoriesBinding;
import com.expensetracker.adapter.CategoryAdapter;

public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private CategoryAdapter categoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        observeViewModel();

        return root;
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter();
        binding.recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void observeViewModel() {
        categoriesViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategories(categories);
        });
        
        categoryAdapter.setOnCategoryClickListener(category -> {
            android.content.Intent intent = new android.content.Intent(getContext(), com.expensetracker.ui.CategoryExpensesActivity.class);
            intent.putExtra("category_name", category.getName());
            intent.putExtra("category_color", category.getColorHex());
            intent.putExtra("category_id", category.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}