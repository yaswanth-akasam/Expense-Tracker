package com.expensetracker.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Category.class}, version = 1, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();

    private static volatile ExpenseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExpenseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExpenseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "expense_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                try {
                    CategoryDao dao = INSTANCE.categoryDao();
                    
                    // Default Categories
                    dao.insert(new Category("Food", "#FF5722", "restaurant", true));
                    dao.insert(new Category("Travel", "#9C27B0", "directions_car", true));
                    dao.insert(new Category("Bills", "#607D8B", "receipt", true));
                    dao.insert(new Category("Shopping", "#E91E63", "shopping_cart", true));
                    dao.insert(new Category("Health", "#4CAF50", "local_hospital", true));
                    dao.insert(new Category("Entertainment", "#FF9800", "movie", true));

                    // Additional Categories
                    dao.insert(new Category("Groceries", "#FFC107", "local_grocery_store", true));
                    dao.insert(new Category("Transport", "#03A9F4", "commute", true));
                    dao.insert(new Category("Utilities", "#00BCD4", "power", true));
                    dao.insert(new Category("Rent", "#8BC34A", "home", true));
                    dao.insert(new Category("Education", "#795548", "school", true));
                    dao.insert(new Category("Gifts", "#673AB7", "card_giftcard", true));
                    dao.insert(new Category("Personal Care", "#F44336", "spa", true));
                    dao.insert(new Category("Subscriptions", "#2196F3", "subscriptions", true));

                    // New Additional Categories
                    dao.insert(new Category("Maintenance", "#8D6E63", "build", true));
                    dao.insert(new Category("Investments", "#42A5F5", "trending_up", true));
                    dao.insert(new Category("Hobbies", "#7E57C2", "palette", true));
                    dao.insert(new Category("Kids", "#FF8A65", "child_friendly", true));
                    dao.insert(new Category("Pets", "#A1887F", "pets", true));

                    dao.insert(new Category("Other", "#9E9E9E", "category", true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };
}